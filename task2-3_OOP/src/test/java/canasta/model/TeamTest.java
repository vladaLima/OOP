package canasta.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TeamTest {
    @Test
    @DisplayName("Команда успешно создается с двумя игроками")
    void shouldCreateTeam() {
        Player player1 = new Player(1, "Vlada");
        Player player2 = new Player(2, "Varya");

        List<Player> players = Arrays.asList(player1, player2);
        assertEquals(player1, players.get(0));
        assertEquals(player2, players.get(1));
    }

    @Test
    @DisplayName("Проверка на запрет создания команды с отрицательным id")
    void shouldRejectTeamWithNegativeId() {
        Player player1 = new Player(1, "Vlada");
        Player player2 = new Player(2, "Varya");

        List<Player> players = Arrays.asList(player1, player2);
        assertThrows(IllegalArgumentException.class, () -> new Team(-5, players));
    }

    @Test
    @DisplayName("Нельзя передавать null вместо списка игроков")
    void shouldRejectTeamWithNullPlayers() {
        assertThrows(IllegalArgumentException.class, () -> new Team(0, null));
    }

    @Test
    @DisplayName("Нельзя передать список из одного или трех игроков")
    void shouldRejectTeamWithNot2Players() {
        Player player1 = new Player(1, "Vlada");
        Player player2 = new Player(2, "Varya");
        Player player3 = new Player(3, "Dasha");

        List<Player> players = Arrays.asList(player1, player2, player3);
        assertThrows(IllegalArgumentException.class, () -> new Team(5, players));
    }

    @Test
    @DisplayName("Нельзя передать null внутри списка игроков")
    void shouldRejectTeamWithNullPlayer() {
        Player player1 = new Player(1, "Vlada");

        List<Player> players = Arrays.asList(player1, null);
        assertThrows(IllegalArgumentException.class, () -> new Team(5, players));
    }

    @Test
    @DisplayName("Список игроков не может содержать одного и того же игрока дважды")
    void shouldRejectTeamWithSamePlayers() {
        Player player1 = new Player(1, "Vlada");
        Player player2 = new Player(1, "Varya");

        List<Player> players = Arrays.asList(player1, player2);
        assertThrows(IllegalArgumentException.class, () -> new Team(5, players));
    }

    @Test
    @DisplayName("Изменение исходного списка не меняет состав команды")
    void shouldRetainParamountTeam() {
        Player player1 = new Player(1, "Vlada");
        Player player2 = new Player(4, "Varya");

        List<Player> players = new ArrayList<>(Arrays.asList(player1, player2));
        Team team = new Team(5, players);
        players.clear();

        assertEquals(2, team.getPlayers().size());
        assertEquals(player1, team.getPlayers().get(0));
        assertEquals(player2, team.getPlayers().get(1));
    }

}
