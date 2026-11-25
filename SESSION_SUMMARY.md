# Session-Übergabe: Tournament Manager

## Projektstand

- Projekt: **Esports Tournament Manager** (DBI-Schulprojekt), Repository `/home/konstantinfolger/DBI/Esports-Tunierverwaltung`.
- Gearbeitet wird direkt auf `main`; das Team möchte keine Feature-Branches.
- Die Projektgrundlage wurde bereits vom Team committed: Quarkus 3.40.1.2, Java-Ziel 25, Maven Wrapper, PostgreSQL-Konfiguration und `docker-compose.yml`.
- Datenbank-Schema: `esports`; `application.properties` verwendet `drop-and-create`, PostgreSQL unter `localhost:5432` und CORS für den Angular-Client.
- Kein PostgreSQL-Container lief beim letzten Check. Die Integrationstests wurden daher nicht ausgeführt.
- Angular-Frontend ist noch nicht angelegt. Die Frontend-Seiten kommen, sobald die Backend-Endpunkte und DTO-Verträge dafür vorhanden sind.

## Aktuelle Arbeit: Use Case „Team anmelden“

Der Assistent erstellt Backend-Boilerplate, DTOs, Service/Resource und Tests. **Das Team schreibt die JPA-Beziehungen, Composite-Key-Mappings, Inheritance-Mappings, `@Enumerated`-Entscheidungen und JPQL-Strings selbst.**

### Bestätigte Regeln

- `Person`-Inheritance: `JOINED`; `Match`-Inheritance später: `SINGLE_TABLE`.
- `MembershipRole`: `CAPTAIN`, `PLAYER`, `SUBSTITUTE`; höchstens ein aktiver Captain pro Team.
- Teamname und Tag sowie `Person.email` sind unique.
- Bereits vorhandener Teamname, Tag oder Personen-E-Mail: **409**; das betroffene Feld steht im JSON-Fehlerbody.
- Wiederholte E-Mail innerhalb eines Requests oder mehr als ein Captain: **400**.
- Unbekanntes Turnier: **404**; Turnier nicht `PLANNED`: **409**.
- `Team.foundedAt` bleibt Pflichtfeld und ist im Request `LocalDate` mit `@NotNull`/`@PastOrPresent`.
- Registrierung erstellt Team, Players, TeamMemberships und TournamentRegistration in einer Transaktion.
- Response: `TeamDTO`, **201 Created** über `/api/use-cases/register-team`.

### Angelegte Dateien

- Entities/Keys: `src/main/java/at/htl/workloads/person/Person.java`, `Player.java`; `src/main/java/at/htl/workloads/team/Team.java`, `TeamMembership.java`, `TeamMembershipId.java`; `src/main/java/at/htl/workloads/tournament/Tournament.java`, `TournamentRegistration.java`, `TournamentRegistrationId.java`.
- Enums: `MembershipRole.java`, `TournamentStatus.java`.
- DTOs: `src/main/java/at/htl/model/team/RegisterTeamRequest.java`, `RegisterTeamPlayerRequest.java`, `TeamDTO.java`.
- Repositories/Mapper/Service: Repositories und `Impl` für Person, Player, Team, TeamMembership, Tournament und TournamentRegistration; `PlayerMapper`, `TeamMapper`, `TeamRegistrationService` und `TeamRegistrationServiceImpl`.
- REST/Errors: `TeamRegistrationResource`, `DomainExceptionMapper`, `ConstraintViolationExceptionMapper`, `ErrorResponse` sowie Domain-Exception-Klassen.
- Tests: `src/test/java/at/htl/workloads/team/TeamRegistrationServiceTest.java`, `src/test/java/at/htl/api/TeamRegistrationResourceTest.java` und `TournamentTestDataFactory.java`.

## Umgesetzte Mappings (vormals studentische TODOs, auf Wunsch vom Assistenten erledigt)

- `Person`: `@Inheritance(strategy = JOINED)`.
- `TeamMembershipId`, `TournamentRegistrationId`: `@Embeddable`.
- `TeamMembership`: `@EmbeddedId` + `@MapsId("teamId")`/`@MapsId("playerId")` auf `@ManyToOne` zu `Team`/`Player`; `role` als `@Enumerated(EnumType.STRING)`.
- `TournamentRegistration`: `@EmbeddedId` + `@MapsId("tournamentId")`/`@MapsId("teamId")`.
- `Team.memberships`, `Player.memberships`, `Tournament.registrations`: `@OneToMany(mappedBy = ...)`; Hilfsmethoden `Team.addMembership(membership, player)` und `Tournament.addRegistration(registration, team)` halten beide Seiten synchron.
- `Tournament.status`: `@Enumerated(EnumType.STRING)`. Venue-Beziehung kommt mit dem Venue-Workload.
- JPQL: `select t from Team t where t.tag = :tag`, `... t.name = :name`, `select p from Person p where p.email = :email` (polymorph, findet alle Subtypen).
- `TeamRegistrationServiceImpl` verbindet Memberships und Registration über diese Hilfsmethoden.

## Tests / nächster Schritt

- PostgreSQL via `docker compose up -d` gestartet; `./mvnw test`: **11 Tests bestanden** (8 Service-Unit-Tests, 3 Resource-Integrationstests).
- Use Case „Team anmelden“ ist im Backend fertig, aber noch nicht committed.
- Nächste Schritte: weitere Use Cases/Workloads (Venue, Match mit `SINGLE_TABLE`-Inheritance), Angular-Frontend.
