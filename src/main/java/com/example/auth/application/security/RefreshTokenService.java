package com.example.auth.application.security;

import com.example.auth.adapter.out.persistence.RefreshTokenRepository;
import com.example.auth.domain.RefreshToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

/**
 * Service for refresh token rotation, storage, and reuse rejection.
 * Implements Constitution Principle III: Test Coverage & JUnit 5 Quality Gate.
 * Implements FR-008: Rotate refresh tokens on each renewal and reject any reuse.
 * Part of application/security layer - domain-logic focused.
 */
@Service
public class RefreshTokenService {
    private final RefreshTokenRepository repository;
    
    @Value("${auth.jwt.refresh-expiration}")
    private long refreshExpiration;
    
    public RefreshTokenService(RefreshTokenRepository repository) {
        this.repository = repository;
    }
    
    /**
     * Generate a new refresh token for a user.
     * Implements FR-006 and FR-007.
     */
    public String generateRefreshToken(UUID userId) {
        String token = generateSecureToken();
        String tokenHash = hashToken(token);
        
        RefreshToken refreshToken = new RefreshToken(userId, tokenHash);
        repository.save(refreshToken);
        
        return token;
    }
    
    /**
     * Validate and rotate a refresh token. Returns the new token if valid, null if invalid/expired/reused.
     * Implements FR-008: Reject reuse by revoking the old token after issuing a new one.
     */
    public String renewRefreshToken(String oldToken, UUID userId) {
        String tokenHash = hashToken(oldToken);
        
        // Find the token record
        Optional<RefreshToken> tokenRecord = repository.findByUserIdAndRevokedAtIsNull(userId);
        
        if (tokenRecord.isEmpty()) {
            return null;
        }
        
        RefreshToken oldTokenRecord = tokenRecord.get();
        
        // Verify the token hash matches
        if (!oldTokenRecord.getTokenHash().equals(tokenHash)) {
            return null;
        }
        
        // Check if token is expired
        if (!oldTokenRecord.isValid()) {
            return null;
        }
        
        // Generate new refresh token
        String newToken = generateSecureToken();
        String newTokenHash = hashToken(newToken);
        
        RefreshToken newTokenRecord = new RefreshToken(userId, newTokenHash);
        newTokenRecord.setDeviceId(oldTokenRecord.getDeviceId());
        
        // Save new token
        repository.save(newTokenRecord);
        
        // Revoke the old token to prevent reuse
        oldTokenRecord.setReplacedByTokenId(newTokenRecord.getId());
        java.time.LocalDateTime now = java.time.LocalDateTime.now();
        oldTokenRecord.setRevokedAt(now);
        repository.save(oldTokenRecord);
        
        return newToken;
    }
    
    /**
     * Revoke all refresh tokens for a user (e.g., on password change).
     * Implements FR-009.
     */
    public void revokeAllTokensForUser(UUID userId) {
        Optional<RefreshToken> tokenRecord = repository.findByUserIdAndRevokedAtIsNull(userId);
        if (tokenRecord.isPresent()) {
            RefreshToken token = tokenRecord.get();
            token.setRevokedAt(java.time.LocalDateTime.now());
            repository.save(token);
        }
    }
    
    private String generateSecureToken() {
        SecureRandom random = new SecureRandom();
        byte[] tokenBytes = new byte[32];
        random.nextBytes(tokenBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(tokenBytes);
    }
    
    private String hashToken(String token) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(token.getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not available", e);
        }
    }
}
