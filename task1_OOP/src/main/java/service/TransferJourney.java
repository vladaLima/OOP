package service;

import model.Stop;
import model.Trip;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Рассчитанная поездка с одной пересадкой, выбранными рейсами и полным временем в пути.
 *
 * @param firstTrip первый рейс
 * @param secondTrip второй рейс
 * @param transferStop остановка пересадки
 * @param boardingIndex индекс посадки на первый рейс
 * @param firstTransferIndex индекс остановки пересадки на маршруте первого рейса
 * @param secondTransferIndex индекс остановки пересадки на маршруте второго рейса
 * @param destinationIndex индекс конечной остановки второго рейса
 * @param waitingTime продолжительность ожидания пересадки
 * @param totalDuration полная продолжительность поездки, включая ожидание
 * @param departureDateTime дата и время начала поездки с остановки посадки
 * @param arrivalDateTime дата и время прибытия в пункт назначения
 */
public record TransferJourney(Trip firstTrip, Trip secondTrip, Stop transferStop, int boardingIndex, int firstTransferIndex, int secondTransferIndex, int destinationIndex, Duration waitingTime, Duration totalDuration, LocalDateTime departureDateTime, LocalDateTime arrivalDateTime) {
    /**
     * Проверяет согласованность рейсов, остановок, индексов, длительностей и дат поездки.
     *
     * @throws IllegalArgumentException если данные поездки противоречивы или некорректны
     */
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