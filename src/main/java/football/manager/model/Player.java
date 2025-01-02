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
    private Position position;

    @NotNull(message = "Month experience must not be null.")
    @Positive(message = "Month experience must be a positive number.")
    private Integer monthExperience;
}
