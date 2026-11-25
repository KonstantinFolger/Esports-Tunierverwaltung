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

## Offene studentische TODOs

1. `Person.java`: vereinbartes `JOINED`-Inheritance-Mapping ergänzen.
2. `TeamMembershipId.java` und `TournamentRegistrationId.java`: `@Embeddable`; in den Entity-Klassen die Composite IDs und die Beziehungen mit den passenden Schlüsselbezügen abbilden.
3. `TeamMembership.java`, `Team.java`, `Player.java`: Beziehungen und Hilfsmethoden ergänzen, die beide Seiten synchron halten.
4. `TournamentRegistration.java`, `Tournament.java`: Tournament-/Team-Beziehungen ergänzen. Die Venue-Beziehung ist ebenfalls als TODO markiert; die Venue-Entity gehört zu einem späteren Workload.
5. `TeamMembership.role` und `Tournament.status`: `@Enumerated`-Speicherart selbst auswählen und eintragen.
6. JPQL-Strings in `TeamRepositoryImpl.findByTag`, `TeamRepositoryImpl.findByName` und `PersonRepositoryImpl.findByEmail` ergänzen. Die E-Mail-Suche muss `Person` einschließlich Subtypen berücksichtigen. Parameter sind bereits gebunden.
7. Die markierten Stellen in `TeamRegistrationServiceImpl` verwenden, um Memberships mit Team/Player und die TournamentRegistration mit Tournament/Team zu verbinden.

## Tests / nächster Schritt

- `./mvnw test -Dtest=TeamRegistrationServiceTest`: **8 Tests bestanden**.
- Das Resource-Integrationstest-File wurde kompiliert, aber nicht ausgeführt: dafür braucht es PostgreSQL und vollständige JPA-Mappings.
- Vor Integrationstests PostgreSQL starten, z. B. mit `docker compose up -d`. Achtung: `drop-and-create` setzt das Schema neu auf und löscht vorhandene Daten.
- Wenn das Team die TODOs ergänzt hat, zuerst die relevanten Tests und danach die ganze Suite ausführen. Fehler in den studentischen Mappings/Queries erklären und Hinweise geben; nicht deren JPA-Mappings oder Query-Strings schreiben.
- `git status` zeigte zuletzt `main` mit uncommitteten, untracked `src/main/java/`- und `src/test/`-Änderungen. Der aktuelle Use Case ist noch nicht committed.
