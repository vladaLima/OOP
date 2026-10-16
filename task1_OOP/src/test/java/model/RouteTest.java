package model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RouteTest {
    private Route createRoute() {
        Stop station = new Stop(1, "Вокзал");
        Stop park = new Stop(2, "Парк");
        Stop school = new Stop(3, "Школа");

        return new Route(10,
                "Вокзал — Школа",
                TransportType.BUS,
                List.of(new RouteSegment(station, Duration.ofMinutes(10)),
                        new RouteSegment(park, Duration.ofMinutes(15)),
                        new RouteSegment(school, Duration.ZERO)),
                Duration.ofMinutes(12)
        );
    }

    @Test
    @DisplayName("Общая длительность маршрута равна сумме длительностей участков")
    void shouldCalculateTotalDuration() {
        Route route = createRoute();
        Duration actual = route.getTotalDuration();

        assertEquals(Duration.ofMinutes(25), actual);
    }

    @Test
    @DisplayName("Время до остановки правильно рассчитывается по её индексу")
    void shouldCalculateDurationToStop() {
        Route route = createRoute();

        assertEquals(Duration.ZERO, route.getDurationToStop(0));
        assertEquals(Duration.ofMinutes(10), route.getDurationToStop(1));
        assertEquals(Duration.ofMinutes(25), route.getDurationToStop(2));
    }

    @Test
    @DisplayName("Время между остановками равно длительности соответствующего участка")
    void shouldCalculateDurationBetweenStops() {
        Route route = createRoute();

        Duration actual = route.getDurationBetweenStops(1, 2);

        assertEquals(Duration.ofMinutes(15), actual);
    }

    @Test
    @DisplayName("Маршрут учитывает порядок остановок при проверке поездки")
    void shouldRespectStopOrder() {
        Route route = createRoute();

        Stop station = route.getSegments().get(0).stop();
        Stop school = route.getSegments().get(2).stop();

        assertTrue(route.canTravel(station, school));
        assertFalse(route.canTravel(school, station));
    }

    @Test
    @DisplayName("Поиск остановки возвращает все её вхождения в кольцевом маршруте")
    void shouldFindAllStopIndexes() {
        Stop station = new Stop(1, "Вокзал");
        Stop park = new Stop(2, "Парк");
        Stop school = new Stop(3, "Школа");

        Route route = new Route(
                20,
                "Кольцевой",
                TransportType.BUS,
                List.of(new RouteSegment(station, Duration.ofMinutes(10)),
                        new RouteSegment(park, Duration.ofMinutes(15)),
                        new RouteSegment(school, Duration.ofMinutes(12)),
                        new RouteSegment(station, Duration.ZERO)),
                Duration.ofMinutes(10)
        );

        List<Integer> actual = route.indexesOfStop(station);

        assertEquals(List.of(0, 3), actual);
    }

    @Test
    @DisplayName("Обращение к несуществующей остановке вызывает исключение")
    void shouldRejectInvalidStopIndex() {
        Route route = createRoute();

        assertThrows(IllegalArgumentException.class, () -> route.getDurationToStop(3));
    }

    @Test
    @DisplayName("Одинаковые маршруты равны и имеют одинаковый hashCode")
    void shouldBeEqualWhenRoutesHaveSameData() {
        Route first = createRoute();
        Route second = createRoute();

        assertEquals(first, second);
        assertEquals(first.hashCode(), second.hashCode());
    }

    @Test
    @DisplayName("Маршруты с разными интервалами движения не равны")
    void shouldNotBeEqualWhenIntervalsDiffer() {
        Route first = createRoute();

        Route second = new Route(
                first.getNumber(),
                first.getName(),
                first.getType(),
                first.getSegments(),
                Duration.ofMinutes(20)
        );

        assertNotEquals(first, second);
    }

    @Test
    @DisplayName("Создание маршрута с нулевым интервалом вызывает исключение")
    void shouldRejectZeroInterval() {
        Route route = createRoute();

        assertThrows(IllegalArgumentException.class,
                () -> new Route(
                        route.getNumber(),
                        route.getName(),
                        route.getType(),
                        route.getSegments(),
                        Duration.ZERO
                )
        );
    }
}
