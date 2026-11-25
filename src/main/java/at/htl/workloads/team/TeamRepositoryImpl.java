package at.htl.workloads.team;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;

import java.util.Optional;

@ApplicationScoped
public class TeamRepositoryImpl implements TeamRepository {

    private final EntityManager entityManager;

    public TeamRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Optional<Team> findByTag(String tag) {
        var query = entityManager.createQuery(
                "" /* TODO (you): JPQL query for the Team with this tag */,
                Team.class);
        query.setParameter("tag", tag);
        return query.getResultStream().findFirst();
    }

    @Override
    public Optional<Team> findByName(String name) {
        var query = entityManager.createQuery(
                "" /* TODO (you): JPQL query for the Team with this name */,
                Team.class);
        query.setParameter("name", name);
        return query.getResultStream().findFirst();
    }

    @Override
    public void add(Team team) {
        entityManager.persist(team);
    }
}
