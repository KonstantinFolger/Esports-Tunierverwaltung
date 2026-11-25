package at.htl.workloads.tournament;

import java.io.Serializable;
import java.util.Objects;

// TODO (you): mark this key class as @Embeddable.
public class TournamentRegistrationId implements Serializable {

    private Long tournamentId;
    private Long teamId;

    public TournamentRegistrationId() {
    }

    public TournamentRegistrationId(Long tournamentId, Long teamId) {
        this.tournamentId = tournamentId;
        this.teamId = teamId;
    }

    public Long getTournamentId() {
        return tournamentId;
    }

    public void setTournamentId(Long tournamentId) {
        this.tournamentId = tournamentId;
    }

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof TournamentRegistrationId that)) {
            return false;
        }
        return Objects.equals(tournamentId, that.tournamentId)
                && Objects.equals(teamId, that.teamId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tournamentId, teamId);
    }
}
