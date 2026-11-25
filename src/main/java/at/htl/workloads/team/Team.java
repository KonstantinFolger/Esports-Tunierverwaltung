package at.htl.workloads.team;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import at.htl.workloads.person.Player;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120, unique = true)
    private String name;

    @Column(nullable = false, length = 8, unique = true)
    private String tag;

    @Column(nullable = false)
    private LocalDate foundedAt;

    @OneToMany(mappedBy = "team")
    private List<TeamMembership> memberships = new ArrayList<>();

    public Team() {
    }

    public Team(String name, String tag, LocalDate foundedAt) {
        this.name = name;
        this.tag = tag;
        this.foundedAt = foundedAt;
    }

    /**
     * Connects a player to this team and keeps both sides of the relationship in sync.
     */
    public void addMembership(TeamMembership membership, Player player) {
        membership.setTeam(this);
        membership.setPlayer(player);
        memberships.add(membership);
        player.getMemberships().add(membership);
    }

    public List<TeamMembership> getMemberships() {
        return memberships;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public LocalDate getFoundedAt() {
        return foundedAt;
    }

    public void setFoundedAt(LocalDate foundedAt) {
        this.foundedAt = foundedAt;
    }
}
