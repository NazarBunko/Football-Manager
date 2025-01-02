package football.manager.model;

import lombok.Data;

@Data
public class Player {
    private Long id;
    private String name;
    private int age;
    private Position position;
    private int month_experience;
    private Long team_id;
}
