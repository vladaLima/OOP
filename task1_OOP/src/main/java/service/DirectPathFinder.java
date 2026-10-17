package service;

import exception.RouteNotFoundException;
import exception.TransportException;
import model.Route;
import model.Stop;

import java.util.List;

/**
 * Поиск маршрутов, позволяющих проехать без пересадки.
 */
public class DirectPathFinder implements PathFinder {
    private final List<Route> allRoutes;

    /**
     * Создаёт поисковик по заданному набору маршрутов.
     *
     * @param allRoutes список доступных маршрутов
     * @throws IllegalArgumentException если список равен null
     */
    public DirectPathFinder(List<Route> allRoutes) {
        if (allRoutes == null) {
            throw new IllegalArgumentException("Routes cannot be null");
        }

        this.allRoutes = List.copyOf(allRoutes);
    }

    /**
     * Находит маршруты, проходящие через обе остановки в правильном порядке.
     *
     * @param from остановка посадки
     * @param to остановка назначения
     * @return список подходящих прямых маршрутов
     * @throws TransportException если прямой маршрут отсутствует
     * @throws IllegalArgumentException если остановка равна null
     */
    @Override
    public List<Route> findRoutes(Stop from, Stop to) throws TransportException {
        if (from == null || to == null) {
            throw new IllegalArgumentException("Stops cannot be null");
        }

        List<Route> directRoutes = allRoutes.stream().filter(route -> route.canTravel(from, to)).toList();

        if (directRoutes.isEmpty()) {
            throw new RouteNotFoundException(from, to);
        }

        return directRoutes;
    }
}
