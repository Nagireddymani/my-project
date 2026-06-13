package com.example.auth.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Domain entity representing a refresh token for session renewal.
 * Part of hexagonal architecture domain layer - framework agnostic business logic.
 */
@Entity
@Table(name = "refresh_tokens")
public class RefreshToken {
    @Id
    @Column(columnDefinition = "UUID")
    private UUID id;
    
    @Column(nullable = false, columnDefinition = "UUID")
    private UUID userId;
    
    @Column(nullable = false)
    private String tokenHash;
    
    private String deviceId;
    
    @Column(nullable = false)
    private LocalDateTime issuedAt;
    
    @Column(nullable = false)
    private LocalDateTime expiresAt;
    
    private LocalDateTime revokedAt;
    
    @Column(columnDefinition = "UUID")
    private UUID replacedByTokenId;
    
    @Column(nullable = false)
    private LocalDateTime createdAt;
    
    @Column(nullable = false)
    private LocalDateTime updatedAt;
    
    // Constructors
    public RefreshToken() {}
    
    public RefreshToken(UUID userId, String tokenHash) {
        this.id = UUID.randomUUID();
        this.userId = userId;
        this.tokenHash = tokenHash;
        this.issuedAt = LocalDateTime.now();
        this.expiresAt = LocalDateTime.now().plusHours(24);
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    
    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }
    
    public String getTokenHash() { return tokenHash; }
    public void setTokenHash(String tokenHash) { this.tokenHash = tokenHash; }
    
    public String getDeviceId() { return deviceId; }
    public void setDeviceId(String deviceId) { this.deviceId = deviceId; }
    
    public LocalDateTime getIssuedAt() { return issuedAt; }
    public void setIssuedAt(LocalDateTime issuedAt) { this.issuedAt = issuedAt; }
    
    public LocalDateTime getExpiresAt() { return expiresAt; }
    public void setExpiresAt(LocalDateTime expiresAt) { this.expiresAt = expiresAt; }
    
    public LocalDateTime getRevokedAt() { return revokedAt; }
    public void setRevokedAt(LocalDateTime revokedAt) { this.revokedAt = revokedAt; }
    
    public UUID getReplacedByTokenId() { return replacedByTokenId; }
    public void setReplacedByTokenId(UUID replacedByTokenId) { this.replacedByTokenId = replacedByTokenId; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    public boolean isValid() {
        return revokedAt == null && LocalDateTime.now().isBefore(expiresAt);
    }
}
