package at.htl.api;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;

@QuarkusTest
class TeamRegistrationResourceTest {

    @Inject
    TournamentTestDataFactory tournamentTestDataFactory;

    private Long tournamentId;

    @BeforeEach
    void setUp() {
        tournamentId = tournamentTestDataFactory.createPlannedTournament();
    }

    @Test
    void registersTeamAndReturnsCreatedLocation() {
        var token = UUID.randomUUID().toString();
        var request = request("Falcons " + token, "FAL" + token.substring(0, 5),
                List.of(player("captain", token, "CAPTAIN"), player("substitute", token, "SUBSTITUTE")));

        var response = given()
                .contentType("application/json")
                .body(request)
                .when()
                .post("/api/use-cases/register-team")
                .then()
                .statusCode(201)
                .body("id", greaterThan(0))
                .body("name", is(request.get("name")))
                .body("tag", is(request.get("tag")))
                .extract();

        org.hamcrest.MatcherAssert.assertThat(response.header("Location"),
                endsWith("/api/teams/" + response.path("id")));
    }

    @Test
    void rejectsMoreThanOneCaptainWithFieldInErrorBody() {
        var token = UUID.randomUUID().toString();
        var request = request("Wolves " + token, "WOL" + token.substring(0, 5),
                List.of(player("one", token, "CAPTAIN"), player("two", token, "CAPTAIN")));

        given()
                .contentType("application/json")
                .body(request)
                .when()
                .post("/api/use-cases/register-team")
                .then()
                .statusCode(400)
                .body("field", is("role"));
    }

    @Test
    void rejectsTagAlreadyRegisteredWithConflictAndFieldInErrorBody() {
        var token = UUID.randomUUID().toString();
        var tag = "DUP" + token.substring(0, 5);
        var firstRequest = request("Ravens " + token, tag,
                List.of(player("first", token, "CAPTAIN")));
        var secondRequest = request("Hawks " + token, tag,
                List.of(player("second", token, "CAPTAIN")));

        given().contentType("application/json").body(firstRequest)
                .when().post("/api/use-cases/register-team").then().statusCode(201);

        given()
                .contentType("application/json")
                .body(secondRequest)
                .when()
                .post("/api/use-cases/register-team")
                .then()
                .statusCode(409)
                .body("field", is("tag"));
    }

    private Map<String, Object> request(String name, String tag, List<Map<String, Object>> players) {
        return Map.of(
                "name", name,
                "tag", tag,
                "tournamentId", tournamentId,
                "foundedAt", LocalDate.now().minusYears(2).toString(),
                "players", players);
    }

    private Map<String, Object> player(String name, String token, String role) {
        return Map.of(
                "firstName", name,
                "lastName", "Player",
                "email", name + "." + token + "@example.test",
                "nickname", name,
                "country", "AT",
                "birthDate", LocalDate.now().minusYears(20).toString(),
                "role", role);
    }
}
