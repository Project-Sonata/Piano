# Gemini CLI Context: Piano (Authentication Microservice)

This project, **Piano**, is an authentication microservice for the Sonata project. It handles user registration, login, and related authentication features using a reactive stack.

## Project Overview

- **Core Technology:** Java 17, Spring Boot 3.3.3.
- **Web Framework:** Spring WebFlux (Reactive).
- **Persistence:** R2DBC with PostgreSQL (Reactive database access).
- **Database Migrations:** Flyway (runs synchronously during startup).
- **Security:** JJWT for token management, Spring Security (inferred/likely present).
- **Messaging/Email:** Spring Boot Starter Mail with Thymeleaf templates for emails.
- **API Documentation:** OpenAPI 3 (SpringDoc) with Swagger UI.
- **Other Tools:** Lombok, Commons Validator, Apache Commons Lang3.

## Building and Running

### Prerequisites
- Docker and Docker Compose
- Java 17 (for local builds)

### Common Commands
- **Local Development (Docker):**
  ```bash
  ./local-run.sh
  ```
  This command starts the application and its PostgreSQL dependency using `docker-compose.yml`.
- **Running Tests:**
  ```bash
  ./run-tests.sh
  ```
  This command runs tests within a Docker container using `docker-compose.test.yml`.
- **Gradle Build:**
  ```bash
  ./gradlew build
  ```
- **Run Checkstyle:**
  ```bash
  ./gradlew checkstyleMain
  ```

## Development Conventions

### Coding Style & Standards
- **Reactive Programming:** The project follows the reactive paradigm using Project Reactor (Mono/Flux). Ensure all I/O and service calls remain non-blocking.
- **Null Safety:** Strict enforcement of nullability annotations. A custom Checkstyle rule (`RequiredFieldAnnotationCheck`) requires all non-primitive fields in certain packages to be annotated with `@NotNull` or `@Nullable`.
- **Response Handling:** Uses a custom `HttpStatuses` utility for consistent `ResponseEntity` creation.
- **Lombok:** Heavily used for reducing boilerplate (e.g., `@Value`, `@Builder`, `@AllArgsConstructor`).

### Testing Practices
- **Integration Testing:** Uses `WebTestClient` for API testing and `Testcontainers` for database-backed tests.
- **Faker:** `JavaFaker` is used for generating test data.
- **Mutation Testing:** Pitest is configured for mutation testing to ensure test quality.
- **Mocking:** Uses `MockWebServer` for external API mocking.

### Architecture & Structure
- **API Layer:** Controllers are located in `com.odeyalo.sonata.piano.api`.
- **Service Layer:** Business logic is in `com.odeyalo.sonata.piano.service`.
- **Persistence:** Repositories are in `com.odeyalo.sonata.piano.repository` using R2DBC.
- **Configuration:** Centrally managed in `com.odeyalo.sonata.piano.config`.
- **Changelog Management:** All pull requests must include a new YAML file in the `changelog/` directory (e.g., `Sonata-1.yaml`) describing the changes. Validation is enforced via CI.

## Key Files
- `build.gradle`: Project dependencies and plugin configuration.
- `src/main/resources/application.properties`: Main application configuration.
- `docker-compose.yml`: Local infrastructure setup (App + DB).
- `buildSrc/`: Contains custom Checkstyle rules.
- `src/main/resources/db/migration/`: Flyway migration scripts.
- `src/main/resources/static/openapi.yaml`: API specification.
