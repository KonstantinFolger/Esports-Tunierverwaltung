package at.htl.api;

import at.htl.workloads.tournament.Tournament;
import at.htl.workloads.tournament.TournamentStatus;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@ApplicationScoped
public class TournamentTestDataFactory {

    private final EntityManager entityManager;

    public TournamentTestDataFactory(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public Long createPlannedTournament() {
        var today = LocalDate.now();
        var tournament = new Tournament("Registration test " + UUID.randomUUID(), today,
                today.plusDays(3), new BigDecimal("1000.00"), TournamentStatus.PLANNED);
        entityManager.persist(tournament);
        return tournament.getId();
    }
}
