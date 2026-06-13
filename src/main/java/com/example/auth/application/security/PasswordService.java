package com.example.auth.application.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service for password hashing, validation, and strength checking.
 * Implements Constitution Principle III: Test Coverage & JUnit 5 Quality Gate.
 * Part of application/security layer - domain-logic focused.
 */
@Service
public class PasswordService {
    private final PasswordEncoder passwordEncoder;
    
    public PasswordService() {
        // Use 12 rounds per plan documentation (OWASP recommended for 2026+)
        this.passwordEncoder = new BCryptPasswordEncoder(12);
    }
    
    /**
     * Hash a plaintext password using Bcrypt.
     */
    public String hashPassword(String plainPassword) {
        return passwordEncoder.encode(plainPassword);
    }
    
    /**
     * Verify a plaintext password against a stored hash.
     */
    public boolean verifyPassword(String plainPassword, String hash) {
        return passwordEncoder.matches(plainPassword, hash);
    }
    
    /**
     * Validate password strength: minimum 8 characters with uppercase, lowercase, and digit.
     * Implements FR-010.
     */
    public boolean isStrongPassword(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }
        
        boolean hasUppercase = password.matches(".*[A-Z].*");
        boolean hasLowercase = password.matches(".*[a-z].*");
        boolean hasDigit = password.matches(".*\\d.*");
        
        return hasUppercase && hasLowercase && hasDigit;
    }
    
    /**
     * Validate password strength with detailed error message.
     */
    public PasswordValidationResult validatePassword(String password) {
        if (password == null) {
            return new PasswordValidationResult(false, "Password cannot be null");
        }
        
        if (password.length() < 8) {
            return new PasswordValidationResult(false, "Password must be at least 8 characters");
        }
        
        if (!password.matches(".*[A-Z].*")) {
            return new PasswordValidationResult(false, "Password must contain at least one uppercase letter");
        }
        
        if (!password.matches(".*[a-z].*")) {
            return new PasswordValidationResult(false, "Password must contain at least one lowercase letter");
        }
        
        if (!password.matches(".*\\d.*")) {
            return new PasswordValidationResult(false, "Password must contain at least one digit");
        }
        
        return new PasswordValidationResult(true, "Password is strong");
    }
    
    public static class PasswordValidationResult {
        public boolean valid;
        public String message;
        
        public PasswordValidationResult(boolean valid, String message) {
            this.valid = valid;
            this.message = message;
        }
        
        public boolean isValid() { return valid; }
        public String getMessage() { return message; }
    }
}
