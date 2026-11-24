package canasta.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class DiscardPileTest {

    private Card createCard(int id, Rank rank, Suit suit) {
        return new Card(id, rank, suit, null);
    }

    @Test
    @DisplayName("Новая стопка сброса должна быть пустой и незамороженной")
    void newDiscardPileShouldBeEmptyAndNotFrozen() {
        DiscardPile pile = new DiscardPile();

        assertTrue(pile.isEmpty());
        assertEquals(0, pile.size());
        assertFalse(pile.isFrozen());
    }

    @Test
    @DisplayName("Добавление обычной карты должно помещать её наверх стопки")
    void addCardShouldPlaceCardOnTop() {
        DiscardPile pile = new DiscardPile();
        Card card = createCard(1, Rank.SEVEN, Suit.HEARTS);

        pile.addCard(card);

        assertEquals(1, pile.size());
        assertEquals(card, pile.getTopCard());
        assertFalse(pile.isFrozen());
    }

    @Test
    @DisplayName("Добавление null должно вызывать исключение")
    void addNullCardShouldThrowException() {
        DiscardPile pile = new DiscardPile();

        assertThrows(IllegalArgumentException.class, () -> pile.addCard(null));
    }

    @Test
    @DisplayName("Добавление дикой карты должно замораживать стопку")
    void addWildCardShouldFreezePile() {
        DiscardPile pile = new DiscardPile();
        Card wildCard = createCard(1, Rank.TWO, Suit.HEARTS);

        pile.addCard(wildCard);

        assertTrue(pile.isFrozen());
        assertTrue(pile.isTopCardWild());
    }

    @Test
    @DisplayName("Добавление чёрной тройки должно замораживать стопку")
    void addBlackThreeShouldFreezePile() {
        DiscardPile pile = new DiscardPile();
        Card blackThree = createCard(1, Rank.THREE, Suit.SPADES);

        pile.addCard(blackThree);

        assertTrue(pile.isFrozen());
        assertTrue(pile.isTopCardBlackThree());
    }

    @Test
    @DisplayName("Добавление обычной карты не должно снимать заморозку")
    void addNormalCardShouldNotUnfreezePile() {
        DiscardPile pile = new DiscardPile();
        pile.addCard(createCard(1, Rank.TWO, Suit.CLUBS));

        Card normalCard = createCard(2, Rank.NINE, Suit.CLUBS);
        pile.addCard(normalCard);

        assertTrue(pile.isFrozen());
        assertFalse(pile.isTopCardWild());
        assertEquals(normalCard, pile.getTopCard());
    }

    @Test
    @DisplayName("Добавление списка карт в нижнюю часть стопки должно сохранять порядок карт: от нижней к верхней")
    void addCardsToBottomShouldPreserveOrder() {
        DiscardPile pile = new DiscardPile();
        Card topCard = createCard(1, Rank.NINE, Suit.HEARTS);
        pile.addCard(topCard);

        Card firstCard = createCard(2, Rank.FOUR, Suit.CLUBS);
        Card secondCard = createCard(3, Rank.SIX, Suit.SPADES);
        List<Card> newCards = List.of(firstCard, secondCard);

        pile.addCardsToBottom(newCards);

        assertEquals(List.of(firstCard, secondCard, topCard), pile.getCards());
        assertEquals(topCard, pile.getTopCard());
    }

    @Test
    @DisplayName("Добавление дикой карты снизу должно замораживать стопку")
    void addWildCardToBottomShouldFreezePile() {
        DiscardPile pile = new DiscardPile();
        pile.addCard(createCard(1, Rank.NINE, Suit.HEARTS));

        Card wildCard = createCard(2, Rank.TWO, Suit.SPADES);
        pile.addCardsToBottom(List.of(wildCard));

        assertTrue(pile.isFrozen());
    }

    @Test
    @DisplayName("Добавление списка с null должно вызывать исключение")
    void addCardsToBottomWithNullShouldThrowException() {
        DiscardPile pile = new DiscardPile();

        assertThrows(IllegalArgumentException.class, () -> pile.addCardsToBottom(null));

        List<Card> cardsWithNull = new ArrayList<>();
        cardsWithNull.add(createCard(1, Rank.SEVEN, Suit.HEARTS));
        cardsWithNull.add(null);

        assertThrows(IllegalArgumentException.class, () -> pile.addCardsToBottom(cardsWithNull));
    }

    @Test
    @DisplayName("Получение верхней карты пустой стопки должно вызывать исключение")
    void getTopCardFromEmptyPileShouldThrowException() {
        DiscardPile pile = new DiscardPile();

        assertThrows(NoSuchElementException.class, pile::getTopCard);
    }

    @Test
    @DisplayName("Получение всех карт должно очищать стопку и снимать заморозку")
    void takeAllCardsShouldClearPileAndUnfreezeIt() {
        DiscardPile pile = new DiscardPile();
        Card wildCard = createCard(1, Rank.TWO, Suit.HEARTS);
        Card normalCard = createCard(2, Rank.NINE, Suit.CLUBS);

        pile.addCard(wildCard);
        pile.addCard(normalCard);

        List<Card> takenCards = pile.takeAllCards();

        assertEquals(List.of(wildCard, normalCard), takenCards);
        assertTrue(pile.isEmpty());
        assertEquals(0, pile.size());
        assertFalse(pile.isFrozen());
    }

    @Test
    @DisplayName("Полученный список карт должен быть неизменяемым")
    void takeAllCardsShouldReturnUnmodifiableList() {
        DiscardPile pile = new DiscardPile();
        pile.addCard(createCard(1, Rank.SEVEN, Suit.HEARTS));

        List<Card> takenCards = pile.takeAllCards();

        assertThrows(UnsupportedOperationException.class, () -> takenCards.add(createCard(2, Rank.EIGHT, Suit.CLUBS)));
    }

    @Test
    @DisplayName("Получение списка карт не должно позволять изменять содержимое стопки")
    void getCardsShouldReturnUnmodifiableList() {
        DiscardPile pile = new DiscardPile();
        pile.addCard(createCard(1, Rank.SEVEN, Suit.HEARTS));

        List<Card> cards = pile.getCards();

        assertThrows(UnsupportedOperationException.class, () -> cards.add(createCard(2, Rank.EIGHT, Suit.CLUBS)));
        assertEquals(1, pile.size());
    }

    @Test
    @DisplayName("Принудительная заморозка должна замораживать стопку")
    void freezeShouldSetFrozenState() {
        DiscardPile pile = new DiscardPile();

        pile.freeze();

        assertTrue(pile.isFrozen());
    }
}