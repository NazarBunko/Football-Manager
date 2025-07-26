package football.manager.repositories;

import football.manager.models.Player;
import football.manager.models.Team;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TeamRepository {

    private static JdbcTemplate jdbcTemplate;

    @Autowired
    public TeamRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public TeamRepository() {}

    public List<Team> index() {
        return jdbcTemplate.query("SELECT * FROM team", new BeanPropertyRowMapper<>(Team.class));
    }

    public boolean add(String name, Long money, double percent, byte[] photo) {
        try {
            jdbcTemplate.update(
                    "INSERT INTO team (name, money, percent, photo) VALUES (?, ?, ?, ?)",
                    name, money, percent, photo
            );
            return true;
        } catch (DataAccessException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(String name, int money, double percent, Long id, byte[] photo) {
        try {
            int rows = jdbcTemplate.update(
                    "UPDATE team SET name = ?, money = ?, percent = ?, photo = ? WHERE id = ?",
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
            return jdbcTemplate.query("SELECT * FROM player WHERE team_id IS NULL", new BeanPropertyRowMapper<>(Player.class));
        }
        return jdbcTemplate.query("SELECT * FROM player WHERE team_id = ?", new BeanPropertyRowMapper<>(Player.class), id);
    }

    public void setPlayersForAllTeams(List<Team> teams) {
        if (teams == null || teams.isEmpty()) {
            throw new IllegalArgumentException("Teams list cannot be null or empty");
        }

        List<Long> teamIds = teams.stream()
                .map(Team::getId)
                .collect(Collectors.toList());

        String sql = "SELECT * FROM player WHERE team_id IN (" +
                teamIds.stream().map(String::valueOf).collect(Collectors.joining(",")) + ")";

        List<Player> players = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Player.class));

        for (Team team : teams) {
            List<Player> teamPlayers = players.stream()
                    .filter(player -> player.getTeam_id().equals(team.getId()))
                    .collect(Collectors.toList());
            team.setPlayers(teamPlayers);
        }
    }


    public Team getTeamById(Long id) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM team WHERE id = ?", new BeanPropertyRowMapper<>(Team.class), id);
        } catch (DataAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean delete(Long id) {
        try {
            jdbcTemplate.update("UPDATE player SET team_id = NULL WHERE team_id = ?", id);
            jdbcTemplate.update("DELETE FROM team WHERE id = ?", id);
            return true;
        } catch (DataAccessException e) {
            e.printStackTrace();
            return false;
        }
    }
}
