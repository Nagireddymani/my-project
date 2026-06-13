package com.example.auth.application.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Service for JWT access token generation and validation.
 * Implements Constitution Principle IV: API Contract Discipline & OpenAPI Documentation.
 * Part of application/security layer - domain-logic focused.
 */
@Service
public class TokenService {
    @Value("${auth.jwt.secret}")
    private String jwtSecret;
    
    @Value("${auth.jwt.expiration}")
    private long jwtExpiration;
    
    /**
     * Generate a JWT access token for an authenticated user.
     * Implements FR-003.
     */
    public String generateAccessToken(UUID userId, String email) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("sub", userId.toString());
        claims.put("email", email);
        claims.put("type", "access");
        
        return createToken(claims, jwtExpiration);
    }
    
    /**
     * Validate a JWT token and return user ID if valid.
     * Returns null if token is invalid or expired.
     */
    public UUID validateAndGetUserId(String token) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
            var claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            
            return UUID.fromString((String) claims.get("sub"));
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * Extract email from a valid JWT token.
     */
    public String extractEmail(String token) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
            var claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            
            return (String) claims.get("email");
        } catch (Exception e) {
            return null;
        }
    }
    
    private String createToken(Map<String, Object> claims, long expiration) {
        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
}
