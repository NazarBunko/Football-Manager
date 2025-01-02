package football.manager.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class Team {
    private Long id;
    private String name;
    private Long money;
    private double percent;
}
