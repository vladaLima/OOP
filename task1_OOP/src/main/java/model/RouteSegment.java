package model;

import java.time.Duration;

/**
 * Остановка маршрута и время движения от неё до следующей остановки.
 *
 * @param stop остановка данного участка
 * @param durationOfTrip длительность движения до следующей остановки; для последней остановки — ноль
 */
public record RouteSegment(Stop stop, Duration durationOfTrip) {
    /**
     * Проверяет наличие остановки и неотрицательную длительность участка.
     *
     * @throws IllegalArgumentException если остановка отсутствует или длительность некорректна
     */
    public RouteSegment {
        if (stop == null) {
            throw new IllegalArgumentException("Stop cannot be null");
        }
        if (durationOfTrip == null || durationOfTrip.isNegative()) {
            throw new IllegalArgumentException("Duration cannot be negative");
        }
    }
}
