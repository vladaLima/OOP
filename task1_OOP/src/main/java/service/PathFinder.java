package service;

import exception.TransportException;
import model.Route;
import model.Stop;

import java.util.List;

/**
 * Общий интерфейс поиска маршрутов между двумя остановками.
 */
public interface PathFinder {
    /**
     * Ищет маршруты, связывающие заданные остановки согласно стратегии реализации.
     *
     * @param from начальная остановка
     * @param to конечная остановка
     * @return список найденных маршрутов
     * @throws TransportException если подходящий маршрут не найден
     */
    List<Route> findRoutes(Stop from, Stop to) throws TransportException;
}
