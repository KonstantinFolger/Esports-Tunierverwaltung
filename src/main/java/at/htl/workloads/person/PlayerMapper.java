package at.htl.workloads.person;

import at.htl.model.team.RegisterTeamPlayerRequest;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PlayerMapper {

    public Player toEntity(RegisterTeamPlayerRequest request) {
        return new Player(request.firstName(), request.lastName(), request.email(), request.nickname(),
                request.country(), request.birthDate());
    }
}
