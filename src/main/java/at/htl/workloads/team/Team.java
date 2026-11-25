package at.htl.workloads.team;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDate;

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

    // TODO (you): add the Team side of the TeamMembership relationship and its synchronization helper.

    public Team() {
    }

    public Team(String name, String tag, LocalDate foundedAt) {
        this.name = name;
        this.tag = tag;
        this.foundedAt = foundedAt;
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
