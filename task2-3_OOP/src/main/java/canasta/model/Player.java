package canasta.model;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class Player {
    private final int id;
    private final String name;
    private final List<Card> hand;

    /**
     * Представляет игрока в игре Канаста.
     * Игрок управляет картами в своей руке.
     */
    public Player(int id, String name) {
        if (id < 0) {
            throw new IllegalArgumentException("Player id cannot be negative");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Player name cannot be null or blank");
        }

        this.id = id;
        this.name = name;
        this.hand = new ArrayList<>();
    }

    /**
     * Возвращает идентификатор игрока.
     *
     * @return идентификатор игрока
     */
    public int getId() {
        return id;
    }

    /**
     * Возвращает имя игрока.
     *
     * @return имя игрока
     */
    public String getName() {
        return name;
    }

    /**
     * Возвращает неизменяемый список карт игрока
     *
     * @return список карт на руке игрока
     */
    public List<Card> getHand() {
        return List.copyOf(hand);
    }

    /**
     * Добавляет карту в руку игрока.
     *
     * @param card карта для добавления
     * @throws IllegalArgumentException если карта равна null
     */
    public void addCard(Card card) {
        if (card == null) {
            throw new IllegalArgumentException("Card cannot be null");
        }

        hand.add(card);
    }

    /**
     * Удаляет карту из руки игрока.
     *
     * @param card карта для удаления
     * @throws IllegalArgumentException если карта равна null
     * @throws NoSuchElementException если карта отсутствует в руке
     */
    public void removeCard(Card card) {
        if (card == null) {
            throw new IllegalArgumentException("Card cannot be null");
        }
        if (!hand.contains(card)) {
            throw new NoSuchElementException("Card is not in player's hand");
        }

        hand.remove(card);
    }

    /**
     * Возвращает количество карт на руке игрока.
     *
     * @return количество карт
     */
    public int getHandSize() {
        return hand.size();
    }

    /**
     * Проверяет, пуста ли рука игрока.
     *
     * @return true, если рука пуста
     */
    public boolean isHandEmpty() {
        return hand.isEmpty();
    }


}
