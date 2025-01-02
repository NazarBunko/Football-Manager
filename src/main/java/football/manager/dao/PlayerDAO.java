package football.manager.dao;

import football.manager.model.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PlayerDAO {
    static JdbcTemplate jdbcTemplate;

    @Autowired
    public PlayerDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public PlayerDAO() {}

    public List<Player> index() {
        return jdbcTemplate.query("SELECT * FROM player", new BeanPropertyRowMapper<>(Player.class));
    }

    public Long add(Player player) {
        Integer maxId = jdbcTemplate.queryForObject("SELECT COALESCE(MAX(id), 0) FROM player", Integer.class);

        Long newId = (long) ((maxId != null ? maxId : 0) + 1);
        player.setId(newId);

        jdbcTemplate.update("INSERT INTO users (id, team_id, name, age, position, month_experience) VALUES (?, ?, ?, ?, ?, ?)",
                player.getId(),
                player.getTeam_id(),
                player.getName(),
                player.getAge(),
                player.getPosition(),
                player.getMonthExperience()
        );

        return player.getId();
    }

    public Player show(Long id) {
        return jdbcTemplate.queryForObject("SELECT * FROM player WHERE id = ?", new BeanPropertyRowMapper<>(Player.class), id);
    }

    public void delete(Long id) {
        jdbcTemplate.update("DELETE FROM player WHERE id = ?", id);
    }

    public void update(Long id, Player updatedPlayer) {
        jdbcTemplate.update("UPDATE users SET team_id = ?, name = ?, age = ?, position = ?, month_experience = ? WHERE id = ?",
                updatedPlayer.getTeam_id(),
                updatedPlayer.getName(),
                updatedPlayer.getAge(),
                updatedPlayer.getPosition(),
                updatedPlayer.getMonthExperience(),
                id
        );
    }
}
