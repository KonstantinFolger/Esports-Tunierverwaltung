package at.htl.workloads.team;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import java.time.LocalDate;

@Entity
public class TeamMembership {

    // TODO (you): map this key with @EmbeddedId and map the Team/Player owning relationships with @MapsId.
    private TeamMembershipId id;

    @Column(nullable = false)
    private LocalDate joinedAt;

    private LocalDate leftAt;

    // TODO (you): choose how MembershipRole is stored with @Enumerated.
    @Column(nullable = false)
    private MembershipRole role;

    public TeamMembership() {
    }

    public TeamMembership(TeamMembershipId id, LocalDate joinedAt, LocalDate leftAt, MembershipRole role) {
        this.id = id;
        this.joinedAt = joinedAt;
        this.leftAt = leftAt;
        this.role = role;
    }

    public TeamMembershipId getId() {
        return id;
    }

    public void setId(TeamMembershipId id) {
        this.id = id;
    }

    public LocalDate getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(LocalDate joinedAt) {
        this.joinedAt = joinedAt;
    }

    public LocalDate getLeftAt() {
        return leftAt;
    }

    public void setLeftAt(LocalDate leftAt) {
        this.leftAt = leftAt;
    }

    public MembershipRole getRole() {
        return role;
    }

    public void setRole(MembershipRole role) {
        this.role = role;
    }
}
