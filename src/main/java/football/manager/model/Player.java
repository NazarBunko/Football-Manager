package football.manager.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class Player {
    @NotNull(message = "Player ID must not be null.")
    private Long id;

    @NotNull(message = "Team ID must not be null.")
    private Long team_id;

    @NotNull(message = "Player name must not be null.")
    @Size(min = 1, max = 100, message = "Player name must be between 1 and 100 characters.")
    private String name;

    @NotNull(message = "Player age must not be null.")
    @Positive(message = "Player age must be a positive number.")
    private Integer age;

    @NotNull(message = "Player position must not be null.")
    @Size(min = 1, max = 100, message = "Player position must be between 1 and 100 characters.")
    private String position;

    @NotNull(message = "Month experience must not be null.")
    @Positive(message = "Month experience must be a positive number.")
    private Integer experience;

    public @NotNull(message = "Player ID must not be null.") Long getId() {
        return id;
    }

    public void setId(@NotNull(message = "Player ID must not be null.") Long id) {
        this.id = id;
    }

    public @NotNull(message = "Team ID must not be null.") Long getTeam_id() {
        return team_id;
    }

    public void setTeam_id(@NotNull(message = "Team ID must not be null.") Long team_id) {
        this.team_id = team_id;
    }

    public @NotNull(message = "Player name must not be null.") @Size(min = 1, max = 100, message = "Player name must be between 1 and 100 characters.") String getName() {
        return name;
    }

    public void setName(@NotNull(message = "Player name must not be null.") @Size(min = 1, max = 100, message = "Player name must be between 1 and 100 characters.") String name) {
        this.name = name;
    }

    public @NotNull(message = "Player age must not be null.") @Positive(message = "Player age must be a positive number.") Integer getAge() {
        return age;
    }

    public void setAge(@NotNull(message = "Player age must not be null.") @Positive(message = "Player age must be a positive number.") Integer age) {
        this.age = age;
    }

    public @NotNull(message = "Player position must not be null.") String getPosition() {
        return position;
    }

    public void setPosition(@NotNull(message = "Player position must not be null.") String position) {
        this.position = position;
    }

    public @NotNull(message = "Month experience must not be null.") @Positive(message = "Month experience must be a positive number.") Integer getExperience() {
        return experience;
    }

    public void setExperience(@NotNull(message = "Month experience must not be null.") @Positive(message = "Month experience must be a positive number.") Integer experience) {
        this.experience = experience;
    }
}
