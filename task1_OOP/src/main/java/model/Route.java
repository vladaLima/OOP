package model;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class Route {
    private final int number;
    private final String name;
    private final TransportType type;
    private final List<RouteSegment> segments;
    private final Duration interval;

    public Route(int number, String name, TransportType type, List<RouteSegment> segments, Duration interval) {
        if (number <= 0) {
            throw new IllegalArgumentException("Number of route must be positive");
        } else if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Route name cannot be null or blank");
        } else if (type == null) {
            throw new IllegalArgumentException("Route type cannot be null");
        } else if (segments == null) {
            throw new IllegalArgumentException("Route segments cannot be null");
        } else if (interval.isNegative()) {
            throw new IllegalArgumentException("Route interval must be positive");
        }

        this.number = number;
        this.name = name;
        this.type = type;
        this.segments = new ArrayList<>(segments);
        this.interval = interval;
    }

    public int getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    public String getFullName() {
        return type.getTransportType() + " №" + number + " (" + name + ")";
    }

    public TransportType getType() {
        return type;
    }

    public List<RouteSegment> getSegments() {
        return List.copyOf(this.segments);
    }

    public Duration getInterval() {
        return interval;
    }

    public Duration getTotalDuration() {
        return segments.stream().map(RouteSegment::durationOfTrip).reduce(Duration.ZERO, Duration::plus);
    }

    public Duration getDurationToStop(int stopIndex) {
        if (stopIndex < 0 || stopIndex >= segments.size()) {
            throw new IllegalArgumentException("Stop index out of bounds");
        }

        var durationToStop = Duration.ZERO;

        for (int i = 0; i < stopIndex; i++) {
           durationToStop = durationToStop.plus(segments.get(i).durationOfTrip());
        }

        return durationToStop;
    }
}
