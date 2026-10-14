package service;

import model.Trip;

import java.time.LocalTime;

public record StopScheduleEntry(Trip trip, int stopIndex, LocalTime arrivalTime) {
    public StopScheduleEntry {
        if (trip == null) {
            throw new IllegalArgumentException("Trip cannot be null");
        }
        if (stopIndex < 0 || stopIndex >= trip.getRoute().getSegments().size()) {
            throw new IllegalArgumentException("Stop index out of bounds");
        }
        if (arrivalTime == null) {
            throw new IllegalArgumentException("Arrival time cannot be null");
        }
    }
}
