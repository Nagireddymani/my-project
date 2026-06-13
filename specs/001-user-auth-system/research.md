# Research: User Authentication System

## Decision: Java 21 + Spring Boot 3
Rationale: The project constitution explicitly mandates Java 21 and Spring Boot 3. This combination offers long-term stability, Spring Security support, and a mature microservice ecosystem.
Alternatives considered: Kotlin with Spring Boot, Micronaut, Quarkus. Rejected because the constitution and current project alignment prioritize Spring Boot 3 standardization.

## Decision: Hexagonal Architecture
Rationale: Ports-and-adapters architecture separates domain logic from framework concerns, making authentication rules independently testable and easier to adapt across adapters.
Alternatives considered: Traditional layered architecture, monolith-style service. Rejected because those approaches increase coupling between domain logic and Spring framework APIs, violating the constitution.

## Decision: JWT access tokens with refresh token rotation
Rationale: Short-lived JWT access tokens provide scalable stateless auth, while rotating refresh tokens enable secure session renewal and revocation without requiring repeated login.
Alternatives considered: Stateful session tokens, long-lived refresh tokens, or refresh tokens without rotation. Rejected because they reduce security or make revocation harder.

## Decision: OpenAPI contract generation
Rationale: Exposing machine-readable OpenAPI is required by the constitution and provides the canonical contract for clients and CI validation. It also supports automated documentation and contract-driven development.
Alternatives considered: Handwritten API docs or API Blueprint. Rejected because those do not align with canonical, generated API contract expectations.

## Decision: JUnit 5 and 90% coverage
Rationale: JUnit 5 is the current standard for Java testing and aligns with the constitution's quality gate. High coverage is enforced to protect authentication logic and error handling.
Alternatives considered: Lower coverage thresholds or older testing frameworks. Rejected to ensure reliability of security-critical flow.
