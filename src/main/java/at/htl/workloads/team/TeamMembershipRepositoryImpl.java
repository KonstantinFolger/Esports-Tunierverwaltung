package at.htl.workloads.team;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;

@ApplicationScoped
public class TeamMembershipRepositoryImpl implements TeamMembershipRepository {

    private final EntityManager entityManager;

    public TeamMembershipRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void add(TeamMembership membership) {
        entityManager.persist(membership);
    }
}
