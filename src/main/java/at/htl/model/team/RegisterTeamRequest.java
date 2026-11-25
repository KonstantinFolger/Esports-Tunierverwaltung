package at.htl.model.team;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

public record RegisterTeamRequest(
        @NotBlank(message = "Team name is required")
        @Size(max = 120, message = "Team name must be at most 120 characters")
        String name,

        @NotBlank(message = "Team tag is required")
        @Size(max = 8, message = "Team tag must be at most 8 characters")
        String tag,

        @NotNull(message = "Tournament id is required")
        @Positive(message = "Tournament id must be positive")
        Long tournamentId,

        @NotNull(message = "Founded date is required")
        @PastOrPresent(message = "Founded date cannot be in the future")
        LocalDate foundedAt,

        @NotEmpty(message = "At least one player is required")
        List<@NotNull @Valid RegisterTeamPlayerRequest> players
) {
}
