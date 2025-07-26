package football.manager.repositories;

import football.manager.models.Player;
import football.manager.models.Team;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PlayerRepository {

    private static JdbcTemplate jdbcTemplate;

    @Autowired
    public PlayerRepository(JdbcTemplate jdbcTemplate) {
        PlayerRepository.jdbcTemplate = jdbcTemplate;
    }

    public PlayerRepository() {}

    public List<Player> index() {
        return jdbcTemplate.query("SELECT * FROM player", new BeanPropertyRowMapper<>(Player.class));
    }

    public boolean add(String name, Long teamId, int age, String position, int monthExperience, byte[] photo) {
        try {
            if (name == null || name.isBlank() || age <= 0 || monthExperience < 0 || position == null || position.isBlank()) {
                return false;
            }

            if (teamId == null || teamId == 0) {
                jdbcTemplate.update(
                        "INSERT INTO player (name, age, position, experience, photo) VALUES (?, ?, ?, ?, ?)",
                        name, age, position, monthExperience, photo
                );
            } else {
                jdbcTemplate.update(
                        "INSERT INTO player (team_id, name, age, position, experience, photo) VALUES (?, ?, ?, ?, ?, ?)",
                        teamId, name, age, position, monthExperience, photo
                );
            }

            return true;
        } catch (DataAccessException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean kickPlayer(Long id) {
        try {
            Player player = jdbcTemplate.queryForObject(
                    "SELECT * FROM player WHERE id = ?",
                    new BeanPropertyRowMapper<>(Player.class),
                    id
            );
            if (player == null || player.getTeam_id() == null) {
                System.out.println("No player found with id " + id + " or player has no team.");
                return false;
            }

            jdbcTemplate.update("UPDATE player SET team_id = NULL WHERE id = ?", id);
            System.out.println("Player " + id + " released from team.");
            return true;

        } catch (DataAccessException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean addToTeam(Long playerId, Long teamId) {
        try {
            Player player = jdbcTemplate.queryForObject(
                    "SELECT * FROM player WHERE id = ?",
                    new BeanPropertyRowMapper<>(Player.class),
                    playerId
            );
            if (player == null) return false;

            Team team = jdbcTemplate.queryForObject(
                    "SELECT * FROM team WHERE id = ?",
                    new BeanPropertyRowMapper<>(Team.class),
                    teamId
            );
            if (team == null) return false;

            jdbcTemplate.update("UPDATE player SET team_id = ? WHERE id = ?", teamId, playerId);
            return true;
        } catch (DataAccessException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean sellPlayer(Long playerId, Long newTeamId, Long price) {
        try {
            Player player = jdbcTemplate.queryForObject(
                    "SELECT * FROM player WHERE id = ?",
                    new BeanPropertyRowMapper<>(Player.class),
                    playerId
            );
            if (player == null) return false;

            Team oldTeam = jdbcTemplate.queryForObject(
                    "SELECT * FROM team WHERE id = ?",
                    new BeanPropertyRowMapper<>(Team.class),
                    player.getTeam_id()
            );
            if (oldTeam == null) return false;

            Team newTeam = jdbcTemplate.queryForObject(
                    "SELECT * FROM team WHERE id = ?",
                    new BeanPropertyRowMapper<>(Team.class),
                    newTeamId
            );
            if (newTeam == null) return false;

            double percent = oldTeam.getPercent();
            long resultPrice = (long) (price + (price * (percent / 100)));

            if (resultPrice > newTeam.getMoney()) {
                return false;
            }

            jdbcTemplate.update("UPDATE team SET money = ? WHERE id = ?", oldTeam.getMoney() + resultPrice, oldTeam.getId());
            jdbcTemplate.update("UPDATE team SET money = ? WHERE id = ?", newTeam.getMoney() - resultPrice, newTeamId);
            jdbcTemplate.update("UPDATE player SET team_id = ? WHERE id = ?", newTeamId, playerId);
            return true;

        } catch (DataAccessException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Player getPlayerById(Long id) {
        try {
            return jdbcTemplate.queryForObject(
                    "SELECT * FROM player WHERE id = ?",
                    new BeanPropertyRowMapper<>(Player.class),
                    id
            );
        } catch (DataAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean delete(Long id) {
        try {
            Player player = getPlayerById(id);
            if (player == null) return false;

            jdbcTemplate.update("DELETE FROM player WHERE id = ?", id);
            return true;

        } catch (DataAccessException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Оновлення гравця.
     * Якщо photo == null, то фото не оновлюється.
     */
    public boolean update(String name, int age, String position, int experience, Long teamId, Long id, byte[] photo) {
        try {
            if (name == null || name.isBlank() || age <= 0 || experience < 0 || position == null || position.isBlank() || id == null || id <= 0) {
                return false;
            }

            if (photo != null) {
                jdbcTemplate.update(
                        "UPDATE player SET name = ?, age = ?, position = ?, experience = ?, team_id = ?, photo = ? WHERE id = ?",
                        name, age, position, experience, teamId, photo, id
                );
            } else {
                jdbcTemplate.update(
                        "UPDATE player SET name = ?, age = ?, position = ?, experience = ?, team_id = ? WHERE id = ?",
                        name, age, position, experience, teamId, id
                );
            }

            return true;
        } catch (DataAccessException e) {
            e.printStackTrace();
            return false;
        }
    }
}
