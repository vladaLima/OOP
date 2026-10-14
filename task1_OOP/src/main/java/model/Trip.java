package model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Trip {
    private final Route route;
    private final LocalTime departureTime;
    private final DayType dayType;

    public Trip(Route route, LocalTime departureTime, DayType dayType) {
        if (route == null) {
            throw new IllegalArgumentException("Route cannot be null");
        }
        if (departureTime == null) {
            throw new IllegalArgumentException("DepartureTime cannot be null");
        }
        if (dayType == null) {
            throw new IllegalArgumentException("Day cannot be null");
        }

        this.route = route;
        this.departureTime = departureTime;
        this.dayType = dayType;
    }

    public Route getRoute() {
        return route;
    }

    public LocalTime getDepartureTime() {
        return departureTime;
    }

    public DayType getDayType() {
        return dayType;
    }

    public LocalTime getArrivalTimeAt(int stopIndex) {
        var travelTime = route.getDurationToStop(stopIndex);
        return departureTime.plus(travelTime);
    }

    public LocalDateTime getArrivalDateTimeAt(LocalDate departureDate, int stopIndex) {
        if (departureDate == null) {
            throw new IllegalArgumentException("Departure date cannot be null");
        }

        var departureDateTime = LocalDateTime.of(departureDate, departureTime);
        return departureDateTime.plus(route.getDurationToStop(stopIndex));
    }

    public boolean passesThrough(Stop targetStop) {
        return route.containsStop(targetStop);
    }
}
