package service;

import exception.ScheduleNotFoundException;
import model.DayType;
import model.Stop;
import model.Trip;

import java.time.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Расписание прибытия рейсов на конкретную остановку.
 */
public class StopSchedule {
    private final Stop stop;
    private final List<Trip> allTrips;

    /**
     * Создаёт расписание остановки на основе списка рейсов.
     *
     * @param stop остановка, для которой составляется расписание
     * @param allTrips список доступных рейсов
     * @throws IllegalArgumentException если остановка или список равны null
     */
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

    /**
     * Формирует отсортированное по времени расписание остановки для типа дня.
     *
     * @param dayType будний или выходной день
     * @return неизменяемый список записей расписания
     * @throws ScheduleNotFoundException если подходящих рейсов нет
     * @throws IllegalArgumentException если тип дня равен null
     */
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

    /**
     * Формирует расписание на календарную дату с учётом типа дня отправления каждого рейса.
     *
     * @param date дата, на которую составляется расписание прибытия
     * @return неизменяемый список записей с датами и временем
     * @throws ScheduleNotFoundException если расписание пусто
     * @throws IllegalArgumentException если дата равна null
     */
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