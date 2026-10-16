package service;

import exception.RouteNotFoundException;
import exception.TransportException;
import model.Route;
import model.RouteSegment;
import model.Stop;
import model.TransportType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DirectPathFinderTest {
    private final Stop station = new Stop(1, "Вокзал");
    private final Stop park = new Stop(2, "Парк");
    private final Stop school = new Stop(3, "Школа");
    private final Stop hospital = new Stop(4, "Больница");

    private Route createRoute(int number, Stop first, Stop middle, Stop last) {
        return new Route(
                number,
                "Тестовый маршрут",
                TransportType.BUS,
                List.of(
                        new RouteSegment(first, Duration.ofMinutes(10)),
                        new RouteSegment(middle, Duration.ofMinutes(15)),
                        new RouteSegment(last, Duration.ZERO)
                ),
                Duration.ofMinutes(12)
        );
    }

    @Test
    @DisplayName("Поиск возвращает только маршруты, по которым можно доехать без пересадки")
    void shouldFindOnlyDirectRoutes() throws TransportException {
        Route route1 = createRoute(10, station, park, school);
        Route route2 = createRoute(20, station, hospital, school);
        Route reverseRoute = createRoute(30, school, park, station);
        DirectPathFinder finder = new DirectPathFinder(List.of(route1, route2, reverseRoute));

        List<Route> actual = finder.findRoutes(station, school);

        assertEquals(2, actual.size());
        assertTrue(actual.contains(route1));
        assertTrue(actual.contains(route2));
        assertFalse(actual.contains(reverseRoute));
    }

    @Test
    @DisplayName("Отсутствие прямого маршрута вызывает RouteNotFoundException")
    void shouldThrowWhenDirectRouteNotFound() {
        Route route = createRoute(10, station, park, school);
        DirectPathFinder finder = new DirectPathFinder(List.of(route));

        assertThrows(RouteNotFoundException.class, () -> finder.findRoutes(school, station));
    }

    @Test
    @DisplayName("Поиск маршрута без начальной остановки вызывает исключение")
    void shouldRejectNullStartStop() {
        Route route = createRoute(10, station, park, school);
        DirectPathFinder finder = new DirectPathFinder(List.of(route));

        assertThrows(IllegalArgumentException.class, () -> finder.findRoutes(null, school));
    }
}
