package exception;

/**
 * Базовое проверяемое исключение при работе с транспортной системой.
 */
public class TransportException extends Exception {
    /**
     * Создаёт исключение с пояснением причины ошибки.
     *
     * @param message описание ошибки
     */
    public TransportException(String message) {
        super(message);
    }
}
