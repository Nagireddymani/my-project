<!--
Sync Impact Report
Version change: placeholders -> 1.0.0
Modified principles:
  - created I. Modern Java & Spring Boot standard
  - created II. Hexagonal Architecture for Microservices
  - created III. Test Coverage & JUnit 5 Quality Gate
  - created IV. API Contract Discipline & OpenAPI Documentation
  - created V. Observability, Resilience, and Operational Consistency
Added sections:
  - Additional Constraints
  - Development Workflow
Removed sections:
  - none
Templates requiring updates:
  - .specify/templates/plan-template.md ✅ no changes required
  - .specify/templates/spec-template.md ✅ no changes required
  - .specify/templates/tasks-template.md ✅ no changes required
Follow-up TODOs:
  - none
-->

# My Project Constitution

## Core Principles

### I. Modern Java & Spring Boot Standard
Use Java 21 and Spring Boot 3 for all services and libraries. All new code must compile and run cleanly against the Java 21 LTS baseline, avoid deprecated Spring Boot 2 APIs, and maintain a single approved Spring Boot dependency alignment per service.

### II. Hexagonal Architecture for Microservices
Design each microservice as a clear ports-and-adapters boundary. Domain logic MUST remain framework-agnostic, inbound REST/OpenAPI adapters and outbound persistence adapters MUST be isolated, and no service may share a database schema with another service.

### III. Test Coverage & JUnit 5 Quality Gate
Enforce a minimum 90% automated coverage threshold at the service/module level. Use JUnit 5 for unit and integration tests, write tests before production behavior changes, and require coverage validation in CI for every merge.

### IV. API Contract Discipline & OpenAPI Documentation
Every service API MUST expose machine-readable OpenAPI documentation and use it as the canonical contract. OpenAPI definitions must be generated from code, validated in CI, and versioned alongside service releases.

### V. Observability, Resilience, and Operational Consistency
Microservices MUST expose health, readiness, and metrics endpoints; use structured logging with correlation IDs; handle failures explicitly; and remain independently deployable with consistent runtime observability.

## Additional Constraints
- Technology stack: Java 21, Spring Boot 3, JUnit 5, OpenAPI 3, and a production-ready build toolchain with CI validation.
- Services MUST be independently deployable, container-ready, and avoid runtime coupling through shared infrastructure or direct database access between services.
- Domain packages MUST not depend on Spring framework APIs; adapters are the only layers allowed to reference Spring Boot, OpenAPI, or persistence frameworks.
- API contract changes that are not backward compatible MUST include a documented migration plan and appropriate version bump.
- Tests MUST cover at least 90% of code paths in each module, with any exception requiring explicit review and documented risk.

## Development Workflow
- Pull requests MUST include tests for new behavior, updated OpenAPI documentation if APIs changed, and explicit references to the constitution principles that apply.
- Code review MUST verify hexagonal boundaries, API contract stability, coverage enforcement, and Spring Boot 3 compliance.
- Continuous integration MUST run build verification, JUnit 5 tests, coverage reporting, OpenAPI validation, and service health contract checks where applicable.
- Any deviation from these rules MUST be documented in the PR description and approved by a reviewer before merge.

## Governance
The constitution is the authoritative development policy for this project. All engineering decisions that affect architecture, testing, API contracts, or service interoperability MUST align with these principles.

- Amendments require a documented rationale and a version update.
- Version policy:
  - MAJOR: principle redefinition or breaking governance changes.
  - MINOR: new principle, workflow expansion, or material constraint addition.
  - PATCH: wording clarifications, typo fixes, and non-semantic refinements.
- Compliance review: every PR MUST reference the applicable constitution principles and confirm CI validation.

**Version**: 1.0.0 | **Ratified**: 2026-05-30 | **Last Amended**: 2026-05-30
