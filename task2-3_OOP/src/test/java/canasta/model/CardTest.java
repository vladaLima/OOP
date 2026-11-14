package canasta.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CardTest {

    @Test
    @DisplayName("Создание обычной карты и проверка её характеристик")
    void shouldCreateRegularCard() {
        Card card = new Card(1, Rank.SEVEN, Suit.HEARTS, null);

        assertEquals(1, card.getId());
        assertEquals(Suit.HEARTS, card.getSuit());
        assertEquals(Rank.SEVEN, card.getRank());
        assertNull(card.getJokerColor());
    }

    @Test
    @DisplayName("Создание красного джокера")
    void shouldCreateRedJoker() {
        Card joker = new Card(2, Rank.JOKER, null, JokerColor.RED);

        assertTrue(joker.isJoker());
        assertTrue(joker.isWild());
        assertEquals(JokerColor.RED, joker.getJokerColor());
        assertNull(joker.getSuit());
    }

    @Test
    @DisplayName("Создание чёрного джокера")
    void shouldCreateBlackJoker() {
        Card joker = new Card(3, Rank.JOKER, null, JokerColor.BLACK);

        assertTrue(joker.isJoker());
        assertEquals(JokerColor.BLACK, joker.getJokerColor());
    }

    @Test
    @DisplayName("Определение двойки как дикой карты")
    void shouldRecognizeWildCard() {
        Card two = new Card(4, Rank.TWO, Suit.SPADES,  null);

        assertTrue(two.isWild());
    }

    @Test
    @DisplayName("Правильное определение красной тройки")
    void shouldRecognizeRedThree() {
        Card three = new Card(5, Rank.THREE, Suit.HEARTS, null);

        assertTrue(three.isRedThree());
        assertFalse(three.isBlackThree());
    }

    @Test
    @DisplayName("Правильное определение чёрной тройки")
    void shouldRecognizeBlackThree() {
        Card three = new Card(6, Rank.THREE, Suit.CLUBS, null);

        assertTrue(three.isBlackThree());
        assertFalse(three.isRedThree());
    }

    @Test
    @DisplayName("Проверка на запрет создания карты с отрицательным ID")
    void shouldRejectNegativeId() {
        assertThrows(IllegalArgumentException.class, () -> new Card(-1, Rank.SEVEN, Suit.HEARTS, null));
    }

    @Test
    @DisplayName("Проверка на запрет создания обычной карты без масти")
    void shouldRejectRegularCardWithoutSuit() {
        assertThrows(IllegalArgumentException.class, () -> new Card(7, Rank.SEVEN,null, null));
    }

    @Test
    @DisplayName("Проверка на запрет создания джокера без указания цвета")
    void shouldRejectJokerWithoutColor() {
        assertThrows(IllegalArgumentException.class, () -> new Card(8, Rank.JOKER,null, null));
    }

    @Test
    @DisplayName("Проверка на запрет создания джокера с мастью")
    void shouldRejectJokerWithSuit() {
        assertThrows(IllegalArgumentException.class, () -> new Card(9, Rank.JOKER, Suit.HEARTS, JokerColor.RED));
    }

    @Test
    @DisplayName("Карты с одинаковым ID должны считаться равными")
    void shouldBeEqualWhenIdsAreSame() {
        Card first = new Card(1, Rank.ACE, Suit.HEARTS, null);
        Card second = new Card(1, Rank.KING, Suit.SPADES, null);

        assertEquals(first, second);
    }

    @Test
    @DisplayName("Карты с разными ID не должны считаться равными")
    void shouldNotBeEqualWhenIdsAreDifferent() {
        Card first = new Card(1, Rank.ACE, Suit.HEARTS, null);
        Card second = new Card(2, Rank.ACE, Suit.HEARTS, null);

        assertNotEquals(first, second);
    }

    @Test
    @DisplayName("Равные карты должны иметь одинаковый hashCode")
    void shouldHaveSameHashCodeWhenCardsAreEqual() {
        Card first = new Card(1, Rank.ACE, Suit.HEARTS, null);
        Card second = new Card(1, Rank.KING, Suit.SPADES, null);

        assertEquals(first.hashCode(), second.hashCode());
    }
}

