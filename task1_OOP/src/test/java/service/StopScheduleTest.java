package service;

import exception.ScheduleNotFoundException;
import model.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class StopScheduleTest {
    private final Stop station = new Stop(1, "Вокзал");
    private final Stop park = new Stop(2, "Парк");
    private final Stop school = new Stop(3, "Школа");

    private Route createRoute() {
        return new Route(
                10,
                "Вокзал — Школа",
                TransportType.BUS,
                List.of(new RouteSegment(station, Duration.ofMinutes(10)),
                        new RouteSegment(park, Duration.ofMinutes(15)),
                        new RouteSegment(school, Duration.ZERO)
                ),
                Duration.ofMinutes(12));
    }

    @Test
    @DisplayName("Расписание фильтруется по типу дня и сортируется по времени прибытия, получаем расписание по типу дня")
    void shouldFilterAndSortScheduleByDayTypeAndArrivalTime() throws ScheduleNotFoundException {
        Route route = createRoute();
        Trip weekdayLate = new Trip(route, LocalTime.of(9, 0), DayType.WEEKDAY);
        Trip weekend = new Trip(route, LocalTime.of(8, 0), DayType.WEEKEND);
        Trip weekdayEarly = new Trip(route, LocalTime.of(7, 30), DayType.WEEKDAY);
        StopSchedule schedule = new StopSchedule(park, List.of(weekdayLate, weekend, weekdayEarly));
        List<StopScheduleEntry> weekdayEntries = schedule.getScheduleEntries(DayType.WEEKDAY);

        assertEquals(2, weekdayEntries.size());
        assertEquals(LocalTime.of(7, 40), weekdayEntries.get(0).arrivalTime());
        assertEquals(LocalTime.of(9, 10), weekdayEntries.get(1).arrivalTime());

        List<StopScheduleEntry> weekendEntries = schedule.getScheduleEntries(DayType.WEEKEND);

        assertEquals(1, weekendEntries.size());
        assertEquals(LocalTime.of(8, 10), weekendEntries.get(0).arrivalTime());
    }

    @Test
    @DisplayName("Рейс пятницы учитывается в расписании прибытия на субботу, получаем календарное расписание")
    void shouldIncludePreviousDayTrip() throws ScheduleNotFoundException {
        Route route = createRoute();
        Trip trip1 = new Trip(route, LocalTime.of(23, 50), DayType.WEEKDAY);
        Trip trip2 = new Trip(route, LocalTime.of(10, 50), DayType.WEEKDAY);
        Trip trip3 = new Trip(route, LocalTime.of(15, 50), DayType.WEEKEND);
        StopSchedule schedule = new StopSchedule(school, List.of(trip1, trip2, trip3));
        LocalDate saturday = LocalDate.of(2026, 10, 17);
        List<StopScheduleDateEntry> entries = schedule.getScheduleDateEntries(saturday);

        assertEquals(2, entries.size());

        StopScheduleDateEntry entry1 = entries.get(0);

        assertEquals(LocalDateTime.of(2026, 10, 16, 23, 50), entry1.departureDateTime());
        assertEquals(LocalDateTime.of(2026, 10, 17, 0, 15), entry1.arrivalDateTime());

        StopScheduleDateEntry entry2 = entries.get(1);

        assertEquals(LocalDateTime.of(2026, 10, 17, 15, 50), entry2.departureDateTime());
        assertEquals(LocalDateTime.of(2026, 10, 17, 16, 15), entry2.arrivalDateTime());
    }

    @Test
    @DisplayName("Отсутствие рейсов нужного типа дня вызывает исключение")
    void shouldThrowWhenScheduleNotFound() {
        Route route = createRoute();
        Trip weekdayTrip = new Trip(route, LocalTime.of(8, 0), DayType.WEEKDAY);
        StopSchedule schedule = new StopSchedule(park, List.of(weekdayTrip));

        assertThrows(ScheduleNotFoundException.class, () -> schedule.getScheduleEntries(DayType.WEEKEND));
    }

    @Test
    @DisplayName("Отсутствие рейсов нужного календарного дня вызывает исключение")
    void shouldThrowWhenScheduleDateNotFound() throws ScheduleNotFoundException {
        Route route = createRoute();
        Trip trip1 = new Trip(route, LocalTime.of(8, 0), DayType.WEEKDAY);
        Trip trip2 = new Trip(route, LocalTime.of(23, 50), DayType.WEEKDAY);
        StopSchedule schedule = new StopSchedule(park, List.of(trip1, trip2));
        LocalDate sunday = LocalDate.of(2026, 10, 18);

        assertThrows(ScheduleNotFoundException.class, () -> schedule.getScheduleDateEntries(sunday));
    }

    @Test
    @DisplayName("Отсутствие календарной даты вызывает исключение")
    void shouldRejectNullDate() {
        Route route = createRoute();
        Trip trip = new Trip(route, LocalTime.of(8, 0), DayType.WEEKDAY);
        StopSchedule schedule = new StopSchedule(park, List.of(trip));

        assertThrows(IllegalArgumentException.class, () -> schedule.getScheduleDateEntries(null));
    }
}
