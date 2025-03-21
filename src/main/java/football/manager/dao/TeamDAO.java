package football.manager.dao;

import football.manager.model.Player;
import football.manager.model.Team;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TeamDAO {

    private static JdbcTemplate jdbcTemplate;

    @Autowired
    public TeamDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public TeamDAO() {}

    public List<Team> index() {
        return jdbcTemplate.query("SELECT * FROM teams", new BeanPropertyRowMapper<>(Team.class));
    }

    public boolean add(String name, Long money, double percent, String photo) {
        try {
            jdbcTemplate.update(
                    "INSERT INTO teams (name, money, percent, photo) VALUES (?, ?, ?, ?)",
                    name, money, percent, photo
            );
            return true;
        } catch (DataAccessException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(String name, int money, double percent, Long id, String photo) {
        try {
            int rows = jdbcTemplate.update(
                    "UPDATE teams SET name = ?, money = ?, percent = ?, photo = ? WHERE id = ?",
                    name, money, percent, photo, id
            );
            return rows > 0;
        } catch (DataAccessException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Player> getPlayers(Long id) {
        if (id == null) {
            return jdbcTemplate.query("SELECT * FROM players WHERE team_id IS NULL", new BeanPropertyRowMapper<>(Player.class));
        }
        return jdbcTemplate.query("SELECT * FROM players WHERE team_id = ?", new BeanPropertyRowMapper<>(Player.class), id);
    }

    public void setPlayersForAllTeams(List<Team> teams) {
        if (teams == null || teams.isEmpty()) {
            throw new IllegalArgumentException("Teams list cannot be null or empty");
        }

        // Отримуємо всі ідентифікатори команд
        List<Long> teamIds = teams.stream()
                .map(Team::getId)
                .collect(Collectors.toList());

        // Отримуємо всіх гравців для цих команд за один запит
        String sql = "SELECT * FROM players WHERE team_id IN (" +
                teamIds.stream().map(String::valueOf).collect(Collectors.joining(",")) + ")";

        List<Player> players = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Player.class));

        // Розподіляємо гравців по командам
        for (Team team : teams) {
            List<Player> teamPlayers = players.stream()
                    .filter(player -> player.getTeam_id().equals(team.getId()))
                    .collect(Collectors.toList());
            team.setPlayers(teamPlayers);
        }
    }


    public Team getTeamById(Long id) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM teams WHERE id = ?", new BeanPropertyRowMapper<>(Team.class), id);
        } catch (DataAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean delete(Long id) {
        try {
            jdbcTemplate.update("UPDATE players SET team_id = NULL WHERE team_id = ?", id);
            jdbcTemplate.update("DELETE FROM teams WHERE id = ?", id);
            return true;
        } catch (DataAccessException e) {
            e.printStackTrace();
            return false;
        }
    }
}
