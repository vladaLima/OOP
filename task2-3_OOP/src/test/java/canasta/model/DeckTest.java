package canasta.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class DeckTest {
    @Test
    @DisplayName("Колода должна содержать 108 карт" +
            "Создаем колоду с фиксированным seed = 42")
    void shouldCreateDeckWith108Cards() {
        Deck deck = new Deck(new Random(42));

        assertEquals(108, deck.size());
    }

    @Test
    @DisplayName("Создаем колоду с фиксированным seed = 56" +
            "Проверяем, что если создать еще одну колоду точно с таким же seed," +
            "то карты будут идти в том же самом порядке и в той же последовательности" +
            "Получим 2 колоды с идентичным порядком карт")
    void shouldCreateIdenticalSequenceCards() {
        Deck deck1 = new Deck(new Random(56));
        Deck deck2 = new Deck(new Random(56));

        List<Integer> ids1 = deck1.getCards().stream().map(Card::getId).toList();
        List<Integer> ids2 = deck2.getCards().stream().map(Card::getId).toList();

        assertEquals(ids1, ids2);
    }

    @Test
    @DisplayName("Созданная колода не должна быть пустой")
    void shouldCreateNonEmptyDeck() {
        Deck deck = new Deck(new Random(42));

        assertFalse(deck.isEmpty());
    }

    @Test
    @DisplayName("Все карты должны иметь уникальные ID")
    void shouldHaveUniqueCardIds() {
        Deck deck = new Deck(new Random(42));

        Set<Integer> ids = new HashSet<>();
        for (Card card : deck.getCards()) {
            ids.add(card.getId());
        }

        assertEquals(108, ids.size());
    }

    @Test
    @DisplayName("Колода должна содержать четыре джокера")
    void shouldContainFourJokers() {
        Deck deck = new Deck(new Random(42));

        long jokerCount = deck.getCards().stream().filter(Card::isJoker).count();

        assertEquals(4, jokerCount);
    }

    @Test
    @DisplayName("После взятия карты размер колоды должен уменьшиться на единицу")
    void shouldDecreaseDeckSizeAfterDrawingCard() {
        Deck deck = new Deck(new Random(42));
        int initialSize = deck.size();
        deck.drawCard();

        assertEquals(initialSize - 1, deck.size());
    }

    @Test
    @DisplayName("Метод drawCard должен возвращать карту")
    void shouldReturnCardWhenDrawing() {
        Deck deck = new Deck(new Random(42));
        Card card = deck.drawCard();

        assertNotNull(card);
    }

    @Test
    @DisplayName("При попытке взять карту из пустой колоды должно выбрасываться исключение")
    void shouldThrowExceptionWhenDrawingFromEmptyDeck() {
        Deck deck = new Deck(new Random(42));

        for (int i = 0; i < 108; i++) {
            deck.drawCard();
        }

        assertThrows(NoSuchElementException.class, deck::drawCard);
    }

    @Test
    @DisplayName("После извлечения всех карт колода должна стать пустой")
    void shouldBeEmptyAfterDrawingAllCards() {
        Deck deck = new Deck(new Random(42));

        for (int i = 0; i < 108; i++) {
            deck.drawCard();
        }

        assertTrue(deck.isEmpty());
        assertEquals(0, deck.size());
    }

    @Test
    @DisplayName("Полученный список карт нельзя изменять")
    void shouldReturnUnmodifiableList() {
        Deck deck = new Deck(new Random(42));
        List<Card> cards = deck.getCards();

        assertThrows(UnsupportedOperationException.class, () -> cards.remove(0));
        assertEquals(108, deck.size());
    }

    @Test
    @DisplayName("Взятая карта должна отсутствовать в оставшейся колоде")
    void shouldRemoveDrawnCardFromDeck() {
        Deck deck = new Deck(new Random(42));
        Card drawnCard = deck.drawCard();

        boolean cardExists = deck.getCards().stream().anyMatch(card -> card.getId() == drawnCard.getId());

        assertFalse(cardExists);
    }

    @Test
    @DisplayName("После нескольких взятий размер колоды должен уменьшаться правильно")
    void shouldDecreaseDeckSizeAfterMultipleDraws() {
        Deck deck = new Deck(new Random(42));

        for (int i = 0; i < 5; i++) {
            deck.drawCard();
        }

        assertEquals(103, deck.size());
    }
}
