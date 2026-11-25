package at.htl.workloads.person;

import jakarta.persistence.Column;
import at.htl.workloads.team.TeamMembership;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Player extends Person {

    @Column(nullable = false, length = 40)
    private String nickname;

    @Column(nullable = false, length = 2)
    private String country;

    @Column(nullable = false)
    private LocalDate birthDate;

    @OneToMany(mappedBy = "player")
    private List<TeamMembership> memberships = new ArrayList<>();

    public Player() {
    }

    public Player(String firstName, String lastName, String email, String nickname, String country,
                  LocalDate birthDate) {
        super(firstName, lastName, email);
        this.nickname = nickname;
        this.country = country;
        this.birthDate = birthDate;
    }

    public List<TeamMembership> getMemberships() {
        return memberships;
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
