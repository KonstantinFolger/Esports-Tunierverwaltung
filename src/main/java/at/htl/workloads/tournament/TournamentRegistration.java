package at.htl.workloads.tournament;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import java.time.LocalDateTime;

@Entity
public class TournamentRegistration {

    // TODO (you): map this key with @EmbeddedId and map the Tournament/Team owning relationships with @MapsId.
    private TournamentRegistrationId id;

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
