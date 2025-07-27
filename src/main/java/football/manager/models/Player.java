package football.manager.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @NotNull(message = "Player ID must not be null.")
    private Long id;

    @Column(name = "team_id")
    @NotNull(message = "Team ID must not be null.")
    private Long team_id;

    @Column(name = "name")
    @NotNull(message = "Player name must not be null.")
    @Size(min = 1, max = 100, message = "Player name must be between 1 and 100 characters.")
    private String name;

    @Column(name = "age")
    @NotNull(message = "Player age must not be null.")
    @Positive(message = "Player age must be a positive number.")
    private Integer age;

    @Column(name = "position")
    @NotNull(message = "Player position must not be null.")
    @Size(min = 1, max = 100, message = "Player position must be between 1 and 100 characters.")
    private String position;

    @Column(name = "experience")
    @NotNull(message = "Month experience must not be null.")
    @Positive(message = "Month experience must be a positive number.")
    private Integer experience;

    @Column(name = "photo")
    @NotNull(message = "Photo must not be null.")
    @Lob
    private byte[] photo;

    @ManyToOne
    @JoinColumn(name = "team_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Team team;


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

    public String getPhotoBase64() {
        if (photo == null || photo.length == 0) return null;
        return java.util.Base64.getEncoder().encodeToString(photo);
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    @Override
    public String toString() {
        return "Player{" +
                "age=" + age +
                ", photo='" + photo + '\'' +
                ", experience=" + experience +
                ", position='" + position + '\'' +
                ", name='" + name + '\'' +
                ", team_id=" + team_id +
                ", id=" + id +
                '}';
    }
}
