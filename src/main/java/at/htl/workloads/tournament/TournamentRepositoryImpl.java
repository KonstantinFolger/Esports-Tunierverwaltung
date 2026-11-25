package at.htl.workloads.tournament;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;

import java.util.Optional;

@ApplicationScoped
public class TournamentRepositoryImpl implements TournamentRepository {

    private final EntityManager entityManager;

    public TournamentRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Optional<Tournament> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Tournament.class, id));
    }
}
