package canasta.model;

import java.util.*;

/**
 * Представляет игровую колоду из двух стандартных колод с четырьмя джокерами.
 */
public class Deck {
    private final List<Card> cards;

    /**
     * Создаёт и перемешивает колоду.
     *
     * @param random генератор случайных чисел для перемешивания
     * @throws IllegalArgumentException если генератор равен null
     */
    public Deck(Random random) {
        if (random == null) {
            throw new IllegalArgumentException("random cannot be null");
        }

        cards = new ArrayList<>();
        createCards();
        Collections.shuffle(cards, random);
    }

    /**
     * Создаем все карты двух стандартных колод (по 52шт. в каждой) + 4 джокера (по 2 в каждой колоде)
     */
    private void createCards() {
        int id = 0;

        for (int deckNumber = 0; deckNumber < 2; deckNumber++) {
            for (Rank rank : Rank.values()) {
                for (Suit suit : Suit.values()) {
                    if (rank != Rank.JOKER) {
                        cards.add(new Card(id, rank, suit, null));
                        id++;
                    }
                }
            }
            cards.add(new Card(id, Rank.JOKER, null, JokerColor.RED));
            id++;
            cards.add(new Card(id, Rank.JOKER, null, JokerColor.BLACK));
            id++;
        }
    }

    /**
     * Возвращает количество оставшихся карт.
     *
     * @return количество карт в колоде
     */
    public int size() {
        return cards.size();
    }

    /**
     * Проверяет, пуста ли колода.
     *
     * @return true, если колода пуста
     */
    public boolean isEmpty() {
        return cards.isEmpty();
    }

    /**
     * Выдаёт верхнюю карту и удаляет её из колоды.
     *
     * @return выданная карта
     * @throws NoSuchElementException если колода пуста
     */
    public Card drawCard() {
        if (cards.isEmpty()) {
            throw new NoSuchElementException("Deck is empty");
        }

        return cards.remove(cards.size() - 1);
    }

    /**
     * Возвращает неизменяемое представление оставшихся карт.
     *
     * @return список оставшихся карт
     */
    public List<Card> getCards() {
        return List.copyOf(cards);
    }
}
