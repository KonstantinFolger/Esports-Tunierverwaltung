package at.htl.workloads.tournament;

import jakarta.persistence.Column;
import at.htl.workloads.team.Team;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;

import java.time.LocalDateTime;

@Entity
public class TournamentRegistration {

    @EmbeddedId
    private TournamentRegistrationId id = new TournamentRegistrationId();

    @MapsId("tournamentId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tournament_id")
    private Tournament tournament;

    @MapsId("teamId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "team_id")
    private Team team;

    @Column(nullable = false)
    private LocalDateTime registeredAt;

    private Integer seed;

    public TournamentRegistration() {
    }

    public TournamentRegistration(TournamentRegistrationId id, LocalDateTime registeredAt, Integer seed) {
        this.id = id;
        this.registeredAt = registeredAt;
        this.seed = seed;
    }

    public TournamentRegistrationId getId() {
        return id;
    }

    public void setId(TournamentRegistrationId id) {
        this.id = id;
    }

    public Tournament getTournament() {
        return tournament;
    }

    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public LocalDateTime getRegisteredAt() {
        return registeredAt;
    }

    public void setRegisteredAt(LocalDateTime registeredAt) {
        this.registeredAt = registeredAt;
    }

    public Integer getSeed() {
        return seed;
    }

    public void setSeed(Integer seed) {
        this.seed = seed;
    }
}
