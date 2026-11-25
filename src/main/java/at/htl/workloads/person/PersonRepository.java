package at.htl.workloads.person;

import java.util.Optional;

public interface PersonRepository {

    Optional<Person> findByEmail(String email);
}
