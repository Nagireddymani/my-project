# Data Model: User Authentication System

## Entities

### User
- `id`: UUID, primary key
- `email`: string, unique, required
- `passwordHash`: string, required
- `fullName`: string, optional
- `roles`: list of strings, optional
- `status`: enum [ACTIVE, SUSPENDED, DEACTIVATED]
- `failedLoginAttempts`: integer
- `lockedUntil`: timestamp, optional
- `createdAt`: timestamp
- `updatedAt`: timestamp

### RefreshToken
- `id`: UUID, primary key
- `userId`: UUID, foreign key → User.id
- `tokenHash`: string, required
- `deviceId`: string, optional
- `issuedAt`: timestamp
- `expiresAt`: timestamp
- `revokedAt`: timestamp, optional
- `replacedByTokenId`: UUID, optional
- `createdAt`: timestamp
- `updatedAt`: timestamp

### PasswordResetRequest
- `id`: UUID, primary key
- `userId`: UUID, foreign key → User.id
- `tokenHash`: string, required
- `createdAt`: timestamp
- `expiresAt`: timestamp
- `usedAt`: timestamp, optional
- `isUsed`: boolean

## Relationships
- A `User` may have multiple `RefreshToken` records.
- A `User` may have multiple `PasswordResetRequest` records.

## Validation Rules
- `email` must be unique and follow standard email format.
- `passwordHash` must never store plaintext passwords.
- `RefreshToken.tokenHash` and `PasswordResetRequest.tokenHash` must be stored hashed, not raw token values.
- `RefreshToken.expiresAt` must enforce short-lived renewal windows.
- `PasswordResetRequest.expiresAt` must be limited to a short duration (e.g. 15 minutes).

## State Transitions
- `User` password reset flow: request created → token issued → token consumed → password updated.
- `RefreshToken` lifecycle: issued → used for renewal → rotated → old token revoked.
- `User` lockout behavior: failed login attempts accumulate → optional lockout state set → reset after successful login or timeout.
