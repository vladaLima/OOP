package service;

import model.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TripGeneratorTest {
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
    @DisplayName("Рейсы генерируются с заданным интервалом движения")
    void shouldGenerateTripsAtRouteInterval() {
        Route route = createRoute();
        TripGenerator generator = new TripGenerator();
        List<Trip> trips = generator.generateTrips(route, LocalTime.of(8, 0), LocalTime.of(8, 35), DayType.WEEKDAY);

        assertEquals(3, trips.size());
        assertEquals(LocalTime.of(8, 0), trips.get(0).getDepartureTime());
        assertEquals(LocalTime.of(8, 12), trips.get(1).getDepartureTime());
        assertEquals(LocalTime.of(8, 24), trips.get(2).getDepartureTime());
    }

    @Test
    @DisplayName("При одинаковом начале и конце диапазона создаётся один рейс + этот рейс отправляется ровно в startTime = endTime")
    void shouldGenerateSingleTripWhenTimesAreEqual() {
        Route route = createRoute();
        TripGenerator generator = new TripGenerator();
        List<Trip> trips = generator.generateTrips(route, LocalTime.of(8, 0), LocalTime.of(8, 0), DayType.WEEKDAY);

        assertEquals(1, trips.size());
        assertEquals(LocalTime.of(8, 0), trips.get(0).getDepartureTime());
    }

    @Test
    @DisplayName("Рейс создаётся при отправлении точно в endTime")
    void shouldIncludeTripExactlyAtEndTime() {
        Route route = createRoute();
        TripGenerator generator = new TripGenerator();
        List<Trip> trips = generator.generateTrips(route, LocalTime.of(8, 0), LocalTime.of(8, 24), DayType.WEEKDAY);

        assertEquals(3, trips.size());
        assertEquals(LocalTime.of(8, 24), trips.get(2).getDepartureTime());
    }

    @Test
    @DisplayName("Конец диапазона раньше начала вызывает исключение")
    void shouldRejectInvalidTimeRange() {
        Route route = createRoute();
        TripGenerator generator = new TripGenerator();

        assertThrows(IllegalArgumentException.class,
                () -> generator.generateTrips(
                        route,
                        LocalTime.of(18, 0),
                        LocalTime.of(8, 0),
                        DayType.WEEKDAY
                )
        );
    }


}
