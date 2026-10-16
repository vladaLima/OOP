package model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class RouteSegmentTest {
    @Test
    @DisplayName("Создание сегмента с отрицательной длительностью вызывает исключение")
    void shouldRejectNegativeDuration() {
        Stop stop = new Stop(1, "Вокзал");

        assertThrows(IllegalArgumentException.class, () -> new RouteSegment(stop, Duration.ofMinutes(-5)));
    }
}
