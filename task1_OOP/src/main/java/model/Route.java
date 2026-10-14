package model;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Route {
    private final int number;
    private final String name;
    private final TransportType type;
    private final List<RouteSegment> segments;
    private final Duration interval;

    public Route(int number, String name, TransportType type, List<RouteSegment> segments, Duration interval) {
        if (number <= 0) {
            throw new IllegalArgumentException("Route number must be positive");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Route name cannot be null or blank");
        }
        if (type == null) {
            throw new IllegalArgumentException("Route type cannot be null");
        }
        if (segments == null || segments.size() < 2) {
            throw new IllegalArgumentException("Route must contain at least two stops");
        }
        if (interval == null || interval.isNegative() || interval.isZero()) {
            throw new IllegalArgumentException("Route interval must be positive");
        }


        for (int i = 0; i < segments.size(); i++) {
            RouteSegment segment = segments.get(i);

            if (segment == null) {
                throw new IllegalArgumentException("Route segment cannot be null");
            }

            Duration duration = segment.durationOfTrip();

            boolean isLast = i == segments.size() - 1;

            if (isLast && !duration.isZero()) {
                throw new IllegalArgumentException("Last stop duration must be zero");
            }
            if (!isLast && duration.isZero()) {
                throw new IllegalArgumentException("Duration time between stops must be positive");
            }
        }

        this.number = number;
        this.name = name;
        this.type = type;
        this.segments = List.copyOf(segments);
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

    public Duration getDurationBetweenStops(int fromIndex, int toIndex) {
        if (fromIndex < 0 || fromIndex >= segments.size() || toIndex < 0 || toIndex >= segments.size() || fromIndex >= toIndex) {
            throw new IllegalArgumentException("Invalid stop indexes");
        }

        return getDurationToStop(toIndex).minus(getDurationToStop(fromIndex));
    }

    public List<Integer> indexesOfStop(Stop stop) {
        if (stop == null) {
            throw new IllegalArgumentException("Stop cannot be null");
        }

        List<Integer> indexes = new ArrayList<>();

        for (int i = 0; i < segments.size(); i++) {
            if (segments.get(i).stop().equals(stop)) {
                indexes.add(i);
            }
        }

        return List.copyOf(indexes);
    }

    public boolean canTravel(Stop from, Stop to) {
        if (from == null || to == null) {
            throw new IllegalArgumentException("Stops cannot be null");
        }

        for (int i = 0; i < segments.size(); i++) {
            if (segments.get(i).stop().equals(from)) {
                for (int j = i + 1; j < segments.size(); j++) {
                    if (segments.get(j).stop().equals(to)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public boolean containsStop(Stop targetStop) {
        return !indexesOfStop(targetStop).isEmpty();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Route other)) {
            return false;
        }

        return number == other.number && name.equals(other.name) && type == other.type && segments.equals(other.segments) && interval.equals(other.interval);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, name, type, segments, interval);
    }
}
