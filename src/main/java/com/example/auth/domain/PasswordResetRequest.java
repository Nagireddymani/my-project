package com.example.auth.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Domain entity representing a password reset request.
 * Part of hexagonal architecture domain layer - framework agnostic business logic.
 */
@Entity
@Table(name = "password_reset_requests")
public class PasswordResetRequest {
    @Id
    @Column(columnDefinition = "UUID")
    private UUID id;
    
    @Column(nullable = false, columnDefinition = "UUID")
    private UUID userId;
    
    @Column(nullable = false)
    private String tokenHash;
    
    @Column(nullable = false)
    private LocalDateTime createdAt;
    
    @Column(nullable = false)
    private LocalDateTime expiresAt;
    
    private LocalDateTime usedAt;
    
    @Column(nullable = false)
    private Boolean isUsed;
    
    // Constructors
    public PasswordResetRequest() {}
    
    public PasswordResetRequest(UUID userId, String tokenHash) {
        this.id = UUID.randomUUID();
        this.userId = userId;
        this.tokenHash = tokenHash;
        this.createdAt = LocalDateTime.now();
        this.expiresAt = LocalDateTime.now().plusMinutes(15);
        this.isUsed = false;
    }
    
    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    
    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }
    
    public String getTokenHash() { return tokenHash; }
    public void setTokenHash(String tokenHash) { this.tokenHash = tokenHash; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getExpiresAt() { return expiresAt; }
    public void setExpiresAt(LocalDateTime expiresAt) { this.expiresAt = expiresAt; }
    
    public LocalDateTime getUsedAt() { return usedAt; }
    public void setUsedAt(LocalDateTime usedAt) { this.usedAt = usedAt; }
    
    public Boolean getIsUsed() { return isUsed; }
    public void setIsUsed(Boolean isUsed) { this.isUsed = isUsed; }
    
    public boolean isValid() {
        return !isUsed && LocalDateTime.now().isBefore(expiresAt);
    }
}
