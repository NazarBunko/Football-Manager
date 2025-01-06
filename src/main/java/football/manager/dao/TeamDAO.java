package football.manager.dao;

import football.manager.model.Player;
import football.manager.model.Team;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class TeamDAO {

    private static JdbcTemplate jdbcTemplate;

    @Autowired
    public TeamDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public TeamDAO() {}

    public List<Team> index() {
        return jdbcTemplate.query("SELECT * FROM team", new BeanPropertyRowMapper<>(Team.class));
    }

    public boolean add(String name, Long money, double percent) {
        try {
            jdbcTemplate.update(
                    "INSERT INTO team (name, money, percent) VALUES (?, ?, ?)",
                    name, money, percent
            );
            return true;
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

    public boolean update(String name, int money, double percent, Long id) {
        try {
            int rows = jdbcTemplate.update(
                    "UPDATE team SET name = ?, money = ?, percent = ? WHERE id = ?",
                    name, money, percent, id
            );
            return rows > 0;
        } catch (DataAccessException e) {
            e.printStackTrace();
            return false;
        }
    }
}
