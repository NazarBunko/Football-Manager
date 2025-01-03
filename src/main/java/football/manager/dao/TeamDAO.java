package football.manager.dao;

import football.manager.model.Player;
import football.manager.model.Team;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.Statement;
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

    public Long add(Team team) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO team (name, money, percent) VALUES (?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, team.getName());
            ps.setLong(2, team.getMoney());
            ps.setDouble(3, team.getPercent());
            return ps;
        }, keyHolder);

        Long generatedId = keyHolder.getKey().longValue();
        team.setId(generatedId);

        return generatedId;
    }

    public List<Player> getPlayers(Long id) {
        return jdbcTemplate.query("SELECT * FROM player WHERE team_id = ?", new BeanPropertyRowMapper<>(Player.class), id);
    }


    public Team show(Long id) {
        return jdbcTemplate.queryForObject("SELECT * FROM team WHERE id = ?", new BeanPropertyRowMapper<>(Team.class), id);
    }

    public void delete(Long id) {
        jdbcTemplate.update("DELETE FROM team WHERE id = ?", id);
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

