package service;

import model.Trip;

import java.time.LocalDateTime;

/**
 * Запись расписания с датами и временем отправления и прибытия.
 *
 * @param trip рейс
 * @param stopIndex индекс остановки в маршруте
 * @param departureDateTime дата и время отправления рейса с первой остановки
 * @param arrivalDateTime дата и время прибытия на указанную остановку
 */
public record StopScheduleDateEntry(Trip trip, int stopIndex, LocalDateTime departureDateTime, LocalDateTime arrivalDateTime) {
    /**
     * Проверяет рейс, индекс и хронологический порядок времени отправления и прибытия.
     *
     * @throws IllegalArgumentException если рейс или время некорректны либо прибытие раньше отправления
     * @throws IndexOutOfBoundsException если индекс остановки вне границ маршрута
     */
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
