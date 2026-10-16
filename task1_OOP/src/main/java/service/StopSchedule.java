package service;

import exception.ScheduleNotFoundException;
import model.DayType;
import model.Stop;
import model.Trip;

import java.time.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class StopSchedule {
    private final Stop stop;
    private final List<Trip> allTrips;

    public StopSchedule(Stop stop, List<Trip> allTrips) {
        if (stop == null) {
            throw new IllegalArgumentException("Stop cannot be null");
        }
        if (allTrips == null) {
            throw new IllegalArgumentException("Trips cannot be null");
        }

        this.stop = stop;
        this.allTrips = List.copyOf(allTrips);
    }

    public List<StopScheduleEntry> getScheduleEntries(DayType dayType) throws ScheduleNotFoundException {
        if (dayType == null) {
            throw new IllegalArgumentException("Day cannot be null");
        }

        List<StopScheduleEntry> schedule = new ArrayList<>();

        for (Trip trip : allTrips) {
            if (trip.getDayType() != dayType) {
                continue;
            }

            List<Integer> indexes = trip.getRoute().indexesOfStop(stop);

            for (int index : indexes) {
                schedule.add(new StopScheduleEntry(trip, index, trip.getArrivalTimeAt(index)));
            }
        }

        if (schedule.isEmpty()) {
            throw new ScheduleNotFoundException(dayType.name());
        }

        schedule.sort(Comparator.comparing(StopScheduleEntry::arrivalTime));

        return List.copyOf(schedule);
    }

    public List<StopScheduleDateEntry> getScheduleDateEntries(LocalDate date) throws ScheduleNotFoundException {
        if (date == null) {
            throw new IllegalArgumentException("Arrival date cannot be null");
        }

        List<StopScheduleDateEntry> schedule = new ArrayList<>();

        for (Trip trip : allTrips) {
            List<Integer> indexes = trip.getRoute().indexesOfStop(stop);
            for (int index : indexes) {
                LocalDateTime expectedArrivalTime = LocalDateTime.of(date, trip.getArrivalTimeAt(index));
                Duration duration = trip.getRoute().getDurationToStop(index);
                LocalDateTime departureDateTime = expectedArrivalTime.minus(duration);
                LocalDate tripDate = departureDateTime.toLocalDate();
                LocalDateTime arrivalDateTime = trip.getArrivalDateTimeAt(tripDate, index);

                if (trip.getDayType() != getDayType(tripDate)) {
                    continue;
                }

                schedule.add(new StopScheduleDateEntry(trip, index, departureDateTime, arrivalDateTime));
            }
        }

        if (schedule.isEmpty()) {
            throw new ScheduleNotFoundException(date);
        }

        schedule.sort(Comparator.comparing(StopScheduleDateEntry::arrivalDateTime));

        return List.copyOf(schedule);
    }

    private DayType getDayType(LocalDate date) {
        DayOfWeek day = date.getDayOfWeek();

        return switch (day) {
            case SATURDAY, SUNDAY -> DayType.WEEKEND;
            case MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY -> DayType.WEEKDAY;
        };
    }
}