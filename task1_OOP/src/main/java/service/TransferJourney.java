package service;

import model.Stop;
import model.Trip;

import java.time.Duration;
import java.time.LocalDateTime;

public record TransferJourney(Trip firstTrip, Trip secondTrip, Stop transferStop, int boardingIndex, int firstTransferIndex, int secondTransferIndex, int destinationIndex, Duration waitingTime, Duration totalDuration, LocalDateTime departureDateTime, LocalDateTime arrivalDateTime) {
    public TransferJourney {
        if (firstTrip == null || secondTrip == null) {
            throw new IllegalArgumentException("Trips cannot be null");
        }
        if (transferStop == null) {
            throw new IllegalArgumentException("Stop cannot be null");
        }
        if (waitingTime == null || totalDuration == null) {
            throw new IllegalArgumentException("Durations cannot be null");
        }
        if (waitingTime.isNegative() || totalDuration.isNegative()) {
            throw new IllegalArgumentException("Durations cannot be negative");
        }
        if (boardingIndex < 0 || firstTransferIndex <= boardingIndex || secondTransferIndex < 0 || destinationIndex <= secondTransferIndex) {
            throw new IllegalArgumentException("Invalid stop indexes");
        }
        if (departureDateTime == null || arrivalDateTime == null) {
            throw new IllegalArgumentException("Departure and arrival date-times cannot be null");
        }
        if (arrivalDateTime.isBefore(departureDateTime)) {
            throw new IllegalArgumentException("Arrival date-time cannot be before departure date-time");
        }
        if (firstTransferIndex >= firstTrip.getRoute().getSegments().size() || destinationIndex >= secondTrip.getRoute().getSegments().size()) {
            throw new IllegalArgumentException("Stop indexes out of bounds");
        }
        if (!firstTrip.getRoute().getSegments().get(firstTransferIndex).stop().equals(transferStop) || !secondTrip.getRoute().getSegments().get(secondTransferIndex).stop().equals(transferStop)) {
            throw new IllegalArgumentException("Transfer stop order does not match route stops");
        }

        Duration calculatedDuration = Duration.between(departureDateTime, arrivalDateTime);

        if (!totalDuration.equals(calculatedDuration)) {
            throw new IllegalArgumentException("Total duration does not match departure and arrival times");
        }
    }
}