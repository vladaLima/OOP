package canasta.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CardTest {

    @Test
    @DisplayName("Создание обычной карты и проверка её характеристик")
    void shouldCreateRegularCard() {
        Card card = new Card(1, Suit.HEARTS, Rank.SEVEN, null);

        assertEquals(1, card.getId());
        assertEquals(Suit.HEARTS, card.getSuit());
        assertEquals(Rank.SEVEN, card.getRank());
        assertNull(card.getJokerColor());
    }

    @Test
    @DisplayName("Создание красного джокера")
    void shouldCreateRedJoker() {
        Card joker = new Card(2, null, Rank.JOKER, JokerColor.RED);

        assertTrue(joker.isJoker());
        assertTrue(joker.isWild());
        assertEquals(JokerColor.RED, joker.getJokerColor());
        assertNull(joker.getSuit());
    }

    @Test
    @DisplayName("Создание чёрного джокера")
    void shouldCreateBlackJoker() {
        Card joker = new Card(3, null, Rank.JOKER, JokerColor.BLACK);

        assertTrue(joker.isJoker());
        assertEquals(JokerColor.BLACK, joker.getJokerColor());
    }

    @Test
    @DisplayName("Определение двойки как дикой карты")
    void shouldRecognizeWildCard() {
        Card two = new Card(4, Suit.SPADES, Rank.TWO, null);

        assertTrue(two.isWild());
    }

    @Test
    @DisplayName("Правильное определение красной тройки")
    void shouldRecognizeRedThree() {
        Card three = new Card(5, Suit.HEARTS, Rank.THREE, null);

        assertTrue(three.isRedThree());
        assertFalse(three.isBlackThree());
    }

    @Test
    @DisplayName("Правильное определение чёрной тройки")
    void shouldRecognizeBlackThree() {
        Card three = new Card(6, Suit.CLUBS, Rank.THREE, null);

        assertTrue(three.isBlackThree());
        assertFalse(three.isRedThree());
    }

    @Test
    @DisplayName("Проверка создания карты с отрицательным ID")
    void shouldRejectNegativeId() {
        assertThrows(IllegalArgumentException.class, () -> new Card(-1, Suit.HEARTS, Rank.SEVEN, null));
    }

    @Test
    @DisplayName("Проверка создания обычной карты без масти")
    void shouldRejectRegularCardWithoutSuit() {
        assertThrows(IllegalArgumentException.class, () -> new Card(7, null, Rank.SEVEN, null));
    }

    @Test
    @DisplayName("Проверка создания джокера без указания цвета")
    void shouldRejectJokerWithoutColor() {
        assertThrows(IllegalArgumentException.class, () -> new Card(8, null, Rank.JOKER, null));
    }

    @Test
    @DisplayName("Проверка создания джокера с мастью")
    void shouldRejectJokerWithSuit() {
        assertThrows(IllegalArgumentException.class, () -> new Card(9, Suit.HEARTS, Rank.JOKER, JokerColor.RED));
    }
}

