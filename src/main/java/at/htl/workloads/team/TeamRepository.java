package at.htl.workloads.team;

import java.util.Optional;

public interface TeamRepository {

    Optional<Team> findByTag(String tag);

    Optional<Team> findByName(String name);

    void add(Team team);
}
