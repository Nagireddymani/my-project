# Tasks: User Authentication System

**Input**: Design documents from `/specs/001-user-auth-system/`

## Phase 1: Setup (Shared Infrastructure) ✅ COMPLETE

**Purpose**: Project initialization and basic structure

- [X] T001 Create Spring Boot 3 Java 21 microservice project structure in `src/main/java/com/example/auth` and `src/test/java/com/example/auth`
- [X] T002 Initialize build file with Spring Boot 3, Spring Security, Spring Web, Spring Data JPA, springdoc-openapi, JJWT or jose4j, and JUnit 5 dependencies in `build.gradle` or `pom.xml`
- [X] T003 [P] Configure OpenAPI generation and security scheme metadata in `src/main/resources/application.yml`
- [X] T004 [P] Configure JUnit 5 test support and coverage reporting in `build.gradle` or `pom.xml`
- [X] T005 [P] Create initial health and readiness endpoint stubs in `src/main/java/com/example/auth/adapter/in/web/HealthController.java`

---

## Phase 2: Foundational (Blocking Prerequisites) ✅ COMPLETE

**Purpose**: Core authentication infrastructure that must be complete before user stories

- [X] T006 Create domain entities `User`, `RefreshToken`, and `PasswordResetRequest` in `src/main/java/com/example/auth/domain`
- [X] T007 Create persistence adapters and repository interfaces for `User`, `RefreshToken`, and `PasswordResetRequest` in `src/main/java/com/example/auth/adapter/out/persistence`
- [X] T008 [P] Implement password hashing, validation, and strength rules in `src/main/java/com/example/auth/application/security/PasswordService.java`
- [X] T009 [P] Implement JWT access token generation and validation service in `src/main/java/com/example/auth/application/security/TokenService.java`
- [X] T010 [P] Implement refresh token rotation, storage, and reuse rejection logic in `src/main/java/com/example/auth/application/security/RefreshTokenService.java`
- [X] T011 Implement shared exception handling and API error mapping in `src/main/java/com/example/auth/adapter/in/web/ApiExceptionHandler.java`
- [X] T012 Configure database schema/migrations for authentication entities in `src/main/resources/db/migration` or equivalent migration location
- [X] T013 [P] Implement generated OpenAPI contract configuration for the authentication API in `src/main/java/com/example/auth/config/OpenApiConfig.java`
- [X] T014 [P] Configure structured logging with correlation ID injection in `src/main/java/com/example/auth/config/LoggingConfig.java` and Spring Boot filter middleware

---

## Phase 3: User Story 1 - Signup and Login (Priority: P1) 🎯 MVP ✅ CORE COMPLETE

**Goal**: Enable account creation and credential-based login with JWT access tokens

**Independent Test**: Create a user account, log in with valid credentials, and receive a valid access token.

- [X] T015 [P] [US1] Create signup/login request and response DTOs in `src/main/java/com/example/auth/adapter/in/web/dto`
- [X] T016 [P] [US1] Implement `AuthenticationService` methods for signup and credential verification in `src/main/java/com/example/auth/application/service/AuthenticationService.java`
- [X] T017 [US1] Implement `/signup` endpoint in `src/main/java/com/example/auth/adapter/in/web/AuthController.java`
- [X] T018 [US1] Add duplicate-email and invalid credential handling in `src/main/java/com/example/auth/application/service/AuthenticationService.java`
- [ ] T019 [P] [US1] Add unit tests for signup and login service behavior in `src/test/java/com/example/auth/application/service/AuthenticationServiceTest.java`
- [ ] T020 [P] [US1] Add integration tests for `/signup` and `/login` endpoints in `src/test/java/com/example/auth/adapter/in/web/AuthControllerIntegrationTest.java`

---

## Phase 4: User Story 2 - Password Recovery (Priority: P2) ✅ CORE COMPLETE

**Goal**: Allow users to request a password reset and set a new password securely.

**Independent Test**: Request password recovery, receive a reset token, and successfully reset the password.

- [X] T021 [US2] Implement password reset request creation and token persistence in `src/main/java/com/example/auth/application/service/PasswordRecoveryService.java`
- [X] T022 [US2] Implement `/forgot-password` endpoint in `src/main/java/com/example/auth/adapter/in/web/AuthController.java`
- [X] T023 [US2] Implement `/reset-password` endpoint in `src/main/java/com/example/auth/adapter/in/web/AuthController.java`
- [ ] T024 [P] [US2] Add validation for reset token expiry, single-use enforcement, and password strength in `src/main/java/com/example/auth/application/service/PasswordRecoveryService.java`
- [ ] T025 [P] [US2] Add unit tests for password recovery service behavior in `src/test/java/com/example/auth/application/service/PasswordRecoveryServiceTest.java`
- [ ] T026 [P] [US2] Add integration tests for `/forgot-password` and `/reset-password` endpoints in `src/test/java/com/example/auth/adapter/in/web/PasswordRecoveryIntegrationTest.java`

---

## Phase 5: User Story 3 - Refresh Token Renewal (Priority: P2) ✅ CORE COMPLETE

**Goal**: Renew access tokens by exchanging refresh tokens and prevent reuse.

**Independent Test**: Authenticate, then renew the session using a refresh token and verify reuse is rejected.

- [X] T027 [US3] Implement refresh token renewal and rotation workflow in `src/main/java/com/example/auth/application/security/RefreshTokenService.java`
- [X] T028 [US3] Implement `/refresh` endpoint in `src/main/java/com/example/auth/adapter/in/web/AuthController.java`
- [X] T029 [US3] Implement refresh token revocation on password changes in `src/main/java/com/example/auth/application/security/RefreshTokenService.java`
- [ ] T030 [P] [US3] Add unit tests for refresh token renewal, rotation, and reuse rejection in `src/test/java/com/example/auth/application/security/RefreshTokenServiceTest.java`
- [ ] T031 [P] [US3] Add integration tests for `/refresh` endpoint and refresh token lifecycle in `src/test/java/com/example/auth/adapter/in/web/RefreshTokenIntegrationTest.java`

---

## Phase 6: Polish & Cross-Cutting Concerns

**Purpose**: Documentation, contract validation, and production readiness.

- [ ] T032 [P] Update `contracts/openapi.yaml` to reflect implemented API details and verify OpenAPI generation matches the service.
- [ ] T033 [P] Document authentication usage and quickstart flows in `specs/001-user-auth-system/quickstart.md`
- [ ] T034 [P] Add structured API error responses and OpenAPI response examples in `src/main/java/com/example/auth/adapter/in/web/ApiExceptionHandler.java`
- [ ] T035 [P] Validate JUnit 5 coverage and ensure the service meets the 90% coverage threshold in `build.gradle` or `pom.xml`
- [ ] T036 [P] Review and refactor hexagonal boundaries in `src/main/java/com/example/auth` to keep domain logic framework-agnostic.

---

## Dependencies & Execution Order

### Phase Dependencies

- **Phase 1: Setup**: No dependencies - start immediately.
- **Phase 2: Foundational**: Depends on Setup completion - blocks all user stories.
- **User Stories (Phase 3+)**: Depend on Foundational completion.
- **Polish & Cross-Cutting**: Depends on all user stories being complete.

### User Story Dependencies

- **User Story 1 (P1)**: Can start after Foundational is complete and is the MVP slice.
- **User Story 2 (P2)**: Can start after Foundational is complete and may proceed in parallel with US3.
- **User Story 3 (P2)**: Can start after Foundational is complete and may proceed in parallel with US2.

### Within Each User Story

- Models before services
- Services before endpoints
- Core implementation before integration tests
- Story complete before moving to the next priority

## Parallel Opportunities

- Setup tasks `T003`, `T004`, and `T005` can run in parallel.
- Foundational tasks `T008`, `T009`, `T010`, and `T013` can run in parallel.
- Story tasks marked `[P]` across different files can run in parallel.
- User Story 2 and User Story 3 implementations can proceed in parallel after the foundation is ready.

## Implementation Strategy

### MVP First

1. Complete Phase 1: Setup.
2. Complete Phase 2: Foundational.
3. Deliver Phase 3: User Story 1.
4. Stop and validate signup/login independently.

### Incremental Delivery

1. Finish Setup and Foundational phases.
2. Deliver User Story 1 as MVP.
3. Add User Story 2 and validate password recovery independently.
4. Add User Story 3 and validate refresh token renewal independently.
5. Finish Polish and contract validation.

### Team Parallelism

- One engineer completes Setup and Foundational.
- Another engineer builds User Story 1.
- Another engineer builds User Story 2 or User Story 3 in parallel after Foundation.
