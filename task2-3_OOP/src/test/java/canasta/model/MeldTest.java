
package canasta.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MeldTest {

    @Test
    @DisplayName("Создание корректной комбинации из трёх обычных карт одного достоинства")
    void shouldCreateValidMeldWithThreeRegularCards() {
        List<Card> cards = List.of(
                new Card(1, Rank.SEVEN, Suit.HEARTS, null),
                new Card(2, Rank.SEVEN, Suit.DIAMONDS, null),
                new Card(3, Rank.SEVEN, Suit.CLUBS, null)
        );

        Meld meld = new Meld(Rank.SEVEN, cards);

        assertEquals(Rank.SEVEN, meld.getRank());
        assertEquals(3, meld.getCards().size());
        assertEquals(cards, meld.getCards());
    }

    @Test
    @DisplayName("Создание комбинации с null вместо достоинства должно завершаться исключением")
    void shouldThrowExceptionWhenRankIsNull() {
        List<Card> cards = List.of(
                new Card(1, Rank.SEVEN, Suit.HEARTS, null),
                new Card(2, Rank.SEVEN, Suit.DIAMONDS, null),
                new Card(3, Rank.SEVEN, Suit.CLUBS, null)
        );

        assertThrows(IllegalArgumentException.class, () -> new Meld(null, cards));
    }

    @Test
    @DisplayName("Создание комбинации с null вместо списка карт должно завершаться исключением")
    void shouldThrowExceptionWhenCardsAreNull() {
        assertThrows(IllegalArgumentException.class, () -> new Meld(Rank.SEVEN, null));
    }

    @Test
    @DisplayName("Создание комбинации менее чем из трёх карт должно завершаться исключением")
    void shouldThrowExceptionWhenThereAreFewerThanThreeCards() {
        List<Card> cards = List.of(
                new Card(1, Rank.SEVEN, Suit.HEARTS, null),
                new Card(2, Rank.SEVEN, Suit.DIAMONDS, null)
        );

        assertThrows(IllegalArgumentException.class, () -> new Meld(Rank.SEVEN, cards));
    }

    @Test
    @DisplayName("Создание комбинации с недопустимым достоинством TWO, THREE или JOKER должно завершаться исключением")
    void shouldThrowExceptionWhenRankIsNotAllowed() {
        List<Card> cards = List.of(
                new Card(1, Rank.SEVEN, Suit.HEARTS, null),
                new Card(2, Rank.SEVEN, Suit.DIAMONDS, null),
                new Card(3, Rank.SEVEN, Suit.CLUBS, null)
        );

        assertAll(
                () -> assertThrows(IllegalArgumentException.class,
                        () -> new Meld(Rank.TWO, cards)),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> new Meld(Rank.THREE, cards)),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> new Meld(Rank.JOKER, cards))
        );
    }

    @Test
    @DisplayName("Добавление обычной карты другого достоинства в комбинацию должно завершаться исключением")
    void shouldThrowExceptionWhenRegularCardHasDifferentRank() {
        List<Card> cards = List.of(
                new Card(1, Rank.SEVEN, Suit.HEARTS, null),
                new Card(2, Rank.SEVEN, Suit.DIAMONDS, null),
                new Card(3, Rank.EIGHT, Suit.CLUBS, null)
        );

        assertThrows(IllegalArgumentException.class, () -> new Meld(Rank.SEVEN, cards));
    }

    @Test
    @DisplayName("Создание комбинации с одной дикой картой и двумя обычными картами должно быть разрешено")
    void shouldCreateValidMeldWithTwoRegularCardsAndOneTwo() {
        List<Card> cards = List.of(
                new Card(1, Rank.SEVEN, Suit.HEARTS, null),
                new Card(2, Rank.SEVEN, Suit.DIAMONDS, null),
                new Card(3, Rank.TWO, Suit.CLUBS, null)
        );

        Meld meld = new Meld(Rank.SEVEN, cards);

        assertEquals(3, meld.getCards().size());
    }

    @Test
    @DisplayName("Создание комбинации с джокером и двумя обычными картами должно быть разрешено")
    void shouldCreateValidMeldWithJoker() {
        List<Card> cards = List.of(
                new Card(1, Rank.SEVEN, Suit.HEARTS, null),
                new Card(2, Rank.SEVEN, Suit.DIAMONDS, null),
                new Card(3, Rank.JOKER, null, JokerColor.RED)
        );

        Meld meld = new Meld(Rank.SEVEN, cards);

        assertEquals(3, meld.getCards().size());
    }

    @Test
    @DisplayName("Создание комбинации более чем с тремя дикими картами должно завершаться исключением")
    void shouldThrowExceptionWhenThereAreMoreThanThreeWildCards() {
        List<Card> cards = List.of(
                new Card(1, Rank.SEVEN, Suit.HEARTS, null),
                new Card(2, Rank.SEVEN, Suit.DIAMONDS, null),
                new Card(3, Rank.SEVEN, Suit.CLUBS, null),
                new Card(4, Rank.SEVEN, Suit.SPADES, null),
                new Card(5, Rank.TWO, Suit.HEARTS, null),
                new Card(6, Rank.TWO, Suit.DIAMONDS, null),
                new Card(7, Rank.JOKER, null, JokerColor.RED),
                new Card(8, Rank.JOKER, null, JokerColor.BLACK)
        );

        assertThrows(IllegalArgumentException.class, () -> new Meld(Rank.SEVEN, cards));
    }

    @Test
    @DisplayName("Создание комбинации, в которой диких карт больше, чем обычных, должно завершаться исключением")
    void shouldThrowExceptionWhenWildCardsOutnumberRegularCards() {
        List<Card> cards = List.of(
                new Card(1, Rank.SEVEN, Suit.HEARTS, null),
                new Card(2, Rank.SEVEN, Suit.DIAMONDS, null),
                new Card(3, Rank.TWO, Suit.CLUBS, null),
                new Card(4, Rank.TWO, Suit.SPADES, null),
                new Card(5, Rank.JOKER, null, JokerColor.RED)
        );

        assertThrows(IllegalArgumentException.class, () -> new Meld(Rank.SEVEN, cards));
    }

    @Test
    @DisplayName("Добавление красной тройки в комбинацию должно завершаться исключением")
    void shouldThrowExceptionWhenMeldContainsRedThree() {
        List<Card> cards = List.of(
                new Card(1, Rank.SEVEN, Suit.HEARTS, null),
                new Card(2, Rank.SEVEN, Suit.DIAMONDS, null),
                new Card(3, Rank.THREE, Suit.HEARTS, null)
        );

        assertThrows(IllegalArgumentException.class, () -> new Meld(Rank.SEVEN, cards));
    }

    @Test
    @DisplayName("Добавление чёрной тройки в комбинацию должно завершаться исключением")
    void shouldThrowExceptionWhenMeldContainsBlackThree() {
        List<Card> cards = List.of(
                new Card(1, Rank.SEVEN, Suit.HEARTS, null),
                new Card(2, Rank.SEVEN, Suit.DIAMONDS, null),
                new Card(3, Rank.THREE, Suit.SPADES, null)
        );

        assertThrows(IllegalArgumentException.class, () -> new Meld(Rank.SEVEN, cards));
    }

    @Test
    @DisplayName("Добавление одной и той же физической карты в комбинацию дважды должно завершаться исключением")
    void shouldThrowExceptionWhenMeldContainsDuplicatePhysicalCard() {
        Card card = new Card(1, Rank.SEVEN, Suit.HEARTS, null);

        List<Card> cards = List.of(card, new Card(1, Rank.SEVEN, Suit.HEARTS, null), new Card(2, Rank.SEVEN, Suit.DIAMONDS, null));

        assertThrows(IllegalArgumentException.class, () -> new Meld(Rank.SEVEN, cards));
    }

    @Test
    @DisplayName("Изменение исходного списка после создания комбинации не должно менять карты внутри Meld")
    void shouldNotChangeMeldWhenOriginalListIsModified() {
        List<Card> cards = new ArrayList<>(List.of(
                new Card(1, Rank.SEVEN, Suit.HEARTS, null),
                new Card(2, Rank.SEVEN, Suit.DIAMONDS, null),
                new Card(3, Rank.SEVEN, Suit.CLUBS, null)
        ));

        Meld meld = new Meld(Rank.SEVEN, cards);
        cards.clear();

        assertEquals(3, meld.getCards().size());
    }

    @Test
    @DisplayName("Полученный список карт комбинации должен быть неизменяемым для внешнего кода")
    void shouldReturnUnmodifiableCardsList() {
        Meld meld = new Meld(Rank.SEVEN, List.of(
                new Card(1, Rank.SEVEN, Suit.HEARTS, null),
                new Card(2, Rank.SEVEN, Suit.DIAMONDS, null),
                new Card(3, Rank.SEVEN, Suit.CLUBS, null)
        ));

        assertThrows(UnsupportedOperationException.class, () -> meld.getCards().add(new Card(4, Rank.SEVEN, Suit.SPADES, null)));
    }
}
