package football.manager.dao;

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

    public Long add(Team team) {
        Integer maxId = jdbcTemplate.queryForObject("SELECT COALESCE(MAX(id), 0) FROM team", Integer.class);

        Long newId = (long) ((maxId != null ? maxId : 0) + 1);
        team.setId(newId);

        jdbcTemplate.update("INSERT INTO team (id, name, money, percent) VALUES (?, ?, ?, ?)",
                team.getId(),
                team.getName(),
                team.getMoney(),
                team.getPercent()
        );

        return team.getId();
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

