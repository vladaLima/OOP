package service;

import model.DayType;
import model.Route;
import model.Trip;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class TripGenerator {
    public List<Trip> generateTrips(Route route, LocalTime startTime, LocalTime endTime, DayType dayType) {
        if (route == null) {
            throw new IllegalArgumentException("Route cannot be null");
        }
        if (startTime == null || endTime == null) {
            throw new IllegalArgumentException("Start time and end time cannot be null");
        }
        if (dayType == null) {
            throw new IllegalArgumentException("Day type cannot be null");
        }
        if (endTime.isBefore(startTime)) {
            throw new IllegalArgumentException("End time cannot be before start time");
        }

        Duration interval = route.getInterval();

        if (interval == null || interval.isNegative() || interval.isZero()) {
            throw new IllegalArgumentException("Route interval must be positive");
        }

        LocalDate baseDate = LocalDate.now();
        LocalDateTime current = LocalDateTime.of(baseDate, startTime);
        LocalDateTime end = LocalDateTime.of(baseDate, endTime);

        List<Trip> trips = new ArrayList<>();
        while (!current.isAfter(end)) {
            trips.add(new Trip(route, current.toLocalTime(), dayType));
            current = current.plus(interval);
        }

        return List.copyOf(trips);
    }
}
