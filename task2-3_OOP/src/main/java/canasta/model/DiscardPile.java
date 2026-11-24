
package canasta.model;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Представляет стопку сброшенных карт в игре Канаста.
 * Хранит карты в порядке их расположения и отслеживает состояние заморозки.
 */
public class DiscardPile {
    private final List<Card> cards;
    private boolean frozen;

    /**
     * Создаёт пустую стопку сброса.
     */
    public DiscardPile() {
        this.cards = new ArrayList<>();
        this.frozen = false;
    }

    /**
     * Добавляет карту на верх стопки сброса.
     * Если добавленная карта является дикой картой или чёрной тройкой, то стопка становится замороженной.
     * Добавление обычной карты сверху не снимает ранее установленную заморозку, она лишь меняет правила взятия всей стопки.
     * Это особенно важно в самом начале игры, когда диллер вдруг вынул для стопки сброса самую первую дикую карту или черную тройку,
     * в этом случае мы будем вынимать дикие карты пока не дойдем до натуральной.
     * Тогда получится, что сверху у нас будет лежать натуральная карта, а внизу дикие и этот сброс будет заморожен.
     * Чтобы снять заморозку полностью, надо будет забраться ВСЮ стопку.
     *
     * @param card карта для добавления
     * @throws IllegalArgumentException если карта равна null
     */
    public void addCard(Card card) {
        if (card == null) {
            throw new IllegalArgumentException("Card cannot be null");
        }

        cards.add(card);

        if (card.isWild() || card.isBlackThree()) {
            frozen = true;
        }
    }

    /**
     * Добавляет карты в нижнюю часть стопки.
     * Используется, в частности, при первоначальном формировании сброса,
     * когда дикие карты или черные тройки откладываются под первую обычную карту.
     *
     * Порядок карт в переданном списке сохраняется.
     * Если среди добавленных карт есть дикая карта или чёрная тройка,
     * стопка становится замороженной.
     *
     * @param newCards карты для добавления в нижнюю часть стопки
     * @throws IllegalArgumentException если список равен null или содержит null
     */
    public void addCardsToBottom(List<Card> newCards) {
        if (newCards == null) {
            throw new IllegalArgumentException("Cards cannot be null");
        }

        if (newCards.stream().anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException("Cards cannot contain null");
        }

        cards.addAll(0, newCards);

        for (Card card : newCards) {
            if (card.isWild() || card.isBlackThree()) {
                frozen = true;
                break;
            }
        }
    }

    /**
     * Возвращает верхнюю карту стопки без её удаления.
     *
     * @return верхняя карта стопки
     * @throws NoSuchElementException если стопка пуста
     */
    public Card getTopCard() {
        if (cards.isEmpty()) {
            throw new NoSuchElementException("Discard pile is empty");
        }

        return cards.get(cards.size() - 1);
    }

    /**
     * Возвращает неизменяемую копию всех карт стопки.
     *
     * @return список карт от нижней к верхней
     */
    public List<Card> getCards() {
        return List.copyOf(cards);
    }

    /**
     * Забирает все карты из стопки и очищает её.
     * После успешного забора стопка перестаёт быть замороженной.
     *
     * Проверка права игрока на забор стопки выполняется игровым механизмом, а не этим классом.
     *
     * Т.к. DiscardPile НЕ знает,
     * какой сейчас ход, какой конкретно игрок хочет взять сброс, какие карты у него на руке, какой раунд и т.д.,
     * то в методе takeAllCards() нельзя просто прописать правило,
     * запрещающее брать карты сброса, если сверху лежит черная тройка.
     * Вместо этого делаем метод, работающий только внутри данного пакета,
     * поэтому извне пользователь не сможет взять карты, если сверху лежит черная тройка
     *
     * @return неизменяемый список забранных карт
     */
    List<Card> takeAllCards() {
        List<Card> takenCards = List.copyOf(cards);

        cards.clear();
        frozen = false;

        return takenCards;
    }

    /**
     * Возвращает количество карт в стопке.
     *
     * @return количество карт
     */
    public int size() {
        return cards.size();
    }

    /**
     * Проверяет, пуста ли стопка.
     *
     * @return true, если стопка пуста
     */
    public boolean isEmpty() {
        return cards.isEmpty();
    }

    /**
     * Проверяет, заморожена ли стопка.
     *
     * @return true, если стопка заморожена
     */
    public boolean isFrozen() {
        return frozen;
    }

    /**
     * Замораживает стопку сброса.
     */
    public void freeze() {
        frozen = true;
    }

    /**
     * Проверяет, является ли верхняя карта чёрной тройкой.
     *
     * Чёрная тройка полностью запрещает забор стопки, пока она остаётся её верхней картой.
     *
     * @return true, если верхняя карта является чёрной тройкой
     */
    public boolean isTopCardBlackThree() {
        return !cards.isEmpty() && getTopCard().isBlackThree();
    }

    /**
     * Проверяет, является ли верхняя карта дикой.
     *
     * @return true, если сверху лежит джокер или двойка
     */
    public boolean isTopCardWild() {
        return !cards.isEmpty() && getTopCard().isWild();
    }
}
