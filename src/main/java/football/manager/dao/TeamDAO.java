package football.manager.dao;

import football.manager.model.Player;
import football.manager.model.Team;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
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

    public void add(String name, Long money, double percent) {
        jdbcTemplate.update(
                "INSERT INTO team (name, money, percent) VALUES (?, ?, ?)",
                name, money, percent
        );
    }


    public List<Player> getPlayers(Long id) {
        if (id == null) {
            return jdbcTemplate.query("SELECT * FROM player WHERE team_id IS NULL", new BeanPropertyRowMapper<>(Player.class));
        }
        return jdbcTemplate.query("SELECT * FROM player WHERE team_id = ?", new BeanPropertyRowMapper<>(Player.class), id);
    }

    public Team getTeamById(Long id) {
        return jdbcTemplate.queryForObject("SELECT * FROM team WHERE id = ?", new BeanPropertyRowMapper<>(Team.class), id);
    }

    public void delete(Long id) {
        jdbcTemplate.update("DELETE FROM team WHERE id = ?", id);
        jdbcTemplate.update("UPDATE player SET team_id = NULL WHERE team_id = ?", id);
    }

    public void update(Long id, Team updatedTeam) {
        jdbcTemplate.update("UPDATE team SET name = ?, money = ?, percent = ? WHERE id = ?",
                updatedTeam.getName(),
                updatedTeam.getMoney(),
                updatedTeam.getPercent(),
                id
        );
    }
}

