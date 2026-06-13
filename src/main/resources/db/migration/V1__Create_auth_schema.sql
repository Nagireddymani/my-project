-- V1__Create_auth_schema.sql
-- User Authentication Service Database Schema
-- Creates tables for user accounts, refresh tokens, and password reset requests

CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT RANDOM_UUID(),
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    full_name VARCHAR(255),
    roles VARCHAR(255),
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE',
    failed_login_attempts INT DEFAULT 0,
    locked_until TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_status ON users(status);

CREATE TABLE refresh_tokens (
    id UUID PRIMARY KEY DEFAULT RANDOM_UUID(),
    user_id UUID NOT NULL,
    token_hash VARCHAR(255) NOT NULL,
    device_id VARCHAR(255),
    issued_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP NOT NULL,
    revoked_at TIMESTAMP,
    replaced_by_token_id UUID,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE INDEX idx_refresh_tokens_user_id ON refresh_tokens(user_id);
CREATE INDEX idx_refresh_tokens_expires_at ON refresh_tokens(expires_at);
CREATE INDEX idx_refresh_tokens_revoked_at ON refresh_tokens(revoked_at);

CREATE TABLE password_reset_requests (
    id UUID PRIMARY KEY DEFAULT RANDOM_UUID(),
    user_id UUID NOT NULL,
    token_hash VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP NOT NULL,
    used_at TIMESTAMP,
    is_used BOOLEAN NOT NULL DEFAULT FALSE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE INDEX idx_password_reset_user_id ON password_reset_requests(user_id);
CREATE INDEX idx_password_reset_expires_at ON password_reset_requests(expires_at);
CREATE INDEX idx_password_reset_is_used ON password_reset_requests(is_used);
