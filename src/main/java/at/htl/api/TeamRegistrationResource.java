package at.htl.api;

import at.htl.model.team.RegisterTeamRequest;
import at.htl.workloads.team.TeamRegistrationService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

@Path("/api/use-cases")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TeamRegistrationResource {

    private final TeamRegistrationService teamRegistrationService;

    public TeamRegistrationResource(TeamRegistrationService teamRegistrationService) {
        this.teamRegistrationService = teamRegistrationService;
    }

    @POST
    @Path("/register-team")
    public Response registerTeam(@NotNull @Valid RegisterTeamRequest request, @Context UriInfo uriInfo) {
        var team = teamRegistrationService.registerTeam(request);
        var location = uriInfo.getBaseUriBuilder()
                .path("api")
                .path("teams")
                .path(String.valueOf(team.id()))
                .build();
        return Response.created(location).entity(team).build();
    }
}
