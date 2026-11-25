package at.htl.workloads.person;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;

@ApplicationScoped
public class PlayerRepositoryImpl implements PlayerRepository {

    private final EntityManager entityManager;

    public PlayerRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void add(Player player) {
        entityManager.persist(player);
    }
}
