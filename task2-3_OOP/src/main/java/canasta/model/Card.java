package canasta.model;

import java.util.Objects;

/**
 * Представляет одну физическую игральную карту.
 */
public class Card {
    private final int id;
    private final Rank rank;
    private final Suit suit;
    private final JokerColor jokerColor;

    /**
     * Создаёт карту.
     *
     * @param id уникальный идентификатор карты
     * @param rank достоинство карты
     * @param suit масть карты (null для джокера)
     * @param jokerColor цвет джокера
     * @throws IllegalArgumentException если id отрицательный,
     * ранг null, джокер имеет масть, натуральные карты не имеют масти
     */
    public Card(int id, Rank rank, Suit suit, JokerColor jokerColor) {
        if (id < 0) {
            throw new IllegalArgumentException("Card id cannot be negative");
        }
        if (rank == null) {
            throw new IllegalArgumentException("Card rank cannot be null");
        }
        if (rank == Rank.JOKER) {
            if (suit != null || jokerColor == null) {
                throw new IllegalArgumentException("Joker must have a color and cannot have a suit");
            }
        } else {
            if (suit == null || jokerColor != null) {
                throw new IllegalArgumentException("Regular card must have a suit and cannot have a joker color");
            }
        }

        this.id = id;
        this.suit = suit;
        this.rank = rank;
        this.jokerColor = jokerColor;
    }

    /**
     * Возвращает уникальный номер карты.
     * Если в первой колоде есть семерка червей и во второй колоде есть такая же семерка червей,
     * то это физически разные карты
     *
     * @return номер карты
     */
    public int getId() {
        return id;
    }

    /**
     * Возвращает масть карты.
     *
     * @return масть карты
     */
    public Suit getSuit() {
        return suit;
    }

    /**
     * Возвращает достоинство карты.
     *
     * @return достоинство карты
     */
    public Rank getRank() {
        return rank;
    }

    /**
     * Возвращает цвет джокера.
     *
     * @return цвет джокера
     */
    public JokerColor getJokerColor() {
        return jokerColor;
    }

    /**
     * Проверяет, является ли карта джокером.
     * @return true, если карта джокер, иначе false
     */
    public boolean isJoker() {
        return rank == Rank.JOKER;
    }

    /**
     * Проверяет, является ли карта красной тройкой.
     * @return true, если карта красная тройка, иначе false
     */
    public boolean isRedThree() {
        return rank == Rank.THREE && (suit == Suit.HEARTS || suit == Suit.DIAMONDS);
    }

    /**
     * Проверяет, является ли карта чёрной тройкой.
     * @return true, если карта черная тройка, иначе false
     */
    public boolean isBlackThree() {
        return rank == Rank.THREE && (suit == Suit.CLUBS || suit == Suit.SPADES);
    }

    /**
     * Проверяет, является ли карта дикой.
     * Дикими картами являются двойки и джокеры.
     *
     * @return true, если карта дикая, иначе false
     */
    public boolean isWild() {
        return rank == Rank.TWO || rank == Rank.JOKER;
    }

    /**
     * Сравнивает карты по id, рангу, масти, цвету джокера.
     *
     * @param obj объект для сравнения
     * @return true, если все характеристики игроков совпадают
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Card card = (Card) obj;

        return id == card.id && rank == card.rank && suit == card.suit && jokerColor == card.jokerColor;
    }

    /**
     * Вычисляет хеш-код карты на основе её id, ранга, масти, цвета джокера.
     *
     * @return хеш-код карты
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, rank, suit, jokerColor);
    }

}
