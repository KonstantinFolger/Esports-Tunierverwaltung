package at.htl.workloads.team;

import at.htl.error.ConflictException;
import at.htl.error.DuplicateException;
import at.htl.error.InvalidRequestException;
import at.htl.error.NotFoundException;
import at.htl.model.team.RegisterTeamPlayerRequest;
import at.htl.model.team.RegisterTeamRequest;
import at.htl.model.team.TeamDTO;
import at.htl.workloads.person.Player;
import at.htl.workloads.person.PersonRepository;
import at.htl.workloads.person.PlayerMapper;
import at.htl.workloads.person.PlayerRepository;
import at.htl.workloads.team.MembershipRole;
import at.htl.workloads.tournament.Tournament;
import at.htl.workloads.tournament.TournamentRegistrationRepository;
import at.htl.workloads.tournament.TournamentRepository;
import at.htl.workloads.tournament.TournamentStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TeamRegistrationServiceTest {

    @Mock
    TeamRepository teamRepository;
    @Mock
    PersonRepository personRepository;
    @Mock
    PlayerRepository playerRepository;
    @Mock
    TeamMembershipRepository teamMembershipRepository;
    @Mock
    TournamentRepository tournamentRepository;
    @Mock
    TournamentRegistrationRepository tournamentRegistrationRepository;

    private TeamRegistrationServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new TeamRegistrationServiceImpl(teamRepository, personRepository, playerRepository,
                teamMembershipRepository, tournamentRepository, tournamentRegistrationRepository,
                new TeamMapper(), new PlayerMapper());
    }

    @Test
    void rejectsDuplicateTagAlreadyInDatabase() {
        when(teamRepository.findByTag("ACE")).thenReturn(Optional.of(new Team()));

        var exception = assertThrows(DuplicateException.class, () -> service.registerTeam(validRequest("ACE")));

        assertEquals("tag", exception.field());
        verifyNoWrites();
    }

    @Test
    void rejectsDuplicateNameAlreadyInDatabase() {
        when(teamRepository.findByName("Falcons")).thenReturn(Optional.of(new Team()));

        var exception = assertThrows(DuplicateException.class, () -> service.registerTeam(validRequest("ACE")));

        assertEquals("name", exception.field());
        verifyNoWrites();
    }

    @Test
    void rejectsPlayerEmailAlreadyInDatabase() {
        when(tournamentRepository.findById(7L)).thenReturn(Optional.of(tournament(TournamentStatus.PLANNED)));
        when(personRepository.findByEmail("captain@example.test"))
                .thenReturn(Optional.of(new Player()));

        var exception = assertThrows(DuplicateException.class, () -> service.registerTeam(validRequest("ACE")));

        assertEquals("email", exception.field());
        verifyNoWrites();
    }

    @Test
    void rejectsRepeatedEmailWithinRequestAsInvalidRequest() {
        var request = new RegisterTeamRequest("Falcons", "ACE", 7L, LocalDate.now().minusYears(2), List.of(
                player("captain@example.test", MembershipRole.CAPTAIN),
                player("captain@example.test", MembershipRole.PLAYER)));

        var exception = assertThrows(InvalidRequestException.class, () -> service.registerTeam(request));

        assertEquals("email", exception.field());
        verifyNoWrites();
    }

    @Test
    void rejectsMoreThanOneCaptain() {
        var request = new RegisterTeamRequest("Falcons", "ACE", 7L, LocalDate.now().minusYears(2), List.of(
                player("one@example.test", MembershipRole.CAPTAIN),
                player("two@example.test", MembershipRole.CAPTAIN)));

        var exception = assertThrows(InvalidRequestException.class, () -> service.registerTeam(request));

        assertEquals("role", exception.field());
        verifyNoWrites();
    }

    @Test
    void rejectsUnknownTournament() {
        when(tournamentRepository.findById(7L)).thenReturn(Optional.empty());

        var exception = assertThrows(NotFoundException.class, () -> service.registerTeam(validRequest("ACE")));

        assertEquals("tournamentId", exception.field());
        verifyNoWrites();
    }

    @Test
    void rejectsTournamentThatIsNotPlanned() {
        when(tournamentRepository.findById(7L)).thenReturn(Optional.of(tournament(TournamentStatus.RUNNING)));

        var exception = assertThrows(ConflictException.class, () -> service.registerTeam(validRequest("ACE")));

        assertEquals("tournamentId", exception.field());
        verifyNoWrites();
    }

    @Test
    void registersTeamPlayersMembershipsAndTournamentRegistration() {
        when(tournamentRepository.findById(7L)).thenReturn(Optional.of(tournament(TournamentStatus.PLANNED)));
        doAnswer(invocation -> {
            ((Team) invocation.getArgument(0)).setId(41L);
            return null;
        }).when(teamRepository).add(any(Team.class));
        var nextPlayerId = new AtomicLong(100L);
        doAnswer(invocation -> {
            ((Player) invocation.getArgument(0)).setId(nextPlayerId.getAndIncrement());
            return null;
        }).when(playerRepository).add(any(Player.class));

        TeamDTO result = service.registerTeam(validRequest("ACE"));

        assertEquals(41L, result.id());
        assertEquals("Falcons", result.name());
        assertEquals("ACE", result.tag());
        verify(teamRepository).add(any(Team.class));
        verify(playerRepository, org.mockito.Mockito.times(2)).add(any(Player.class));
        var memberships = ArgumentCaptor.forClass(TeamMembership.class);
        verify(teamMembershipRepository, org.mockito.Mockito.times(2)).add(memberships.capture());
        assertEquals(2, memberships.getAllValues().size());
        assertNotNull(memberships.getAllValues().get(0).getId());
        verify(tournamentRegistrationRepository).add(any());
    }

    private RegisterTeamRequest validRequest(String tag) {
        return new RegisterTeamRequest("Falcons", tag, 7L, LocalDate.now().minusYears(2), List.of(
                player("captain@example.test", MembershipRole.CAPTAIN),
                player("substitute@example.test", MembershipRole.SUBSTITUTE)));
    }

    private RegisterTeamPlayerRequest player(String email, MembershipRole role) {
        return new RegisterTeamPlayerRequest("Alex", "Miller", email, "ace", "AT",
                LocalDate.now().minusYears(20), role);
    }

    private Tournament tournament(TournamentStatus status) {
        return new Tournament("Test Cup", LocalDate.now(), LocalDate.now().plusDays(3),
                new BigDecimal("1000.00"), status);
    }

    private void verifyNoWrites() {
        verify(teamRepository, never()).add(any());
        verify(playerRepository, never()).add(any());
        verify(teamMembershipRepository, never()).add(any());
        verify(tournamentRegistrationRepository, never()).add(any());
    }
}
