# Quickstart: User Authentication System

## Overview
This feature is a standalone authentication microservice providing signup, login, JWT access tokens, refresh token renewal, and password recovery.

## Start the service
1. Build and launch the Spring Boot service.
2. Verify the service is running and the OpenAPI contract is available.

## Common flows

### Signup
1. Send `POST /signup` with:
   - `email`
   - `password`
   - optional `fullName`
2. Expect a successful account creation response.

### Login
1. Send `POST /login` with:
   - `email`
   - `password`
2. Expect a response with:
   - `accessToken`
   - `refreshToken`
   - `tokenType`: `Bearer`
   - `expiresIn`

### Access protected resources
1. Send requests with header `Authorization: Bearer <accessToken>`.
2. The service validates the token before returning protected data.

### Refresh token renewal
1. Send `POST /refresh` with:
   - `refreshToken`
2. Expect a new `accessToken` and a rotated `refreshToken`.

### Forgot password
1. Send `POST /forgot-password` with:
   - `email`
2. Expect a 202 response that triggers a password reset email.

### Reset password
1. Send `POST /reset-password` with:
   - `token`
   - `newPassword`
2. Expect confirmation that the password has been updated.

## Notes
- Use the OpenAPI contract in `contracts/openapi.yaml` as the canonical API definition.
- Access tokens are short-lived; refresh tokens are rotated on each renewal.
- Password reset tokens are single-use and expire quickly.
