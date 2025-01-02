package football.manager.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class Team {
    @NotNull(message = "Id must not be null.")
    private Long id;

    @NotNull(message = "Name must not be null.")
    @Length(max = 255, message = "Name length must be smaller than 255 symbols.")
    private String name;

    @NotNull(message = "Money must not be null.")
    @Positive(message = "Money must be a positive number.")
    private Long money;

    @Positive(message = "Percent must be a positive number.")
    private double percent;
}
