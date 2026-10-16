package model;

import exception.RouteNotFoundException;
import exception.ScheduleNotFoundException;
import exception.TransportException;
import service.*;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("ГОРОДСКОЙ ТРАНСПОРТ");
        System.out.println();

        Stop station = new Stop(1, "Вокзал");
        Stop park = new Stop(2, "Парк");
        Stop museum = new Stop(3, "Музей");
        Stop school = new Stop(4, "Школа");
        Stop hospital = new Stop(5, "Больница");
        Stop university = new Stop(6, "Университет");
        Stop square = new Stop(7, "Площадь");
        Stop stadium = new Stop(8, "Стадион");
        Stop library = new Stop(9, "Библиотека");
        Stop airport = new Stop(10, "Аэропорт");
        Stop riverStation = new Stop(11, "Речной вокзал");
        Stop island = new Stop(12, "Остров");
        Stop embankment = new Stop(13, "Набережная");

         List<Stop> stops = List.of(station, park, museum, school, hospital, university, square, stadium, library, airport, riverStation, island, embankment);

         System.out.println("ОСТАНОВКИ\n");

         for (Stop stop : stops) {
             System.out.println(stop.id() + ". " + stop.name());
         }

         System.out.println();
         System.out.println("ВСЕГО ОСТАНОВОК: " + stops.size());
         System.out.println();

        Route route10 = new Route(
                10,
                "Вокзал - Музей",
                TransportType.BUS,
                List.of(
                        new RouteSegment(station, Duration.ofMinutes(12)),
                        new RouteSegment(park, Duration.ofMinutes(15)),
                        new RouteSegment(museum, Duration.ZERO)
                ),
                Duration.ofMinutes(20)
        );

        Route route20 = new Route(
                20,
                "Парк - Больница",
                TransportType.TROLLEYBUS,
                List.of(
                        new RouteSegment(park, Duration.ofMinutes(16)),
                        new RouteSegment(school, Duration.ofMinutes(20)),
                        new RouteSegment(hospital, Duration.ZERO)
                ),
                Duration.ofMinutes(15)
        );

        Route route30 = new Route(
                30,
                "Вокзал - Университет",
                TransportType.TRAM,
                List.of(
                        new RouteSegment(station, Duration.ofMinutes(10)),
                        new RouteSegment(square, Duration.ofMinutes(23)),
                        new RouteSegment(university, Duration.ZERO)
                ),
                Duration.ofMinutes(12)
        );

        Route route40 = new Route(
                40,
                "Музей - Библиотека",
                TransportType.ELECTROBUS,
                List.of(
                        new RouteSegment(museum, Duration.ofMinutes(14)),
                        new RouteSegment(stadium, Duration.ofMinutes(12)),
                        new RouteSegment(library, Duration.ZERO)
                ),
                Duration.ofMinutes(15)
        );

        Route route50 = new Route(
                50,
                "Вокзал - Аэропорт",
                TransportType.TAXI,
                List.of(
                        new RouteSegment(station, Duration.ofMinutes(15)),
                        new RouteSegment(hospital, Duration.ofMinutes(15)),
                        new RouteSegment(airport, Duration.ZERO)
                ),
                Duration.ofMinutes(10)
        );

        Route route60 = new Route(
                60,
                "Площадь - Аэропорт",
                TransportType.METRO,
                List.of(
                        new RouteSegment(square, Duration.ofMinutes(8)),
                        new RouteSegment(university, Duration.ofMinutes(8)),
                        new RouteSegment(airport, Duration.ZERO)
                ),
                Duration.ofMinutes(6)
        );

        Route route70 = new Route(
                70,
                "Речной вокзал - Набережная",
                TransportType.FERRY,
                List.of(
                        new RouteSegment(riverStation, Duration.ofMinutes(15)),
                        new RouteSegment(island, Duration.ofMinutes(15)),
                        new RouteSegment(embankment, Duration.ZERO)
                ),
                Duration.ofMinutes(30)
        );

        Route route80 = new Route(
                80,
                "Вокзал - Аэропорт",
                TransportType.ELECTROBUS,
                List.of(
                        new RouteSegment(station, Duration.ofMinutes(14)),
                        new RouteSegment(square, Duration.ofMinutes(16)),
                        new RouteSegment(airport, Duration.ZERO)
                ),
                Duration.ofMinutes(20)
        );

        List<Route> routes = List.of(route10, route20, route30, route40, route50, route60, route70, route80);

        System.out.println("МАРШРУТЫ");
        System.out.println();

        for (Route route : routes) {
            TransportType type = route.getType();
            System.out.println(route.getFullName() +
                    "\nСкорость: " + type.getAvgSpeedKmh() + " км/ч\n" +
                    "Вместимость: " + type.getCapacity() + " чел.\n" +
                    "Время в пути: " + route.getTotalDuration().toMinutes() + " мин\n" +
                    "Интервал: " + route.getInterval().toMinutes() + " мин\n");
        }

        System.out.println("ВСЕГО МАРШРУТОВ: " + routes.size());
        System.out.println();

        TripGenerator generator = new TripGenerator();

        List<Trip> trips = new ArrayList<>();

        // Рейсы будних дней
        trips.addAll(generator.generateTrips(route10, LocalTime.of(8, 0), LocalTime.of(8, 20), DayType.WEEKDAY));
        trips.addAll(generator.generateTrips(route20, LocalTime.of(8, 15), LocalTime.of(8, 15), DayType.WEEKDAY));
        trips.addAll(generator.generateTrips(route30, LocalTime.of(8, 0), LocalTime.of(8, 12), DayType.WEEKDAY));
        trips.addAll(generator.generateTrips(route40, LocalTime.of(8, 30), LocalTime.of(8, 30), DayType.WEEKDAY));
        trips.addAll(generator.generateTrips(route50, LocalTime.of(8, 10), LocalTime.of(8, 10), DayType.WEEKDAY));
        trips.addAll(generator.generateTrips(route60, LocalTime.of(8, 15), LocalTime.of(8, 21), DayType.WEEKDAY));
        trips.addAll(generator.generateTrips(route70, LocalTime.of(8, 0), LocalTime.of(8, 0), DayType.WEEKDAY));

        // Рейсы выходных дней
        trips.addAll(generator.generateTrips(route10, LocalTime.of(9, 0), LocalTime.of(9, 20), DayType.WEEKEND));
        trips.addAll(generator.generateTrips(route30, LocalTime.of(9, 0), LocalTime.of(9, 0), DayType.WEEKEND));
        trips.addAll(generator.generateTrips(route60, LocalTime.of(9, 0), LocalTime.of(9, 0), DayType.WEEKEND));

        System.out.println("\nСОЗДАННЫЕ РЕЙСЫ");
        System.out.println();

        for (Trip trip : trips) {
            System.out.println(trip.getRoute().getFullName() +
                    "\nОтправление: " + trip.getDepartureTime() +
                    "\nТип дня: " + trip.getDayType() + "\n");
        }

        long weekdayCount = trips.stream().filter(trip -> trip.getDayType() == DayType.WEEKDAY).count();
        long weekendCount = trips.stream().filter(trip -> trip.getDayType() == DayType.WEEKEND).count();

        System.out.println("РЕЙСОВ В БУДНИЕ ДНИ: " + weekdayCount);
        System.out.println("РЕЙСОВ В ВЫХОДНЫЕ ДНИ: " + weekendCount);

        System.out.println("ВСЕГО РЕЙСОВ: " + trips.size());

        // Расписание по остановке по типу дня
        System.out.println("\nРАСПИСАНИЕ ОСТАНОВКИ: " + park.name());
        System.out.println("БУДНИЕ ДНИ\n");

        StopSchedule parkSchedule = new StopSchedule(park, trips);

        try {
            List<StopScheduleEntry> entries = parkSchedule.getScheduleEntries(DayType.WEEKDAY);

            for (StopScheduleEntry entry : entries) {
                System.out.println(entry.arrivalTime() + "   " + entry.trip().getRoute().getFullName());
            }

        } catch (ScheduleNotFoundException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("\nРАСПИСАНИЕ ОСТАНОВКИ: " + park.name());
        System.out.println("ВЫХОДНЫЕ ДНИ\n");

        try {
            List<StopScheduleEntry> entries = parkSchedule.getScheduleEntries(DayType.WEEKEND);

            for (StopScheduleEntry entry : entries) {System.out.println(entry.arrivalTime() + "   " + entry.trip().getRoute().getFullName());
            }

        } catch (ScheduleNotFoundException e) {
            System.out.println(e.getMessage());
        }

        // Расписание по остановке по конкретной дате
        LocalDate date = LocalDate.of(2026, 10, 16);

        System.out.println("\nРАСПИСАНИЕ ОСТАНОВКИ: " + park.name());
        System.out.println("ДАТА: " + date + "\n");

        try {
            List<StopScheduleDateEntry> entries = parkSchedule.getScheduleDateEntries(date);

            for (StopScheduleDateEntry entry : entries) {
                System.out.println(entry.arrivalDateTime() + "   " + entry.trip().getRoute().getFullName());
            }

        } catch (ScheduleNotFoundException e) {
            System.out.println(e.getMessage());
        }

        // Выброс исключения для остановки, которой нет ни в 1 маршруте
        System.out.println();
        Stop unknownStop = new Stop(99, "Неизвестная остановка");
        StopSchedule emptySchedule = new StopSchedule(unknownStop, trips);

        try {
            emptySchedule.getScheduleDateEntries(LocalDate.of(2026, 10, 17));
        } catch (ScheduleNotFoundException e) {
            System.out.println(e.getMessage());
        }

        // Время прибытия на k-ую остановку
        System.out.println("\nВРЕМЯ ПРИБЫТИЯ НА ОСТАНОВКУ");

        // Автобус №10 — прибытие в Музей
        System.out.println("\nАВТОБУС №10 — ПРИБЫТИЕ В МУЗЕЙ");

        for (Trip trip : trips) {
            if (trip.getRoute().equals(route10)) {
                System.out.println("Отправление: " + trip.getDepartureTime()
                                + " | Прибытие: " + trip.getArrivalTimeAt(2)
                                + " | Тип дня: " + trip.getDayType()
                );
            }
        }

        // Троллейбус №20 — прибытие в Школу
        System.out.println("\nТРОЛЛЕЙБУС №20 — ПРИБЫТИЕ В ШКОЛУ");

        for (Trip trip : trips) {
            if (trip.getRoute().equals(route20)) {
                System.out.println("Отправление: " + trip.getDepartureTime()
                                + " | Прибытие: " + trip.getArrivalTimeAt(1)
                                + " | Тип дня: " + trip.getDayType()
                );
            }
        }

        // Трамвай №30 — прибытие в Университет
        System.out.println("\nТРАМВАЙ №30 — ПРИБЫТИЕ В УНИВЕРСИТЕТ");

        for (Trip trip : trips) {
            if (trip.getRoute().equals(route30)) {
                System.out.println("Отправление: " + trip.getDepartureTime()
                                + " | Прибытие: " + trip.getArrivalTimeAt(2)
                                + " | Тип дня: " + trip.getDayType()
                );
            }
        }

        // Время прибытия на k-ую остановку с учётом даты
        System.out.println("\nВРЕМЯ ПРИБЫТИЯ НА ОСТАНОВКУ С УЧЁТОМ ДАТЫ");

        LocalDate tripDate = LocalDate.of(2026, 10, 16);

        System.out.println("\nАВТОБУС №10 — ПРИБЫТИЕ В МУЗЕЙ");
        System.out.println("Дата поездки: " + tripDate);

        for (Trip trip : trips) {
            if (trip.getRoute().equals(route10) && trip.getDayType() == DayType.WEEKDAY) {
                System.out.println("Отправление: " + tripDate.atTime(trip.getDepartureTime())
                                + " | Прибытие: "
                                + trip.getArrivalDateTimeAt(tripDate, 2)
                );
            }
        }

        // Время прибытия на k-ую остановку. Ночной рейс с переходом через полночь
        System.out.println("\nНОЧНОЙ РЕЙС — ПЕРЕХОД ЧЕРЕЗ ПОЛНОЧЬ");

        Trip nightTrip = new Trip(route50, LocalTime.of(23, 50), DayType.WEEKDAY);

        LocalDate nightDate = LocalDate.of(2026, 10, 16);

        System.out.println("Маршрут: " + nightTrip.getRoute().getFullName());
        System.out.println("Отправление: " + nightDate.atTime(nightTrip.getDepartureTime()));
        System.out.println("Прибытие в Больницу: " + nightTrip.getArrivalDateTimeAt(nightDate, 1));
        System.out.println("Прибытие в Аэропорт: " + nightTrip.getArrivalDateTimeAt(nightDate, 2));

        // Интервалы движения по маршрутам
        System.out.println("\nИНТЕРВАЛЫ ДВИЖЕНИЯ");
        System.out.println();

        for (Route route : routes) {
            System.out.println(route.getFullName() + " | Интервал: " + route.getInterval().toMinutes() + " мин"
            );
        }

        // Общее время движения в пути
        System.out.println("\nОБЩЕЕ ВРЕМЯ ДВИЖЕНИЯ В ПУТИ");
        System.out.println();

        for (Route route : routes) {
            System.out.println(route.getFullName() + " | Время в пути: " + route.getTotalDuration().toMinutes() + " мин");
        }

        // Время поездки между остановками
        System.out.println("\nВРЕМЯ ПОЕЗДКИ МЕЖДУ ОСТАНОВКАМИ");
        System.out.println();

        System.out.println("Автобус №10: Вокзал -> Парк: " + route10.getDurationBetweenStops(0, 1).toMinutes() + " мин");
        System.out.println("Автобус №10: Парк -> Музей: " + route10.getDurationBetweenStops(1, 2).toMinutes() + " мин");
        System.out.println("Автобус №10: Вокзал -> Музей: " + route10.getDurationBetweenStops(0, 2).toMinutes() + " мин");
        System.out.println("Трамвай №30: Площадь -> Университет: " + route30.getDurationBetweenStops(1, 2).toMinutes() + " мин");
        System.out.println("Метро №60: Площадь -> Аэропорт: " + route60.getDurationBetweenStops(0, 2).toMinutes() + " мин");

        // Поиск прямых маршрутов, остановки без пересадок
        System.out.println("\nПОИСК ПРЯМЫХ МАРШРУТОВ");

        DirectPathFinder directFinder = new DirectPathFinder(routes);

        // Вокзал -> Музей
        try {
            System.out.println("\nВокзал -> Музей");

            List<Route> foundRoutes = directFinder.findRoutes(station, museum);

            for (Route route : foundRoutes) {
                System.out.println("Найден маршрут: " + route.getFullName());
            }

        } catch (TransportException e) {
            System.out.println(e.getMessage());
        }

        // Вокзал -> Аэропорт
        try {
            System.out.println("\nВокзал -> Аэропорт");

            List<Route> foundRoutes = directFinder.findRoutes(station, airport);

            for (Route route : foundRoutes) {
                System.out.println("Найден маршрут: " + route.getFullName());
            }

        } catch (TransportException e) {
            System.out.println(e.getMessage());
        }

        // Площадь -> Аэропорт
        try {
            System.out.println("\nПлощадь -> Аэропорт");

            List<Route> foundRoutes = directFinder.findRoutes(square, airport);

            for (Route route : foundRoutes) {
                System.out.println("Найден маршрут: " + route.getFullName());
            }

        } catch (TransportException e) {
            System.out.println(e.getMessage());
        }

        // Прямой маршрут отсутствует
        System.out.println("\nПОИСК НЕСУЩЕСТВУЮЩЕГО ПРЯМОГО МАРШРУТА");
        System.out.println();

        try {
            System.out.println("Парк -> Аэропорт");

            List<Route> foundRoutes = directFinder.findRoutes(park, airport);

            for (Route route : foundRoutes) {
                System.out.println("Найден маршрут: " + route.getFullName());
            }

        } catch (TransportException e) {
            System.out.println(e.getMessage());

        }

        // Поиск прямых маршрутов. Проверка направления движения
        System.out.println("\nПОИСК ПРЯМЫХ МАРШРУТОВ. ПРОВЕРКА НАПРАВЛЕНИЯ ДВИЖЕНИЯ");
        System.out.println();

        try {
            System.out.println("Музей -> Вокзал");

            List<Route> foundRoutes = directFinder.findRoutes(museum, station);

            for (Route route : foundRoutes) {
                System.out.println("Найден маршрут: " + route.getFullName());
            }

        } catch (TransportException e) {
            System.out.println(e.getMessage());
        }

        // Поиск нескольких прямых маршрутов
        System.out.println("\nПОИСК НЕСКОЛЬКИХ ПРЯМЫХ МАРШРУТОВ");

        PathFinder directPathFinder = new DirectPathFinder(routes);

        try {
            System.out.println("Вокзал -> Аэропорт");

            List<Route> foundRoutes = directPathFinder.findRoutes(station, airport);

            System.out.println("Найдено маршрутов: " + foundRoutes.size());

            for (Route route : foundRoutes) {
                System.out.println(route.getFullName() + " | Время в пути: " + route.getDurationBetweenStops(0, 2).toMinutes() + " мин");
            }

        } catch (TransportException e) {
            System.out.println(e.getMessage());
        }

        // Поиск маршрутов с одной пересадкой
        System.out.println("\nПОИСК МАРШРУТОВ С ОДНОЙ ПЕРЕСАДКОЙ");
        System.out.println();

        TransferPathFinder transferFinder = new TransferPathFinder(trips, routes);

        try {
            System.out.println("Вокзал -> Школа");

            List<TransferRouteOption> options = transferFinder.findTransferOptions(station, school);

            for (TransferRouteOption option : options) {
                System.out.println("Первый маршрут: " + option.firstRoute().getFullName());
                System.out.println("Пересадка: " + option.transferStop().name());
                System.out.println("Второй маршрут: " + option.secondRoute().getFullName());
                System.out.println();
            }

        } catch (TransportException e) {
            System.out.println(e.getMessage());
        }

        // Поиск оптимальной поездки с одной пересадкой
        System.out.println("ОПТИМАЛЬНАЯ ПОЕЗДКА С ПЕРЕСАДКОЙ");
        System.out.println();

        try {
            LocalDate travelDate = LocalDate.of(2026, 10, 16);
            Duration minTransferTime = Duration.ofMinutes(3);

            TransferJourney journey = transferFinder.findBestJourney(station, library, travelDate, minTransferTime);

            System.out.println("Откуда: " + station.name());
            System.out.println("Куда: " + library.name());
            System.out.println("Первый маршрут: " + journey.firstTrip().getRoute().getFullName());
            System.out.println("Время отправления первого рейса с самой первой остановки маршрута: " + journey.firstTrip().getDepartureTime());
            System.out.println("Время отправления первого рейса с остановки посадки: " + journey.firstTrip().getArrivalTimeAt(journey.boardingIndex()));
            System.out.println("Пересадка: " + journey.transferStop().name());
            System.out.println("Время прибытия первого рейса на остановку пересадки: " + journey.firstTrip().getArrivalTimeAt(journey.firstTransferIndex()));
            System.out.println("Минимальное время для пересадки: " + minTransferTime.toMinutes() + " мин");
            System.out.println("Ожидание пересадки: " + journey.waitingTime().toMinutes() + " мин");
            System.out.println("Второй маршрут: " + journey.secondTrip().getRoute().getFullName());
            System.out.println("Время отправления второго рейса с первой остановки маршрута: " + journey.secondTrip().getDepartureTime());
            System.out.println("Время прибытия второго рейса на остановку пересадки: " + journey.secondTrip().getArrivalTimeAt(journey.secondTransferIndex()));
            System.out.println("Время прибытия второго рейса на конечную остановку: " + journey.secondTrip().getArrivalTimeAt(journey.destinationIndex()));
            System.out.println("Общее время поездки: " + journey.totalDuration().toMinutes() + " мин");
            System.out.println("Отправление с остановки посадки первого рейса: " + journey.departureDateTime());
            System.out.println("Прибытие на конечную остановку второго рейса: " + journey.arrivalDateTime());

        } catch (TransportException e) {
            System.out.println(e.getMessage());
        }

        // Недостаточное время на пересадку. Выбор рейса на следующую дату
        System.out.println("\nНЕДОСТАТОЧНОЕ ВРЕМЯ НА ПЕРЕСАДКУ");
        System.out.println();

        try {
            LocalDate travelDate = LocalDate.of(2026, 10, 16);
            Duration minTransferTime = Duration.ofMinutes(10);
            TransferJourney journey = transferFinder.findBestJourney(station, school, travelDate, minTransferTime);

            System.out.println("Откуда: " + station.name());
            System.out.println("Куда: " + school.name());
            System.out.println("Первый маршрут: " + journey.firstTrip().getRoute().getFullName());
            System.out.println("Время отправления первого рейса с самой первой остановки маршрута: " + journey.firstTrip().getDepartureTime());
            System.out.println("Время отправления первого рейса с остановки посадки маршрута: " + journey.firstTrip().getArrivalTimeAt(journey.boardingIndex()));
            System.out.println("Пересадка: " + journey.transferStop().name());
            System.out.println("Время прибытия первого рейса на остановку пересадки: " + journey.firstTrip().getArrivalTimeAt(journey.firstTransferIndex()));
            System.out.println("Минимальное время для пересадки: " + minTransferTime.toMinutes());
            System.out.println("Ожидание пересадки: " + journey.waitingTime().toHours() + " ч " + journey.waitingTime().toMinutesPart() + " мин");
            System.out.println("Второй маршрут: " + journey.secondTrip().getRoute().getFullName());
            System.out.println("Время отправления второго рейса с первой остановки маршрута: " + journey.secondTrip().getDepartureTime());
            System.out.println("Время прибытия второго рейса на остановку пересадки в этот день (на него не успеваем): " + journey.secondTrip().getArrivalDateTimeAt(travelDate, journey.secondTransferIndex()));
            System.out.println("Время прибытия второго рейса на остановку пересадки в следующий подходящий день: " + journey.arrivalDateTime().minus(journey.secondTrip().getRoute().getDurationBetweenStops(journey.secondTransferIndex(), journey.destinationIndex())));
            System.out.println("Время прибытия второго рейса на конечную остановку: " + journey.arrivalDateTime());
            System.out.println("Общее время поездки: " + journey.totalDuration().toHours() + " ч " + journey.totalDuration().toMinutesPart() + " мин");
            System.out.println("Отправление c остановки посадки первого рейса: " + journey.departureDateTime());
            System.out.println("Прибытие на конечную остановку второго рейса: " + journey.arrivalDateTime());

        } catch (TransportException e) {
            System.out.println(e.getMessage());
        }

        // Исключение при отсутствии маршрута с пересадкой
        System.out.println("\nОТСУТСТВИЕ МАРШРУТА С ПЕРЕСАДКОЙ");
        System.out.println();

        try {
            TransferJourney journey = transferFinder.findBestJourney(island, airport, LocalDate.of(2026, 10, 16), Duration.ofMinutes(5));

            System.out.println("Найдена поездка: " + journey.totalDuration().toMinutes() + " мин");

        } catch (TransportException e) {
            System.out.println(e.getMessage());
        }

        // Демонстрация полиморфизма
        System.out.println("\nДЕМОНСТРАЦИЯ ПОЛИМОРФИЗМА");

        PathFinder pathFinder = new DirectPathFinder(routes);

        try {
            System.out.println("\nПоиск прямого маршрута: Вокзал -> Музей");

            List<Route> foundRoutes = pathFinder.findRoutes(station, museum);

            for (Route route : foundRoutes) {
                System.out.println("Найден маршрут: " + route.getFullName());
            }

        } catch (TransportException e) {
            System.out.println(e.getMessage());
        }

        // Меняем реализацию интерфейса

        pathFinder = new TransferPathFinder(trips, routes);

        try {
            System.out.println("\nПоиск маршрута с пересадкой: Вокзал -> Университет");

            List<Route> foundRoutes = pathFinder.findRoutes(station, university);

            System.out.println("Найдено маршрутов: " + foundRoutes.size());

            for (Route route : foundRoutes) {
                System.out.println(route.getFullName());
            }

        } catch (TransportException e) {
            System.out.println(e.getMessage());
        }
    }



}