package model;

import java.time.Duration;

public record RouteSegment(Stop stop, Duration durationOfTrip) {
    public RouteSegment {
        if (stop == null) {
            throw new IllegalArgumentException("Stop cannot be null");
        }
        if (durationOfTrip == null || durationOfTrip.isNegative()) {
            throw new IllegalArgumentException("Duration cannot be negative");
        }
    }
}
