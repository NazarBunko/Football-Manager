package football.manager.dao;

import football.manager.model.Player;
import football.manager.model.Team;
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
        return jdbcTemplate.query("SELECT * FROM players", new BeanPropertyRowMapper<>(Player.class));
    }

    public boolean add(String name, Long teamId, int age, String position, int monthExperience, String photo) {
        try {
            if (name == null || name.isBlank() || age <= 0 || monthExperience < 0 || position == null || position.isBlank()) {
                return false;
            }

            if (teamId == null || teamId == 0) {
                jdbcTemplate.update(
                        "INSERT INTO players (name, age, position, experience, photo) VALUES (?, ?, ?, ?, ?)",
                        name, age, position, monthExperience, photo
                );
            } else {
                jdbcTemplate.update(
                        "INSERT INTO players (team_id, name, age, position, experience, photo) VALUES (?, ?, ?, ?, ?, ?)",
                        teamId, name, age, position, monthExperience, photo
                );
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean kickPlayer(Long id) {
        try {
            Player player = jdbcTemplate.queryForObject("SELECT * FROM players WHERE id = ?", new BeanPropertyRowMapper<>(Player.class), id);
            if (player == null || player.getTeam_id() == null) {
                return false;
            }
            try {
                jdbcTemplate.update("UPDATE players SET team_id = NULL WHERE id = ?", id);
                return true;
            } catch (Exception e) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public boolean addToTeam(Long id, Long teamId) {
        try {
            Player player = jdbcTemplate.queryForObject("SELECT * FROM players WHERE id = ?", new BeanPropertyRowMapper<>(Player.class), id);
            if (player == null) {
                return false;
            }
            Team newTeam = jdbcTemplate.queryForObject("SELECT * FROM teams WHERE id = ?", new BeanPropertyRowMapper<>(Team.class), teamId);
            if (newTeam == null) {
                return false;
            }
            try {
                jdbcTemplate.update("UPDATE players SET team_id = ? WHERE id = ?", teamId, id);
                return true;
            } catch (Exception e) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public Boolean sellPlayer(Long id, Long newTeamId, Long price) {
        try {
            Player player = jdbcTemplate.queryForObject("SELECT * FROM players WHERE id = ?", new BeanPropertyRowMapper<>(Player.class), id);
            if (player == null) {
                return false;
            }

            Team oldTeam = jdbcTemplate.queryForObject("SELECT * FROM teams WHERE id = ?", new BeanPropertyRowMapper<>(Team.class), player.getTeam_id());
            if (oldTeam == null) {
                return false;
            }

            Team newTeam = jdbcTemplate.queryForObject("SELECT * FROM teams WHERE id = ?", new BeanPropertyRowMapper<>(Team.class), newTeamId);
            if (newTeam == null) {
                return false;
            }

            double percent = oldTeam.getPercent();
            Long resultPrice = (long) (price + (price * (percent / 100)));

            if (resultPrice > newTeam.getMoney()) {
                return false;
            }

            try {
                jdbcTemplate.update("UPDATE teams SET money = ? WHERE id = ?", oldTeam.getMoney() + resultPrice, oldTeam.getId());
                jdbcTemplate.update("UPDATE teams SET money = ? WHERE id = ?", newTeam.getMoney() - resultPrice, newTeamId);
                jdbcTemplate.update("UPDATE players SET team_id = ? WHERE id = ?", newTeamId, id);
                return true;
            } catch (Exception e) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public Player getPlayerById(Long id) {
        return jdbcTemplate.queryForObject("SELECT * FROM players WHERE id = ?", new BeanPropertyRowMapper<>(Player.class), id);
    }

    public boolean delete(Long id) {
        Player player = jdbcTemplate.queryForObject("SELECT * FROM players WHERE id = ?", new BeanPropertyRowMapper<>(Player.class), id);
        if (player == null) {
            return false;
        }
        jdbcTemplate.update("DELETE FROM players WHERE id = ?", id);
        return true;
    }

    public boolean update(String name, int age, String position, int experience, Long teamId, Long id, String photo) {
        try {
            if (name == null || name.isBlank() || age <= 0 || experience < 0 || position == null || position.isBlank() || id == null || id <= 0) {
                return false;
            }

            jdbcTemplate.update(
                    "UPDATE players SET name = ?, age = ?, position = ?, experience = ?, team_id = ?, photo = ? WHERE id = ?",
                    name, age, position, experience, teamId, photo, id
            );

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
