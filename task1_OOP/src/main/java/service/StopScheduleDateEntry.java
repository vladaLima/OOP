package service;

import model.Trip;

import java.time.LocalDateTime;

public record StopScheduleDateEntry(Trip trip, int stopIndex, LocalDateTime departureDateTime, LocalDateTime arrivalDateTime) {
    public StopScheduleDateEntry {
        if (trip == null) {
            throw new IllegalArgumentException("Trip cannot be null");
        }
        if (stopIndex < 0 || stopIndex >= trip.getRoute().getSegments().size()) {
            throw new IndexOutOfBoundsException("Stop index out of bounds");
        }
        if (departureDateTime == null || arrivalDateTime == null) {
            throw new IllegalArgumentException("Departure and arrival date-times cannot be null");
        }
        if (arrivalDateTime.isBefore(departureDateTime)) {
            throw new IllegalArgumentException("Arrival date-time cannot be before departure date-time");
        }
    }
}
