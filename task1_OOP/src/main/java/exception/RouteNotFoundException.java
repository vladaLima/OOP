package exception;

import model.Stop;

/**
 * Сообщает об отсутствии подходящего маршрута или поездки.
 */
public class RouteNotFoundException extends TransportException {
    /**
     * Создаёт исключение для пары остановок, между которыми не найден прямой маршрут.
     *
     * @param from начальная остановка
     * @param to конечная остановка
     */
    public RouteNotFoundException(Stop from, Stop to) {
        super("No direct route found between " + from.name() + " and " + to.name());
    }
    /**
     * Создаёт исключение с произвольным описанием отсутствующего маршрута.
     *
     * @param message описание ошибки
     */
    public RouteNotFoundException(String message) {
        super(message);
    }
}