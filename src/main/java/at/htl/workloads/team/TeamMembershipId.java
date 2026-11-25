package at.htl.workloads.team;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class TeamMembershipId implements Serializable {

    private Long teamId;
    private Long playerId;

    public TeamMembershipId() {
    }

    public TeamMembershipId(Long teamId, Long playerId) {
        this.teamId = teamId;
        this.playerId = playerId;
    }

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof TeamMembershipId that)) {
            return false;
        }
        return Objects.equals(teamId, that.teamId)
                && Objects.equals(playerId, that.playerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(teamId, playerId);
    }
}
