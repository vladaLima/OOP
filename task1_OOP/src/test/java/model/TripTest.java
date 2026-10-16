package model;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TripTest {
    private Route createRoute() {
        Stop station = new Stop(1, "Вокзал");
        Stop park = new Stop(2, "Парк");
        Stop school = new Stop(3, "Школа");

        return new Route(
                10,
                "Вокзал — Школа",
                TransportType.BUS,
                List.of(new RouteSegment(station, Duration.ofMinutes(10)),
                        new RouteSegment(park, Duration.ofMinutes(15)),
                        new RouteSegment(school, Duration.ZERO)),
                Duration.ofMinutes(12)
        );
    }

    @Test
    @DisplayName("Время прибытия рассчитывается по времени отправления и длительности пути")
    void shouldCalculateArrivalTime() {
        Route route = createRoute();
        Trip trip = new Trip(route, LocalTime.of(8, 0), DayType.WEEKEND);

        assertEquals(LocalTime.of(8, 0), trip.getArrivalTimeAt(0));
        assertEquals(LocalTime.of(8, 10), trip.getArrivalTimeAt(1));
        assertEquals(LocalTime.of(8, 25), trip.getArrivalTimeAt(2));
    }

    @Test
    @DisplayName("Прибытие после полуночи правильно изменяет календарную дату")
    void shouldCalculateArrivalDateTimeAcrossMidnight() {
        Route route = createRoute();
        Trip trip = new Trip(route, LocalTime.of(23, 50), DayType.WEEKDAY);
        LocalDate departureDate = LocalDate.of(2026, 10, 15);
        LocalDateTime actual = trip.getArrivalDateTimeAt(departureDate, 2);
        LocalDateTime expected = LocalDateTime.of(2026, 10, 16, 0, 15);

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Отсутствие даты отправления вызывает исключение")
    void shouldRejectNullDepartureDate() {
        Route route = createRoute();
        Trip trip = new Trip(route, LocalTime.of(8, 0), DayType.WEEKDAY);

        assertThrows(IllegalArgumentException.class, () -> trip.getArrivalDateTimeAt(null, 2));
    }

    @Test
    @DisplayName("Создание рейса без маршрута вызывает исключение")
    void shouldRejectNullRoute() {
        assertThrows(IllegalArgumentException.class, () -> new Trip(null, LocalTime.of(8, 0), DayType.WEEKDAY));
    }
}
