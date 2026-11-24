
package canasta.model;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Представляет комбинацию карт одного достоинства в игре Канаста.
 * Комбинация может содержать обычные и дикие карты.
 */
public class Meld {

    private final Rank rank;
    private final List<Card> cards;

    /**
     * Создаёт комбинацию карт.
     *
     * @param rank достоинство обычных карт комбинации
     * @param cards список карт комбинации
     * @throws IllegalArgumentException если достоинство или список
     * некорректны, карт меньше трёх либо нарушены правила комбинации
     */
    public Meld(Rank rank, List<Card> cards) {
        if (rank == null) {
            throw new IllegalArgumentException("Rank cannot be null");
        }
        if (rank == Rank.TWO || rank == Rank.THREE || rank == Rank.JOKER) {
            throw new IllegalArgumentException("Invalid meld rank");
        }
        if (cards == null) {
            throw new IllegalArgumentException("Cards cannot be null");
        }
        if (cards.size() < 3) {
            throw new IllegalArgumentException("Meld must contain at least three cards");
        }
        if (cards.stream().anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException("Meld cannot contain null cards");
        }

        this.rank = rank;
        this.cards = List.copyOf(cards);

        validateCardRanks();
        validateWildCards();
        validateSpecialCards();
        validateUniqueCards();
    }

    /**
     * Возвращает достоинство комбинации.
     *
     * @return достоинство обычных карт комбинации
     */
    public Rank getRank() {
        return rank;
    }

    /**
     * Возвращает неизменяемый список карт комбинации.
     *
     * @return список карт
     */
    public List<Card> getCards() {
        return cards;
    }

    /**
     * Проверяет, что все обычные карты имеют достоинство комбинации.
     *
     * @throws IllegalArgumentException если найдена карта другого достоинства
     */
    private void validateCardRanks() {
        for (Card card : cards) {
            if (!card.isWild() && card.getRank() != rank) {
                throw new IllegalArgumentException("Regular cards must have the same rank as the meld");
            }
        }
    }

    /**
     * Проверяет количество диких карт в комбинации.
     *
     * Диких карт не должно быть больше трёх и не должно быть в принципе больше, чем обычных карт.
     *
     * @throws IllegalArgumentException если нарушено ограничение
     */
    private void validateWildCards() {
        int wildCards = 0;
        int regularCards = 0;

        for (Card card : cards) {
            if (card.isWild()) {
                wildCards++;
            } else {
                regularCards++;
            }
        }

        if (wildCards > 3 || wildCards > regularCards) {
            throw new IllegalArgumentException("Invalid number of wild cards");
        }
    }

    /**
     * Проверяет, что тройки не входят в комбинацию.
     *
     * @throws IllegalArgumentException если найдена красная или чёрная тройка
     */
    private void validateSpecialCards() {
        for (Card card : cards) {
            if (card.isRedThree() || card.isBlackThree()) {
                throw new IllegalArgumentException("Threes cannot be included in a meld");
            }
        }
    }

    /**
     * Проверяет отсутствие повторяющихся физических карт.
     *
     * HashSet<> использует проверку карт через equals/hashCode,
     * которые были переопределены в классе Card
     *
     * @throws IllegalArgumentException если одна карта встречается в комбинации несколько раз
     */
    private void validateUniqueCards() {
        Set<Card> uniqueCards = new HashSet<>(cards);

        if (uniqueCards.size() != cards.size()) {
            throw new IllegalArgumentException("Meld cannot contain duplicate cards");
        }
    }
}
