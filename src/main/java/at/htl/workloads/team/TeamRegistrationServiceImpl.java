package at.htl.workloads.team;

import at.htl.error.ConflictException;
import at.htl.error.DuplicateException;
import at.htl.error.InvalidRequestException;
import at.htl.error.NotFoundException;
import at.htl.model.team.RegisterTeamPlayerRequest;
import at.htl.model.team.RegisterTeamRequest;
import at.htl.model.team.TeamDTO;
import at.htl.workloads.person.PersonRepository;
import at.htl.workloads.person.PlayerMapper;
import at.htl.workloads.person.PlayerRepository;
import at.htl.workloads.tournament.TournamentRegistration;
import at.htl.workloads.tournament.TournamentRegistrationId;
import at.htl.workloads.tournament.TournamentRegistrationRepository;
import at.htl.workloads.tournament.TournamentRepository;
import at.htl.workloads.tournament.TournamentStatus;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@ApplicationScoped
public class TeamRegistrationServiceImpl implements TeamRegistrationService {

    private final TeamRepository teamRepository;
    private final PersonRepository personRepository;
    private final PlayerRepository playerRepository;
    private final TeamMembershipRepository teamMembershipRepository;
    private final TournamentRepository tournamentRepository;
    private final TournamentRegistrationRepository tournamentRegistrationRepository;
    private final TeamMapper teamMapper;
    private final PlayerMapper playerMapper;

    public TeamRegistrationServiceImpl(TeamRepository teamRepository, PersonRepository personRepository,
                                       PlayerRepository playerRepository, TeamMembershipRepository teamMembershipRepository,
                                       TournamentRepository tournamentRepository,
                                       TournamentRegistrationRepository tournamentRegistrationRepository,
                                       TeamMapper teamMapper, PlayerMapper playerMapper) {
        this.teamRepository = teamRepository;
        this.personRepository = personRepository;
        this.playerRepository = playerRepository;
        this.teamMembershipRepository = teamMembershipRepository;
        this.tournamentRepository = tournamentRepository;
        this.tournamentRegistrationRepository = tournamentRegistrationRepository;
        this.teamMapper = teamMapper;
        this.playerMapper = playerMapper;
    }

    @Override
    @Transactional
    public TeamDTO registerTeam(RegisterTeamRequest request) {
        validateRequest(request);

        if (teamRepository.findByTag(request.tag()).isPresent()) {
            throw new DuplicateException("tag", "Team tag '" + request.tag() + "' already exists");
        }
        if (teamRepository.findByName(request.name()).isPresent()) {
            throw new DuplicateException("name", "Team name '" + request.name() + "' already exists");
        }

        var tournament = tournamentRepository.findById(request.tournamentId())
                .orElseThrow(() -> new NotFoundException("tournamentId",
                        "Tournament " + request.tournamentId() + " does not exist"));
        if (tournament.getStatus() != TournamentStatus.PLANNED) {
            throw new ConflictException("tournamentId",
                    "Tournament " + request.tournamentId() + " is not PLANNED");
        }

        for (var playerRequest : request.players()) {
            if (personRepository.findByEmail(playerRequest.email()).isPresent()) {
                throw new DuplicateException("email", "Player email '" + playerRequest.email() + "' already exists");
            }
        }

        var team = teamMapper.toEntity(request);
        teamRepository.add(team);

        var joinedAt = LocalDate.now();
        for (RegisterTeamPlayerRequest playerRequest : request.players()) {
            var player = playerMapper.toEntity(playerRequest);
            playerRepository.add(player);

            var membershipId = new TeamMembershipId(team.getId(), player.getId());
            var membership = new TeamMembership(membershipId, joinedAt, null, playerRequest.role());
            team.addMembership(membership, player);
            teamMembershipRepository.add(membership);
        }

        var registrationId = new TournamentRegistrationId(tournament.getId(), team.getId());
        var registration = new TournamentRegistration(registrationId, LocalDateTime.now(), null);
        tournament.addRegistration(registration, team);
        tournamentRegistrationRepository.add(registration);

        return teamMapper.toDto(team);
    }

    private void validateRequest(RegisterTeamRequest request) {
        if (request == null) {
            throw new InvalidRequestException("request", "Registration request is required");
        }
        if (request.name() == null || request.name().isBlank()) {
            throw new InvalidRequestException("name", "Team name is required");
        }
        if (request.name().length() > 120) {
            throw new InvalidRequestException("name", "Team name must be at most 120 characters");
        }
        if (request.tag() == null || request.tag().isBlank()) {
            throw new InvalidRequestException("tag", "Team tag is required");
        }
        if (request.tag().length() > 8) {
            throw new InvalidRequestException("tag", "Team tag must be at most 8 characters");
        }
        if (request.tournamentId() == null || request.tournamentId() <= 0) {
            throw new InvalidRequestException("tournamentId", "A positive tournament id is required");
        }
        if (request.foundedAt() == null || request.foundedAt().isAfter(LocalDate.now())) {
            throw new InvalidRequestException("foundedAt", "Founded date is required and cannot be in the future");
        }
        if (request.players() == null || request.players().isEmpty()) {
            throw new InvalidRequestException("players", "At least one player is required");
        }

        Set<String> emails = new HashSet<>();
        int captains = 0;
        for (var player : request.players()) {
            if (player == null) {
                throw new InvalidRequestException("players", "Player entries cannot be null");
            }
            if (player.firstName() == null || player.firstName().isBlank()) {
                throw new InvalidRequestException("firstName", "Player first name is required");
            }
            if (player.firstName().length() > 80) {
                throw new InvalidRequestException("firstName", "Player first name must be at most 80 characters");
            }
            if (player.lastName() == null || player.lastName().isBlank()) {
                throw new InvalidRequestException("lastName", "Player last name is required");
            }
            if (player.lastName().length() > 80) {
                throw new InvalidRequestException("lastName", "Player last name must be at most 80 characters");
            }
            if (player.email() == null || player.email().isBlank()) {
                throw new InvalidRequestException("email", "Player email is required");
            }
            if (player.email().length() > 254) {
                throw new InvalidRequestException("email", "Player email must be at most 254 characters");
            }
            if (player.nickname() == null || player.nickname().isBlank()) {
                throw new InvalidRequestException("nickname", "Player nickname is required");
            }
            if (player.nickname().length() > 40) {
                throw new InvalidRequestException("nickname", "Player nickname must be at most 40 characters");
            }
            if (player.country() == null || player.country().isBlank()) {
                throw new InvalidRequestException("country", "Player country is required");
            }
            if (player.country().length() != 2) {
                throw new InvalidRequestException("country", "Player country must use a two-character code");
            }
            if (player.birthDate() == null || !player.birthDate().isBefore(LocalDate.now())) {
                throw new InvalidRequestException("birthDate", "Player birth date must be in the past");
            }
            if (player.role() == null) {
                throw new InvalidRequestException("role", "Player membership role is required");
            }
            if (!emails.add(player.email())) {
                throw new InvalidRequestException("email", "Player email is repeated in the registration request");
            }
            if (player.role() == MembershipRole.CAPTAIN) {
                captains++;
            }
        }
        if (captains > 1) {
            throw new InvalidRequestException("role", "A team can have at most one active CAPTAIN");
        }
    }
}
