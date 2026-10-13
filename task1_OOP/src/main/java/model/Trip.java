package model;

import java.time.LocalTime;

public class Trip {
    private Route route;
    private final LocalTime departureTime;

    public Trip(Route route, LocalTime departureTime) {
        this.route = route;
        this.departureTime = departureTime;
    }

    public Route getRoute() {
        return route;
    }

    public LocalTime getDepartureTime() {
        return departureTime;
    }

    public LocalTime getArrivalTimeAt(int stopIndex) {
        var travelTime = route.getDurationToStop(stopIndex);
        return departureTime.plus(travelTime);
    }

    public boolean passesThrough(Stop targetStop) {
        return route.getSegments().stream().anyMatch(stop -> stop.equals(targetStop));
    }
}
