package model;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Упорядоченный маршрут транспорта с остановками, временем хода и интервалом движения.
 */
public class Route {
    private final int number;
    private final String name;
    private final TransportType type;
    private final List<RouteSegment> segments;
    private final Duration interval;

    /**
     * Создаёт маршрут и проверяет корректность его параметров и последовательности участков.
     *
     * @param number положительный номер маршрута
     * @param name непустое название маршрута
     * @param type вид транспорта
     * @param segments участки маршрута в порядке следования; последний имеет нулевую длительность
     * @param interval положительный интервал движения
     * @throws IllegalArgumentException если параметры или участки маршрута некорректны
     */
    public Route(int number, String name, TransportType type, List<RouteSegment> segments, Duration interval) {
        if (number <= 0) {
            throw new IllegalArgumentException("Route number must be positive");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Route name cannot be null or blank");
        }
        if (type == null) {
            throw new IllegalArgumentException("Route type cannot be null");
        }
        if (segments == null || segments.size() < 2) {
            throw new IllegalArgumentException("Route must contain at least two stops");
        }
        if (interval == null || interval.isNegative() || interval.isZero()) {
            throw new IllegalArgumentException("Route interval must be positive");
        }


        for (int i = 0; i < segments.size(); i++) {
            RouteSegment segment = segments.get(i);

            if (segment == null) {
                throw new IllegalArgumentException("Route segment cannot be null");
            }

            Duration duration = segment.durationOfTrip();

            boolean isLast = i == segments.size() - 1;

            if (isLast && !duration.isZero()) {
                throw new IllegalArgumentException("Last stop duration must be zero");
            }
            if (!isLast && duration.isZero()) {
                throw new IllegalArgumentException("Duration time between stops must be positive");
            }
        }

        this.number = number;
        this.name = name;
        this.type = type;
        this.segments = List.copyOf(segments);
        this.interval = interval;
    }

    /**
     * Возвращает номер маршрута.
     *
     * @return номер маршрута
     */
    public int getNumber() {
        return number;
    }

    /**
     * Возвращает название маршрута.
     *
     * @return название маршрута
     */
    public String getName() {
        return name;
    }

    /**
     * Формирует полное название маршрута с видом транспорта и номером.
     *
     * @return строка с видом транспорта, номером и названием маршрута
     */
    public String getFullName() {
        return type.getTransportType() + " №" + number + " (" + name + ")";
    }

    /**
     * Возвращает вид транспорта на маршруте.
     *
     * @return вид транспорта
     */
    public TransportType getType() {
        return type;
    }

    /**
     * Возвращает неизменяемую копию списка участков в порядке следования.
     *
     * @return список участков маршрута
     */
    public List<RouteSegment> getSegments() {
        return List.copyOf(this.segments);
    }

    /**
     * Возвращает установленный интервал движения рейсов.
     *
     * @return интервал движения
     */
    public Duration getInterval() {
        return interval;
    }

    /**
     * Вычисляет полное время движения от первой до последней остановки.
     *
     * @return сумма длительностей всех участков
     */
    public Duration getTotalDuration() {
        return segments.stream().map(RouteSegment::durationOfTrip).reduce(Duration.ZERO, Duration::plus);
    }

    /**
     * Вычисляет время движения от начала маршрута до остановки по её индексу.
     *
     * @param stopIndex индекс остановки, начиная с нуля
     * @return время движения от первой остановки
     * @throws IllegalArgumentException если индекс вне границ маршрута
     */
    public Duration getDurationToStop(int stopIndex) {
        if (stopIndex < 0 || stopIndex >= segments.size()) {
            throw new IllegalArgumentException("Stop index out of bounds");
        }

        var durationToStop = Duration.ZERO;

        for (int i = 0; i < stopIndex; i++) {
           durationToStop = durationToStop.plus(segments.get(i).durationOfTrip());
        }

        return durationToStop;
    }

    /**
     * Вычисляет время движения между остановками в прямом направлении маршрута.
     *
     * @param fromIndex индекс начальной остановки
     * @param toIndex индекс конечной остановки, строго больший начального
     * @return время движения между остановками
     * @throws IllegalArgumentException если индексы некорректны или нарушен порядок движения
     */
    public Duration getDurationBetweenStops(int fromIndex, int toIndex) {
        if (fromIndex < 0 || fromIndex >= segments.size() || toIndex < 0 || toIndex >= segments.size() || fromIndex >= toIndex) {
            throw new IllegalArgumentException("Invalid stop indexes");
        }

        return getDurationToStop(toIndex).minus(getDurationToStop(fromIndex));
    }

    /**
     * Находит все вхождения остановки в маршруте, включая повторные.
     *
     * @param stop искомая остановка
     * @return неизменяемый список индексов; пустой, если остановка не встречается
     * @throws IllegalArgumentException если остановка равна null
     */
    public List<Integer> indexesOfStop(Stop stop) {
        if (stop == null) {
            throw new IllegalArgumentException("Stop cannot be null");
        }

        List<Integer> indexes = new ArrayList<>();

        for (int i = 0; i < segments.size(); i++) {
            if (segments.get(i).stop().equals(stop)) {
                indexes.add(i);
            }
        }

        return List.copyOf(indexes);
    }

    /**
     * Проверяет возможность проехать от одной остановки до другой без пересадки по направлению маршрута.
     *
     * @param from остановка посадки
     * @param to остановка назначения
     * @return true, если конечная остановка встречается после начальной
     * @throws IllegalArgumentException если любая остановка равна null
     */
    public boolean canTravel(Stop from, Stop to) {
        if (from == null || to == null) {
            throw new IllegalArgumentException("Stops cannot be null");
        }

        for (int i = 0; i < segments.size(); i++) {
            if (segments.get(i).stop().equals(from)) {
                for (int j = i + 1; j < segments.size(); j++) {
                    if (segments.get(j).stop().equals(to)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /**
     * Проверяет наличие остановки в маршруте.
     *
     * @param targetStop искомая остановка
     * @return true, если остановка входит в маршрут
     * @throws IllegalArgumentException если остановка равна null
     */
    public boolean containsStop(Stop targetStop) {
        return !indexesOfStop(targetStop).isEmpty();
    }

    /**
     * Сравнивает маршруты по номеру, названию, виду транспорта, участкам и интервалу.
     *
     * @param obj объект для сравнения
     * @return true, если все характеристики маршрутов совпадают
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Route other)) {
            return false;
        }

        return number == other.number && name.equals(other.name) && type == other.type && segments.equals(other.segments) && interval.equals(other.interval);
    }

    /**
     * Вычисляет хеш-код по характеристикам маршрута, используемым в equals.
     *
     * @return хеш-код маршрута
     */
    @Override
    public int hashCode() {
        return Objects.hash(number, name, type, segments, interval);
    }
}
