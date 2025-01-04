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
        return jdbcTemplate.query("SELECT * FROM player", new BeanPropertyRowMapper<>(Player.class));
    }

    public void add(String name, Long teamId, int age, String position, int monthExperience) {
        if (teamId == 0) {
            jdbcTemplate.update(
                    "INSERT INTO player (name, age, position, experience) VALUES (?, ?, ?, ?)",
                    name, age, position, monthExperience
            );
        } else {
            jdbcTemplate.update(
                    "INSERT INTO player (team_id, name, age, position, experience) VALUES (?, ?, ?, ?, ?)",
                    teamId, name, age, position, monthExperience
            );
        }
    }


    public void kickPlayer(Long id) {
        jdbcTemplate.update("UPDATE player SET team_id = NULL WHERE id = ?", id);
    }

    public void addToTeam(Long id, Long teamId){
        jdbcTemplate.update("UPDATE player SET team_id = ? WHERE id = ?", teamId, id);
    }

    public Boolean sellPlayer(Long id, Long newTeamId, Long price) {
        try {
            // Отримуємо гравця
            Player player = jdbcTemplate.queryForObject("SELECT * FROM player WHERE id = ?", new BeanPropertyRowMapper<>(Player.class), id);
            if (player == null) {
                return false;  // Гравець не знайдений
            }

            // Отримуємо стару команду
            Team oldTeam = jdbcTemplate.queryForObject("SELECT * FROM team WHERE id = ?", new BeanPropertyRowMapper<>(Team.class), player.getTeam_id());
            if (oldTeam == null) {
                return false;  // Стара команда не знайдена
            }

            // Отримуємо нову команду
            Team newTeam = jdbcTemplate.queryForObject("SELECT * FROM team WHERE id = ?", new BeanPropertyRowMapper<>(Team.class), newTeamId);
            if (newTeam == null) {
                return false;  // Нова команда не знайдена
            }

            double percent = oldTeam.getPercent();
            Long resultPrice = (long) (price + (price * (percent / 100)));  // Розраховуємо результат

            // Перевіряємо, чи достатньо грошей у нової команди
            if (resultPrice > newTeam.getMoney()) {
                return false;  // Якщо у нової команди недостатньо грошей
            }

            // Починаємо транзакцію
            jdbcTemplate.execute("BEGIN");

            try {
                // Оновлюємо гроші старої команди
                jdbcTemplate.update("UPDATE team SET money = ? WHERE id = ?", oldTeam.getMoney() + resultPrice, oldTeam.getId());

                // Оновлюємо гроші нової команди
                jdbcTemplate.update("UPDATE team SET money = ? WHERE id = ?", newTeam.getMoney() - resultPrice, newTeamId);

                // Оновлюємо команду гравця
                jdbcTemplate.update("UPDATE player SET team_id = ? WHERE id = ?", newTeamId, id);

                // Комітимо транзакцію
                jdbcTemplate.execute("COMMIT");
                return true;  // Все пройшло успішно
            } catch (Exception e) {
                // Якщо сталася помилка, відкотимо транзакцію
                jdbcTemplate.execute("ROLLBACK");
                return false;  // Виникла помилка, операції не були виконані
            }
        } catch (Exception e) {
            // Обробка загальних помилок
            return false;
        }
    }


    public Player getPlayerById(Long id) {
        return jdbcTemplate.queryForObject("SELECT * FROM player WHERE id = ?", new BeanPropertyRowMapper<>(Player.class), id);
    }

    public void delete(Long id) {
        jdbcTemplate.update("DELETE FROM player WHERE id = ?", id);
    }

    public void update(Long id, Player updatedPlayer) {
        jdbcTemplate.update("UPDATE users SET team_id = ?, name = ?, age = ?, position = ?, experience = ? WHERE id = ?",
                updatedPlayer.getTeam_id(),
                updatedPlayer.getName(),
                updatedPlayer.getAge(),
                updatedPlayer.getPosition(),
                updatedPlayer.getMonthExperience(),
                id
        );
    }
}
