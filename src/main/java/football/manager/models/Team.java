package football.manager.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Data
@Entity
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @NotNull(message = "Id must not be null.")
    private Long id;

    @Column(name = "name")
    @NotNull(message = "Name must not be null.")
    @Length(max = 255, message = "Name length must be smaller than 255 symbols.")
    private String name;

    @Column(name = "money")
    @NotNull(message = "Money must not be null.")
    @Positive(message = "Money must be a positive number.")
    private Long money;

    @Column(name = "percent")
    @Positive(message = "Percent must be a positive number.")
    private double percent;

    @Column(name = "photo")
    @NotNull(message = "Photo must not be null.")
    @Lob
    private byte[] photo;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "team_id")
    private List<Player> players;

    public @NotNull(message = "Id must not be null.") Long getId() {
        return id;
    }

    public void setId(@NotNull(message = "Id must not be null.") Long id) {
        this.id = id;
    }

    public @NotNull(message = "Name must not be null.") @Length(max = 255, message = "Name length must be smaller than 255 symbols.") String getName() {
        return name;
    }

    public void setName(@NotNull(message = "Name must not be null.") @Length(max = 255, message = "Name length must be smaller than 255 symbols.") String name) {
        this.name = name;
    }

    @Positive(message = "Percent must be a positive number.")
    public double getPercent() {
        return percent;
    }

    public void setPercent(@Positive(message = "Percent must be a positive number.") double percent) {
        this.percent = percent;
    }

    public @NotNull(message = "Money must not be null.") @Positive(message = "Money must be a positive number.") Long getMoney() {
        return money;
    }

    public void setMoney(@NotNull(message = "Money must not be null.") @Positive(message = "Money must be a positive number.") Long money) {
        this.money = money;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public String getPhotoBase64() {
        if (photo == null || photo.length == 0) return null;
        return java.util.Base64.getEncoder().encodeToString(photo);
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    @Override
    public String toString() {
        return "Team{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", money=" + money +
                ", percent=" + percent +
                ", photo='" + photo + '\'' +
                '}';
    }
}
