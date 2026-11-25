package at.htl.workloads.team;

import at.htl.model.team.RegisterTeamRequest;
import at.htl.model.team.TeamDTO;

public interface TeamRegistrationService {

    TeamDTO registerTeam(RegisterTeamRequest request);
}
