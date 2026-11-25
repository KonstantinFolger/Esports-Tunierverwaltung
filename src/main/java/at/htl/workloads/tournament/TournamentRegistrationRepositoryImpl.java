package at.htl.workloads.tournament;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;

@ApplicationScoped
public class TournamentRegistrationRepositoryImpl implements TournamentRegistrationRepository {

    private final EntityManager entityManager;

    public TournamentRegistrationRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void add(TournamentRegistration registration) {
        entityManager.persist(registration);
    }
}
