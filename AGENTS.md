# Anweisungen für den Coding-Agent

## Beim Start jeder Session

1. Lies **zuerst** `SESSION_SUMMARY.md` im Projekt-Root vollständig durch, bevor du irgendetwas anderes tust.
2. Übernimm daraus Projektstand, bestätigte Regeln, offene TODOs und den nächsten Schritt als verbindlichen Kontext.

## Arbeitsweise: selbstständig arbeiten, keine unnötigen Rückfragen

- **Arbeite selbstständig.** Stelle keine Rückfragen, die du aus `SESSION_SUMMARY.md`, dem Code, `pom.xml`, den Tests oder sinnvollen Standardannahmen selbst beantworten kannst.
- Frage **nicht** nach Bestätigung wie „Soll ich anfangen?“, „Passt das so?“ oder „Welche Variante möchtest du?“ – triff eine vernünftige Entscheidung, setze sie um und nenne sie kurz im Ergebnis.
- Bei kleinen Unklarheiten: nimm die naheliegendste Lösung, die zu bestehendem Code und den Regeln in `SESSION_SUMMARY.md` passt, und mach weiter.
- Rückfragen nur, wenn du wirklich blockiert bist (z. B. widersprüchliche Anforderungen, destruktive Aktion wie Daten löschen, Commit/Push). Dann **eine** gebündelte, konkrete Frage statt vieler einzelner.
- Führe nach Änderungen die relevanten Tests selbst aus (`./mvnw test -Dtest=...`) und behebe Fehler, statt nachzufragen.
- Halte Antworten kurz: was gemacht wurde, Testergebnis, was offen ist.

## Grenzen (aus SESSION_SUMMARY.md, weiterhin gültig)

- Gearbeitet wird direkt auf `main`, keine Feature-Branches. Nicht committen/pushen ohne Auftrag.
- JPA-Beziehungen, Composite-Key-Mappings, Inheritance-Mappings, `@Enumerated`-Entscheidungen und JPQL-Strings schreibt das Team selbst – dort nur Fehler erklären und Hinweise geben, nicht selbst implementieren.
- `drop-and-create` löscht DB-Daten: vor `docker compose up -d` bzw. Integrationstests ist das akzeptiert, aber keine anderen Daten-löschenden Aktionen ohne Grund.

## Am Ende einer Session

- Aktualisiere `SESSION_SUMMARY.md` mit dem neuen Stand, damit die nächste Session nahtlos weitermachen kann.
