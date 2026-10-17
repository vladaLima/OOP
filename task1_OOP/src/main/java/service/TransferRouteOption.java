package service;

import model.Route;
import model.Stop;

/**
 * Вариант поездки с одной пересадкой между двумя маршрутами.
 *
 * @param firstRoute первый маршрут
 * @param secondRoute второй маршрут
 * @param transferStop общая остановка пересадки
 */
public record TransferRouteOption(Route firstRoute, Route secondRoute, Stop transferStop) {
    /**
     * Проверяет наличие обоих маршрутов и принадлежность остановки пересадки каждому из них.
     *
     * @throws IllegalArgumentException если маршруты или остановка некорректны
     */
    public TransferRouteOption {
        if (firstRoute == null || secondRoute == null) {
            throw new IllegalArgumentException("Routes cannot be null");
        }
        if (transferStop == null) {
            throw new IllegalArgumentException("Transfer stop cannot be null");
        }
        if (!firstRoute.containsStop(transferStop) || !secondRoute.containsStop(transferStop)) {
            throw new IllegalArgumentException("Transfer stop must belong to both routes");
        }
    }
}
