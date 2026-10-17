package service;

import model.Trip;

import java.time.LocalTime;

/**
 * Запись расписания: рейс, индекс остановки и время прибытия.
 *
 * @param trip рейс
 * @param stopIndex индекс остановки в маршруте рейса
 * @param arrivalTime время прибытия без календарной даты
 */
public record StopScheduleEntry(Trip trip, int stopIndex, LocalTime arrivalTime) {
    /**
     * Проверяет корректность рейса, индекса остановки и времени прибытия.
     *
     * @throws IllegalArgumentException если данные записи некорректны
     */
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
