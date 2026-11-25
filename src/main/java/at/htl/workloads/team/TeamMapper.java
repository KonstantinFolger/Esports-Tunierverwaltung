package at.htl.workloads.team;

import at.htl.model.team.RegisterTeamRequest;
import at.htl.model.team.TeamDTO;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TeamMapper {

    public Team toEntity(RegisterTeamRequest request) {
        return new Team(request.name(), request.tag(), request.foundedAt());
    }

    public TeamDTO toDto(Team team) {
        return new TeamDTO(team.getId(), team.getName(), team.getTag());
    }
}
