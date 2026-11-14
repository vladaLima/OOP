package canasta.model;

import java.util.List;

/**
 * Представляет команду игроков (2 игрока в команде) в игре Канаста.
 */
public class Team {
    private final int id;
    private final List<Player> players;
    private int score;

    /**
     * Создаёт команду из двух игроков.
     *
     * @param id идентификатор команды
     * @param players список из двух игроков
     * @throws IllegalArgumentException если ID отрицательный,
     * список null, содержит не двух игроков или null
     */
    public Team(int id, List<Player> players) {
        if (id < 0) {
            throw new IllegalArgumentException("Team id cannot be negative");
        }
        if (players == null) {
            throw new IllegalArgumentException("Players cannot be null");
        }
        if (players.size() != 2) {
            throw new IllegalArgumentException("Team must have exactly two players");
        }
        if (players.contains(null)) {
            throw new IllegalArgumentException("Player cannot be null");
        }
        if (players.get(0).equals(players.get(1))) {
            throw new IllegalArgumentException("Team cannot contain the same player twice");
        }

        this.id = id;
        this.players = List.copyOf(players);
        this.score = 0;
    }

    /**
     * Возвращает идентификатор команды.
     *
     * @return идентификатор команды
     */
    public int getId() {
        return id;
    }

    /**
     * Возвращает неизменяемый список игроков команды.
     *
     * @return список игроков команды
     */
    public List<Player> getPlayers() {
        return List.copyOf(players);
    }

    /**
     * Возвращает количество очков команды.
     *
     * @return количество очков
     */
    public int getScore() {
        return score;
    }

}
