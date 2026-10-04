package model;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class Route {
    private final int number;
    private final String name;
    private final TransportType type;
    private final List<RouteSegment> segments;

    public Route(int number, String name, TransportType type, List<RouteSegment> segments) {
        if (number <= 0) {
            throw new IllegalArgumentException("Number of route must be positive");
        } else if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Route name cannot be null or blank");
        }
        this.number = number;
        this.name = name;
        this.type = type;
        this.segments = new ArrayList<>(segments);
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

    public Duration getTotalDuration() {
        return segments.stream().map(RouteSegment::durationOfTrip).reduce(Duration.ZERO, Duration::plus);
    }
}
