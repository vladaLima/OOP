package model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Рейс конкретного маршрута с временем отправления и типом дня.
 */
public class Trip {
    private final Route route;
    private final LocalTime departureTime;
    private final DayType dayType;

    /**
     * Создаёт рейс с заданными маршрутом, временем отправления и типом дня.
     *
     * @param route маршрут рейса
     * @param departureTime время отправления с первой остановки
     * @param dayType тип дня, в который выполняется рейс
     * @throws IllegalArgumentException если какой-либо аргумент равен null
     */
    public Trip(Route route, LocalTime departureTime, DayType dayType) {
        if (route == null) {
            throw new IllegalArgumentException("Route cannot be null");
        }
        if (departureTime == null) {
            throw new IllegalArgumentException("DepartureTime cannot be null");
        }
        if (dayType == null) {
            throw new IllegalArgumentException("Day cannot be null");
        }

        this.route = route;
        this.departureTime = departureTime;
        this.dayType = dayType;
    }

    /**
     * Возвращает маршрут рейса.
     *
     * @return маршрут
     */
    public Route getRoute() {
        return route;
    }

    /**
     * Возвращает время отправления с первой остановки маршрута.
     *
     * @return время отправления
     */
    public LocalTime getDepartureTime() {
        return departureTime;
    }

    /**
     * Возвращает тип дня, в который выполняется рейс.
     *
     * @return тип дня
     */
    public DayType getDayType() {
        return dayType;
    }

    /**
     * Вычисляет время прибытия на остановку без учёта календарной даты.
     *
     * @param stopIndex индекс остановки в маршруте
     * @return местное время прибытия; при переходе через полночь дата не сохраняется
     * @throws IllegalArgumentException если индекс остановки некорректен
     */
    public LocalTime getArrivalTimeAt(int stopIndex) {
        var travelTime = route.getDurationToStop(stopIndex);
        return departureTime.plus(travelTime);
    }

    /**
     * Вычисляет дату и время прибытия на остановку с учётом перехода через полночь.
     *
     * @param departureDate календарная дата отправления с первой остановки
     * @param stopIndex индекс остановки в маршруте
     * @return дата и время прибытия
     * @throws IllegalArgumentException если дата равна null или индекс некорректен
     */
    public LocalDateTime getArrivalDateTimeAt(LocalDate departureDate, int stopIndex) {
        if (departureDate == null) {
            throw new IllegalArgumentException("Departure date cannot be null");
        }

        var departureDateTime = LocalDateTime.of(departureDate, departureTime);
        return departureDateTime.plus(route.getDurationToStop(stopIndex));
    }
}
