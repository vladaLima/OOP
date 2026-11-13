package canasta.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {
    @Test
    @DisplayName("Игрок должен создаваться с правильными данными")
    void shouldCreatePlayer() {
        Player player = new Player(1, "Vlada");

        assertEquals(1, player.getId());
        assertEquals("Vlada", player.getName());
        assertEquals(0, player.getHandSize());
    }

    @Test
    @DisplayName("Игрок должен получать карту в руку")
    void shouldAddCardToHand() {
        Player player = new Player(1, "Vlada");
        Card card = new Card(1, Rank.ACE, Suit.HEARTS, null);

        player.addCard(card);

        assertEquals(1, player.getHandSize());
        assertTrue(player.getHand().contains(card));
    }

    @Test
    @DisplayName("Игрок должен уметь удалять карту из руки")
    void shouldRemoveCardFromHand() {
        Player player = new Player(1, "Vlada");
        Card card = new Card(1, Rank.ACE, Suit.HEARTS, null);

        player.addCard(card);
        player.removeCard(card);

        assertEquals(0, player.getHandSize());
        assertTrue(player.isHandEmpty());
    }

    @Test
    @DisplayName("Нельзя добавить null в руку игрока")
    void shouldRejectNullCardWhenAdding() {
        Player player = new Player(1, "Vlada");

        assertThrows(IllegalArgumentException.class, () -> player.addCard(null));
    }

    @Test
    @DisplayName("Нельзя удалить null из руки игрока")
    void shouldRejectNullCardWhenRemoving() {
        Player player = new Player(1, "Alex");

        assertThrows(IllegalArgumentException.class, () -> player.removeCard(null));
    }

    @Test
    @DisplayName("Нельзя удалить карту, которой нет в руке")
    void shouldRejectRemovingAbsentCard() {
        Player player = new Player(1, "Vlada");
        Card card = new Card(1, Rank.QUEEN, Suit.DIAMONDS, null);

        assertThrows(NoSuchElementException.class, () -> player.removeCard(card));
    }

    @Test
    @DisplayName("Нельзя изменить руку через getHand")
    void shouldReturnUnmodifiableHand() {
        Player player = new Player(1, "Vlada");
        Card card = new Card(1, Rank.JOKER, null, JokerColor.RED);

        assertThrows(UnsupportedOperationException.class, () -> player.getHand().add(card));
    }
}
