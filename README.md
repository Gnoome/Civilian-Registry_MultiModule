# Civilian Registry — RESTful Web Service

A **Maven multi-module** Java project implementing a full CRUD backend for managing a civilian registry, built with **JAX-RS (Jersey)**, backed by **MySQL/JDBC**, and verified with a complete **unit + integration test suite**.

The project started as a plain Java console application and was re-architected into a layered, testable, RESTful system — complete with its own HTTP client — to practice real-world API design and test automation patterns.

---

## Table of Contents

- [Overview](#overview)
- [Architecture](#architecture)
- [Tech Stack](#tech-stack)
- [REST API Reference](#rest-api-reference)
- [Validation Rules](#validation-rules)
- [Testing Strategy](#testing-strategy)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
- [Skills Demonstrated](#skills-demonstrated)

---

## Overview

The service manages **civilian records** (ID, name, surname, gender, birthdate, address, tax number) and exposes a full set of CRUD operations over HTTP, with:

- ✅ **Self-provisioning database** — on first run, the service detects whether its MySQL schema exists and creates the database + tables automatically from bundled SQL scripts.
- ✅ **Server-side validation layer** — every write/search operation runs through a dedicated validator before touching the database, returning descriptive `400 Bad Request` errors instead of failing silently.
- ✅ **A real HTTP client** — a separate module ships a console menu application that talks to the API exactly like any external consumer would (via `jakarta.ws.rs.client`), reusing the same domain model as the server.
- ✅ **Automated end-to-end testing** — integration tests spin up a real Tomcat instance, deploy the WAR, and exercise every endpoint over actual HTTP, fully automated via Maven.

---

## Architecture

The project is split into **three independently-versioned Maven modules**, each with a single responsibility:

```
Civilian_Registry (parent POM)
├── domain          → shared model & validation logic (no framework dependencies)
├── rest             → JAX-RS/Jersey service + JDBC persistence + test suite
└── client_service   → console client consuming the REST API
```

| Module | Role | Depends on |
|---|---|---|
| **domain** | `Person` entity + `PersonValidator` — pure Java, framework-agnostic business rules | — |
| **rest** | Jersey REST endpoints, `DatabaseManager` (JDBC persistence), unit & integration tests | `domain` |
| **client_service** | Interactive console menu + HTTP client built on `jakarta.ws.rs.client` | `domain` |

This separation means the validation logic is written **once** and reused identically by both the server (for input sanitization) and the client (for fail-fast UX before a request is even sent) — no duplicated business rules.

---

## Tech Stack

**Backend**
- Java 17
- Jersey 3.1 (JAX-RS reference implementation)
- Jackson (`jersey-media-json-jackson`) for JSON (de)serialization
- Raw JDBC + MySQL Connector/J 8.0 for persistence (no ORM — direct SQL control)
- Jakarta Servlet / deployed as a WAR on Tomcat

**Testing**
- JUnit 5 (Jupiter) — unit tests, including parameterized test suites
- Rest-Assured 5 — HTTP-level integration tests
- Maven Failsafe plugin — runs integration tests in their own lifecycle phase
- Codehaus Cargo plugin — automatically provisions, starts, and stops an embedded Tomcat 10 instance for integration testing

**Build**
- Maven (multi-module reactor build)

---

## REST API Reference

Base path: `/Civilian_REST/api/Civilians`

| Method | Endpoint | Description | Success | Failure |
|---|---|---|---|---|
| `POST` | `/Civilians` | Create a new civilian record | `201 Created` + `Location` header | `400` (invalid data or duplicate ID) |
| `GET` | `/Civilians` | Retrieve every civilian in the registry | `200 OK` + JSON array | — |
| `GET` | `/Civilians/search` | Search by any combination of `id`, `name`, `surname`, `birthdate`, `gender`, `address`, `tax` query params | `200 OK` + JSON array | `400` (invalid filter values) |
| `PATCH` | `/Civilians/{id}` | Update a civilian's `address` and/or `tax` number | `200 OK` | `400` (not found / invalid input) |
| `DELETE` | `/Civilians/{id}` | Remove a civilian by ID | `204 No Content` | `404 Not Found` |

All endpoints also return `503 Service Unavailable` if the underlying MySQL connection cannot be established, rather than throwing an unhandled error.

**Example — create a civilian:**

```bash
curl -X POST http://localhost:8080/Civilian_REST/api/Civilians \
  -H "Content-Type: application/json" \
  -d '{
        "id": "12345678",
        "name": "George",
        "surname": "Nan",
        "gender": "M",
        "birthdate": "11-4-1995",
        "address": "Athens",
        "tax": "123456789"
      }'
```

---

## Validation Rules

Enforced centrally in `PersonValidator` and applied contextually (insert / update / search / delete each validate a different subset of fields):

- **ID** — required, exactly 8 digits
- **Name / Surname** — required, non-empty
- **Gender** — required, `M` or `F` (case-insensitive)
- **Birthdate** — required, strict `DD-MM-YYYY` format
- **Tax number** — optional, but if present must be exactly 9 characters

---

## Testing Strategy

The project targets full coverage across both internal classes used by the service **and** every exposed HTTP method:

### Unit tests (`mvn test`)
- **`PersonValidatorTest`** — parameterized JUnit 5 tests (`@CsvSource`, `@NullAndEmptySource`) covering every validation branch (valid/invalid id, name, surname, gender, birthdate, tax).
- **`DatabaseManagerTest`** — exercises every persistence method (`addPerson`, `deletePerson`, `updatePerson`, `existsPerson`, `viewAllPersons`, `viewSelectedCivilians`, `existsDatabase`) against a real schema, with automatic cleanup between runs.

### Integration tests (`mvn verify`)
- **`CivilianServiceIT`** — Rest-Assured tests that exercise the deployed service **over real HTTP**, covering all 5 REST methods end-to-end (create → search → update → list → delete), using ordered execution (`@TestMethodOrder`) to simulate a realistic record lifecycle.
- Fully automated: the **Cargo** plugin downloads and boots an embedded **Tomcat 10.1** instance, deploys the built WAR, runs the Rest-Assured suite against `localhost:8080`, then tears the server down — all within a single `mvn verify` command, no manual server setup required.

---

## Project Structure

```
Civilian_Registry/
├── pom.xml                                  # parent/reactor POM
├── domain/
│   └── src/main/java/gr/gnoome/
│       ├── Person.java                      # entity / DTO
│       └── PersonValidator.java             # shared validation rules
├── rest/
│   ├── src/main/java/gr/gnoome/
│   │   ├── service/CivilianService.java     # JAX-RS resource (5 endpoints)
│   │   └── utility/DatabaseManager.java      # JDBC persistence layer
│   ├── src/main/resources/SQL_Scripts/       # schema + parameterized CRUD SQL
│   ├── src/main/webapp/WEB-INF/web.xml       # Jersey servlet mapping
│   └── src/test/java/gr/gnoome/service/
│       ├── PersonValidatorTest.java          # unit tests
│       ├── DatabaseManagerTest.java          # unit tests
│       └── CivilianServiceIT.java            # Rest-Assured integration tests
└── client_service/
    └── src/main/java/gr/gnoome/
        ├── HttpHandler.java                  # JAX-RS client wrapper
        └── Menu.java                         # interactive console menu
```

---

## Getting Started

**Prerequisites:** JDK 17, Maven, a local MySQL server reachable at `localhost:3306` (the service provisions its own schema on first connection).

```bash
# Build & run all unit tests across every module
mvn clean install

# Run only the integration test suite (auto-starts/stops Tomcat via Cargo)
cd rest
mvn verify

# Run the console client against a running deployment
cd client_service
mvn compile exec:java -Dexec.mainClass="gr.gnoome.Menu"
```

> Note: database credentials are currently hardcoded in `DatabaseManager` for the scope of this exercise; externalizing them to environment variables / a config file would be the natural next step for a production deployment.

---

## Skills Demonstrated

- Designing and implementing a RESTful API with proper HTTP semantics (status codes, `Location` headers, idempotent updates)
- Structuring a non-trivial codebase as a **multi-module Maven reactor project** with clean dependency boundaries
- Writing a JDBC persistence layer by hand — parameterized queries, transactions with explicit commit/rollback, connection lifecycle management
- Building a **layered validation strategy** shared between client and server to avoid duplicated business logic
- Writing **parameterized unit tests** (JUnit 5) to cover validation logic exhaustively with minimal code
- Automating **full end-to-end integration testing** with Rest-Assured + an ephemeral, Maven-managed application server (Failsafe + Cargo), with zero manual setup
- Implementing a REST **client library** from scratch using `jakarta.ws.rs.client`, decoupled from the server implementation
