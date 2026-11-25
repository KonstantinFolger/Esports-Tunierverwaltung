package at.htl.workloads.tournament;

import java.util.Optional;

public interface TournamentRepository {

    Optional<Tournament> findById(Long id);
}
