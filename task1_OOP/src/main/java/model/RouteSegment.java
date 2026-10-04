package model;

import java.time.Duration;

public record RouteSegment(Stop stop, Duration durationOfTrip) {}
