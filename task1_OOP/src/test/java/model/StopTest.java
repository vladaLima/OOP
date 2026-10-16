package model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StopTest {
    @Test
    @DisplayName("Остановки с одинаковыми данными равны и имеют одинаковый hashCode")
    void shouldBeEqualWhenFieldsAreEqual() {
        Stop first = new Stop(1, "Вокзал");
        Stop second = new Stop(1, "Вокзал");

        assertEquals(first, second);
        assertEquals(first.hashCode(), second.hashCode());
    }

    @Test
    @DisplayName("Остановки с разными данными НЕ равны и имеют одинаковый либо разный hashCode")
    void shouldNotBeEqualWhenFieldsAreDifferent() {
        Stop first = new Stop(1, "Вокзал");
        Stop second = new Stop(2, "Вокзал");

        assertNotEquals(first, second);
    }

    @Test
    @DisplayName("Создание остановки с неположительным ID вызывает исключение")
    void shouldRejectNonPositiveId() {
        assertThrows(IllegalArgumentException.class, () -> new Stop(0, "Вокзал"));
    }

}
