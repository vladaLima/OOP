package model;

/**
 * Неизменяемая остановка, идентифицируемая номером и названием.
 *
 * @param id положительный идентификатор остановки
 * @param name непустое название остановки
 */
public record Stop(int id, String name) {
    /**
     * Проверяет корректность идентификатора и названия остановки.
     *
     * @throws IllegalArgumentException если идентификатор неположительный или название пустое
     */
    public Stop {
        if (id <= 0) {
            throw new IllegalArgumentException("id must be positive");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name cannot be null or blank");
        }
    }
}
