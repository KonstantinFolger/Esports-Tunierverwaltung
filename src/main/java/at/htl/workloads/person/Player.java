package at.htl.workloads.person;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import java.time.LocalDate;

@Entity
public class Player extends Person {

    @Column(nullable = false, length = 40)
    private String nickname;

    @Column(nullable = false, length = 2)
    private String country;

    @Column(nullable = false)
    private LocalDate birthDate;

    // TODO (you): add the Player side of the TeamMembership relationship here.

    public Player() {
    }

    public Player(String firstName, String lastName, String email, String nickname, String country,
                  LocalDate birthDate) {
        super(firstName, lastName, email);
        this.nickname = nickname;
        this.country = country;
        this.birthDate = birthDate;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }
}
