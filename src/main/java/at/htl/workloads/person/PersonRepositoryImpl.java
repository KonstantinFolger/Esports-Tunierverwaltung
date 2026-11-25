package at.htl.workloads.person;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;

import java.util.Optional;

@ApplicationScoped
public class PersonRepositoryImpl implements PersonRepository {

    private final EntityManager entityManager;

    public PersonRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Optional<Person> findByEmail(String email) {
        var query = entityManager.createQuery(
                "select p from Person p where p.email = :email",
                Person.class);
        query.setParameter("email", email);
        return query.getResultStream().findFirst();
    }
}
