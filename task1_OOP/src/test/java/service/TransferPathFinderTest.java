package service;

import exception.RouteNotFoundException;
import exception.TransportException;
import model.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TransferPathFinderTest {
    private final Stop station = new Stop(1, "Вокзал");
    private final Stop park = new Stop(2, "Парк");
    private final Stop school = new Stop(3, "Школа");
    private final Stop museum = new Stop(4, "Музей");
    private final Stop circus = new Stop(5, "Цирк");

    private Route createRoute(int number, Stop stop1, Stop stop2, Stop stop3, int durationOfTrip1, int durationOfTrip2) {
        return new Route(
                number,
                "Тестовый маршрут",
                TransportType.BUS,
                List.of(new RouteSegment(stop1, Duration.ofMinutes(durationOfTrip1)),
                        new RouteSegment(stop2, Duration.ofMinutes(durationOfTrip2)),
                        new RouteSegment(stop3, Duration.ZERO)
                ),
                Duration.ofMinutes(10)
        );
    }

    @Test
    @DisplayName("Поиск пересадки находит общую остановку двух маршрутов между разными комбинациями маршрутов")
    void shouldFindTransferOption() throws TransportException {
        Route route1 = createRoute(10, station, park, museum, 10, 5);
        Route route2 = createRoute(20, park, circus, school, 15, 5);
        Route route3 = createRoute(30, school, park, museum, 5, 11);
        Route route4 = createRoute(40, museum, school, park, 17, 12);
        Route route5 = createRoute(40, station, circus, museum, 20, 14);
        TransferPathFinder finder = new TransferPathFinder(List.of(), List.of(route1, route2, route3, route4, route5));
        List<TransferRouteOption> options = finder.findTransferOptions(station, school);

        assertEquals(4, options.size());

        TransferRouteOption option = options.get(0);

        assertEquals(route1, option.firstRoute());
        assertEquals(route2, option.secondRoute());
        assertEquals(park, option.transferStop());

        TransferRouteOption option2 = options.get(1);

        assertEquals(route1, option2.firstRoute());
        assertEquals(route4, option2.secondRoute());
        assertEquals(museum, option2.transferStop());

        TransferRouteOption option3 = options.get(2);

        assertEquals(route5, option3.firstRoute());
        assertEquals(route2, option3.secondRoute());
        assertEquals(circus, option3.transferStop());

        TransferRouteOption option4 = options.get(3);

        assertEquals(route5, option4.firstRoute());
        assertEquals(route4, option4.secondRoute());
        assertEquals(museum, option4.transferStop());
    }

    @Test
    @DisplayName("Пересадка ровно за минимальное время допустима, более ранний рейс не подходит, т.к. мы не успеем на него сесть и выберем тот, который придет в ЭТОТ день")
    void shouldRespectMinimumTransferTimeAndThereIsSuitableTripTheDay() throws TransportException {
        Route firstRoute = createRoute(10, station, park, museum, 10, 5);
        Route secondRoute = createRoute(20, park, circus, school, 15, 10);
        Trip firstTrip = new Trip(firstRoute, LocalTime.of(8, 0), DayType.WEEKDAY);
        Trip tooEarly = new Trip(secondRoute, LocalTime.of(8, 14), DayType.WEEKDAY);
        Trip suitable = new Trip(secondRoute, LocalTime.of(8, 15), DayType.WEEKDAY);
        TransferPathFinder finder = new TransferPathFinder(List.of(firstTrip, tooEarly, suitable), List.of(firstRoute, secondRoute));
        TransferJourney journey = finder.findBestJourney(station, school, LocalDate.of(2026, 10, 16), Duration.ofMinutes(5));

        assertEquals(suitable, journey.secondTrip());
        assertEquals(Duration.ofMinutes(5), journey.waitingTime());
        assertEquals(Duration.ofMinutes(40), journey.totalDuration());

        assertEquals(LocalDateTime.of(2026, 10, 16, 8, 40), journey.arrivalDateTime());
    }

    @Test
    @DisplayName("Пересадка, когда нет в ЭТОТ день рейса, который придет через минимальное время посадки." +
            "Автобус придет слишком рано, человек в ЭТОТ день не успеет сесть, но он дождется этот же ранний автобус на следующий подходящий тип дня и сядет." +
            "У человека нет других вариантов")
    void shouldRespectMinimumTransferTimeButThereIsNoTSuitableTripTheDay() throws TransportException {
        Route firstRoute = createRoute(10, station, park, museum, 10, 5);
        Route secondRoute = createRoute(20, park, circus, school, 15, 10);
        Trip firstTrip = new Trip(firstRoute, LocalTime.of(8, 0), DayType.WEEKDAY);
        Trip tooEarly = new Trip(secondRoute, LocalTime.of(8, 14), DayType.WEEKDAY);
        TransferPathFinder finder = new TransferPathFinder(List.of(firstTrip, tooEarly), List.of(firstRoute, secondRoute));
        TransferJourney journey = finder.findBestJourney(station, school, LocalDate.of(2026, 10, 16), Duration.ofMinutes(5));

        assertEquals(tooEarly, journey.secondTrip());
        assertEquals(Duration.ofHours(72).plusMinutes(4), journey.waitingTime());
        assertEquals(Duration.ofHours(72).plusMinutes(39), journey.totalDuration());

        assertEquals(LocalDateTime.of(2026, 10, 19, 8, 39), journey.arrivalDateTime());
    }

    @Test
    @DisplayName("Пересадка через полночь учитывает дату и тип дня каждого рейса" +
            "Остановка, на которую человек садится НЕ первая, departureTime в Trip - время отправления с остановки с индексом 0 на маршруте рейса" +
            "journey.departureDateTime() - время отправки рейса с остановки посадки" +
            "Минимального времени на пересадку не хватило")
    void shouldHandleTransferAcrossMidnightLackOfRespectMinTransferTime() throws TransportException {
        Route firstRoute = createRoute(10, museum, station, park, 10, 5);
        Route secondRoute = createRoute(20, park, circus, school, 15, 10);
        Trip trip1 = new Trip(firstRoute, LocalTime.of(23, 50), DayType.WEEKDAY);
        Trip trip2 = new Trip(secondRoute, LocalTime.of(0, 5), DayType.WEEKEND);
        TransferPathFinder finder = new TransferPathFinder(List.of(trip1, trip2), List.of(firstRoute, secondRoute));
        TransferJourney journey = finder.findBestJourney(station, school, LocalDate.of(2026, 10, 16), Duration.ofMinutes(5));

        assertEquals(LocalDateTime.of(2026, 10, 16, 0, 0), journey.departureDateTime());
        assertEquals(LocalDateTime.of(2026, 10, 17, 0, 30), journey.arrivalDateTime());

        assertEquals(Duration.ofHours(24), journey.waitingTime());
        assertEquals(Duration.ofHours(24).plusMinutes(30), journey.totalDuration());
        assertEquals(trip2, journey.secondTrip());
    }

    @Test
    @DisplayName("Пересадка через полночь учитывает дату и тип дня каждого рейса" +
            "Остановка, на которую человек садится НЕ первая, departureTime в Trip - время отправления с остановки с индексом 0 на маршруте рейса" +
            "journey.departureDateTime() - время отправки рейса с остановки посадки" +
            "Минимального времени на пересадку хватило, НО trip2 - WEEKEND, поэтому даже если времени хватило, человек не сможет в пятницу сесть на субботний рейс" +
            "Человек ждет до субботы, чтобы сесть")
    void shouldHandleTransferAcrossMidnightAndRespectMinTransferTime() throws TransportException {
        Route firstRoute = createRoute(10, museum, station, park, 10, 5);
        Route secondRoute = createRoute(20, park, circus, school, 15, 10);
        Trip trip1 = new Trip(firstRoute, LocalTime.of(23, 50), DayType.WEEKDAY);
        Trip trip2 = new Trip(secondRoute, LocalTime.of(0, 10), DayType.WEEKEND);
        TransferPathFinder finder = new TransferPathFinder(List.of(trip1, trip2), List.of(firstRoute, secondRoute));
        TransferJourney journey = finder.findBestJourney(station, school, LocalDate.of(2026, 10, 16), Duration.ofMinutes(5));

        assertEquals(LocalDateTime.of(2026, 10, 16, 0, 0), journey.departureDateTime());
        assertEquals(LocalDateTime.of(2026, 10, 17, 0, 35), journey.arrivalDateTime());

        assertEquals(Duration.ofHours(24).plusMinutes(5), journey.waitingTime());
        assertEquals(Duration.ofHours(24).plusMinutes(35), journey.totalDuration());
        assertEquals(trip2, journey.secondTrip());
    }

    @Test
    @DisplayName("Пересадка через полночь учитывает дату и тип дня каждого рейса" +
            "Остановка, на которую человек садится НЕ первая, departureTime в Trip - время отправления с остановки с индексом 0 на маршруте рейса" +
            "journey.departureDateTime() - время отправки рейса с остановки посадки" +
            "Минимального времени на пересадку хватило" +
            "Стандартная ситуация")
    void shouldHandleTransferAcrossMidnight() throws TransportException {
        Route firstRoute = createRoute(10, museum, station, park, 10, 5);
        Route secondRoute = createRoute(20, park, circus, school, 15, 10);
        Trip trip1 = new Trip(firstRoute, LocalTime.of(23, 40), DayType.WEEKDAY);
        Trip trip2 = new Trip(secondRoute, LocalTime.of(0, 5), DayType.WEEKEND);
        TransferPathFinder finder = new TransferPathFinder(List.of(trip1, trip2), List.of(firstRoute, secondRoute));
        TransferJourney journey = finder.findBestJourney(station, school, LocalDate.of(2026, 10, 16), Duration.ofMinutes(5));

        assertEquals(LocalDateTime.of(2026, 10, 16, 23, 50), journey.departureDateTime());
        assertEquals(LocalDateTime.of(2026, 10, 17, 0, 30), journey.arrivalDateTime());

        assertEquals(Duration.ofMinutes(10), journey.waitingTime());
        assertEquals(Duration.ofMinutes(40), journey.totalDuration());

        assertEquals(trip2, journey.secondTrip());
    }

    @Test
    @DisplayName("Лучшая поездка выбирается по минимальной общей длительности")
    void shouldChooseJourneyWithShortestTotalDuration() throws TransportException {
        Route firstRoute = createRoute(10, station, park, museum, 10, 5);
        Route secondRoute = createRoute(20, park, school, circus, 15, 10);
        Trip earlyFirstTrip = new Trip(firstRoute, LocalTime.of(8, 0), DayType.WEEKDAY);
        Trip laterFirstTrip = new Trip(firstRoute, LocalTime.of(8, 20), DayType.WEEKDAY);
        Trip earlySecondTrip = new Trip(secondRoute, LocalTime.of(8, 30), DayType.WEEKDAY);
        Trip laterSecondTrip = new Trip(secondRoute, LocalTime.of(8, 35), DayType.WEEKDAY);
        TransferPathFinder finder = new TransferPathFinder(List.of(earlyFirstTrip, laterFirstTrip, earlySecondTrip, laterSecondTrip), List.of(firstRoute, secondRoute));
        TransferJourney best = finder.findBestJourney(station, school, LocalDate.of(2026, 10, 16), Duration.ofMinutes(5));

        assertEquals(laterFirstTrip, best.firstTrip());
        assertEquals(laterSecondTrip, best.secondTrip());

        assertEquals(Duration.ofMinutes(5), best.waitingTime());
        assertEquals(Duration.ofMinutes(30), best.totalDuration());

        assertEquals(LocalDateTime.of(2026, 10, 16, 8, 20), best.departureDateTime());
        assertEquals(LocalDateTime.of(2026, 10, 16, 8, 50), best.arrivalDateTime());
    }

    @Test
    @DisplayName("findRoutes возвращает уникальные маршруты найденных пересадок")
    void shouldReturnUniqueRoutesForTransfers() throws TransportException {
        Route route1 = createRoute(10, station, park, museum, 10, 5);
        Route route2 = createRoute(20, park, circus, school, 15, 5);
        Route route3 = createRoute(40, museum, school, park, 17, 12);
        TransferPathFinder finder = new TransferPathFinder(List.of(), List.of(route1, route2, route3));

        List<Route> actual = finder.findRoutes(station, school);

        assertEquals(3, actual.size());

        assertTrue(actual.contains(route1));
        assertTrue(actual.contains(route2));
        assertTrue(actual.contains(route3));
    }

    @Test
    @DisplayName("Отрицательное минимальное время пересадки вызывает исключение")
    void shouldRejectNegativeMinimumTransferTime() {
        Route firstRoute = createRoute(10, station, park, museum, 10, 5);
        Route secondRoute = createRoute(20, park, school, circus, 15, 10);
        TransferPathFinder finder = new TransferPathFinder(List.of(), List.of(firstRoute, secondRoute));

        assertThrows(IllegalArgumentException.class, () -> finder.findBestJourney(station, school, LocalDate.of(2026, 10, 16), Duration.ofMinutes(-5)));
    }

    @Test
    @DisplayName("Если маршрут с одной пересадкой не найден, выбрасывается RouteNotFoundException")
    void shouldThrowWhenTransferRouteNotFound() {
        Route firstRoute = createRoute(10, station, park, museum, 10, 5);
        Route secondRoute = createRoute(20, park, circus, school, 15, 10);
        TransferPathFinder finder = new TransferPathFinder(List.of(), List.of(firstRoute, secondRoute));

        assertThrows(RouteNotFoundException.class, () -> finder.findRoutes(school, station));
    }

    @Test
    @DisplayName("Если маршруты существуют, но подходящих рейсов нет, выбрасывается RouteNotFoundException")
    void shouldThrowWhenSuitableJourneyNotFound() {
        Route firstRoute = createRoute(10, station, park, museum, 10, 5);
        Route secondRoute = createRoute(20, park, circus, school, 15, 10);
        TransferPathFinder finder = new TransferPathFinder(List.of(), List.of(firstRoute, secondRoute));

        assertThrows(RouteNotFoundException.class, () -> finder.findBestJourney(station, school, LocalDate.of(2026, 10, 16), Duration.ofMinutes(5)));
    }

    @Test
    @DisplayName("Если рейсы не относятся к нужным маршрутам, выбрасывается RouteNotFoundException" +
            "Проверяем, что маршруты с пересадкой действительно существуют")
    void shouldThrowWhenTripsDoNotMatchRoutes() throws TransportException {
        Route firstRoute = createRoute(10, station, park, museum, 10, 5);
        Route secondRoute = createRoute(20, park, circus, school, 15, 10);
        Route otherRoute = createRoute(30, museum, circus, school, 12, 8);
        Trip otherTrip1 = new Trip(otherRoute, LocalTime.of(8, 0), DayType.WEEKDAY);
        Trip otherTrip2 = new Trip(otherRoute, LocalTime.of(9, 0), DayType.WEEKDAY);
        TransferPathFinder finder = new TransferPathFinder(List.of(otherTrip1, otherTrip2), List.of(firstRoute, secondRoute));

        assertFalse(finder.findTransferOptions(station, school).isEmpty());

        assertThrows(RouteNotFoundException.class, () -> finder.findBestJourney(station, school, LocalDate.of(2026, 10, 16), Duration.ofMinutes(5)));
    }

    @Test
    @DisplayName("Изменение исходного списка маршрутов не влияет на TransferPathFinder")
    void shouldProtectRoutesFromExternalChanges() throws TransportException {
        Route firstRoute = createRoute(10, station, park, museum, 10, 5);
        Route secondRoute = createRoute(20, park, circus, school, 15, 10);
        List<Route> routes = new ArrayList<>();
        routes.add(firstRoute);
        routes.add(secondRoute);
        TransferPathFinder finder = new TransferPathFinder(List.of(), routes);

        routes.clear();

        List<TransferRouteOption> options = finder.findTransferOptions(station, school);

        assertEquals(1, options.size());

        assertEquals(firstRoute, options.get(0).firstRoute());
        assertEquals(secondRoute, options.get(0).secondRoute());
        assertEquals(park, options.get(0).transferStop());
    }

    @Test
    @DisplayName("Изменение исходного списка рейсов не влияет на TransferPathFinder")
    void shouldProtectTripsFromExternalChanges() throws TransportException {
        Route firstRoute = createRoute(10, station, park, museum, 10, 5);
        Route secondRoute = createRoute(20, park, circus, school, 15, 10);
        Trip firstTrip = new Trip(firstRoute, LocalTime.of(8, 0), DayType.WEEKDAY);
        Trip secondTrip = new Trip(secondRoute, LocalTime.of(8, 15), DayType.WEEKDAY);
        List<Trip> trips = new ArrayList<>();
        trips.add(firstTrip);
        trips.add(secondTrip);
        TransferPathFinder finder = new TransferPathFinder(trips, List.of(firstRoute, secondRoute));

        trips.clear();

        TransferJourney journey = finder.findBestJourney(station, school, LocalDate.of(2026, 10, 16), Duration.ofMinutes(5));

        assertEquals(firstTrip, journey.firstTrip());
        assertEquals(secondTrip, journey.secondTrip());

        assertEquals(Duration.ofMinutes(5), journey.waitingTime());
        assertEquals(Duration.ofMinutes(40), journey.totalDuration());

        assertEquals(LocalDateTime.of(2026, 10, 16, 8, 0), journey.departureDateTime());
        assertEquals(LocalDateTime.of(2026, 10, 16, 8, 40), journey.arrivalDateTime());
    }
}
