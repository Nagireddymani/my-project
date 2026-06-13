package com.example.auth.application.service;

import com.example.auth.adapter.out.persistence.UserRepository;
import com.example.auth.adapter.out.persistence.RefreshTokenRepository;
import com.example.auth.application.security.PasswordService;
import com.example.auth.application.security.TokenService;
import com.example.auth.application.security.RefreshTokenService;
import com.example.auth.adapter.in.web.ApiExceptionHandler.AuthenticationException;
import com.example.auth.adapter.in.web.ApiExceptionHandler.UserNotFoundException;
import com.example.auth.domain.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

/**
 * Service for user authentication operations: signup, login, logout.
 * Implements user stories 1 (signup/login) and 3 (refresh token renewal).
 * Part of application/service layer - domain logic focused.
 */
@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordService passwordService;
    private final TokenService tokenService;
    private final RefreshTokenService refreshTokenService;
    
    @Value("${auth.jwt.expiration}")
    private long accessTokenExpiration;
    
    public AuthenticationService(UserRepository userRepository,
                                 RefreshTokenRepository refreshTokenRepository,
                                 PasswordService passwordService,
                                 TokenService tokenService,
                                 RefreshTokenService refreshTokenService) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.passwordService = passwordService;
        this.tokenService = tokenService;
        this.refreshTokenService = refreshTokenService;
    }
    
    /**
     * Sign up a new user with email, password, and full name.
     * Implements FR-001: Create new user account with unique email.
     */
    public SignupResult signup(String email, String password, String fullName) {
        // Validate email format
        if (!isValidEmail(email)) {
            throw new AuthenticationException("INVALID_EMAIL", "Invalid email format");
        }
        
        // Check if email already exists
        Optional<User> existingUser = userRepository.findByEmail(email);
        if (existingUser.isPresent()) {
            throw new AuthenticationException("EMAIL_ALREADY_EXISTS", "An account with this email already exists");
        }
        
        // Validate password strength
        PasswordService.PasswordValidationResult pwValidation = passwordService.validatePassword(password);
        if (!pwValidation.isValid()) {
            throw new AuthenticationException("WEAK_PASSWORD", pwValidation.getMessage());
        }
        
        // Hash password
        String passwordHash = passwordService.hashPassword(password);
        
        // Create user
        User user = new User(email, passwordHash);
        user.setFullName(fullName);
        User savedUser = userRepository.save(user);
        
        // Generate tokens
        String accessToken = tokenService.generateAccessToken(savedUser.getId(), savedUser.getEmail());
        String refreshToken = refreshTokenService.generateRefreshToken(savedUser.getId());
        
        return new SignupResult(accessToken, refreshToken, accessTokenExpiration);
    }
    
    /**
     * Log in a user with email and password credentials.
     * Implements FR-002: Authenticate user with valid credentials and return JWT token.
     * Implements FR-005: Lock account after 5 failed attempts for 30 minutes.
     */
    public LoginResult login(String email, String password) {
        // Find user by email
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            throw new AuthenticationException("INVALID_CREDENTIALS", "Invalid email or password");
        }
        
        User user = userOpt.get();
        
        // Check if account is locked
        if (isAccountLocked(user)) {
            throw new AuthenticationException("ACCOUNT_LOCKED", "Account is temporarily locked due to failed login attempts. Please try again later.");
        }
        
        // Verify password
        boolean passwordValid = passwordService.verifyPassword(password, user.getPasswordHash());
        if (!passwordValid) {
            // Increment failed attempts
            incrementFailedAttempts(user);
            throw new AuthenticationException("INVALID_CREDENTIALS", "Invalid email or password");
        }
        
        // Check account status
        if (!"ACTIVE".equals(user.getStatus())) {
            throw new AuthenticationException("ACCOUNT_INACTIVE", "Your account is not active");
        }
        
        // Reset failed attempts on successful login
        user.setFailedLoginAttempts(0);
        user.setLockedUntil(null);
        userRepository.save(user);
        
        // Generate tokens
        String accessToken = tokenService.generateAccessToken(user.getId(), user.getEmail());
        String refreshToken = refreshTokenService.generateRefreshToken(user.getId());
        
        return new LoginResult(accessToken, refreshToken, accessTokenExpiration);
    }
    
    /**
     * Log out a user by revoking all refresh tokens.
     * Implements FR-004: Logout endpoint to revoke tokens.
     */
    public void logout(UUID userId) {
        refreshTokenService.revokeAllTokensForUser(userId);
    }
    
    private boolean isValidEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }
    
    private boolean isAccountLocked(User user) {
        if (user.getLockedUntil() == null) {
            return false;
        }
        if (java.time.LocalDateTime.now().isAfter(user.getLockedUntil())) {
            // Lock period expired, reset
            user.setLockedUntil(null);
            user.setFailedLoginAttempts(0);
            userRepository.save(user);
            return false;
        }
        return true;
    }
    
    private void incrementFailedAttempts(User user) {
        int attempts = user.getFailedLoginAttempts() != null ? user.getFailedLoginAttempts() : 0;
        attempts++;
        user.setFailedLoginAttempts(attempts);
        
        // Lock account after 5 failed attempts for 30 minutes
        if (attempts >= 5) {
            user.setLockedUntil(java.time.LocalDateTime.now().plusMinutes(30));
        }
        
        userRepository.save(user);
    }
    
    public static class SignupResult {
        public String accessToken;
        public String refreshToken;
        public long expiresIn;
        
        public SignupResult(String accessToken, String refreshToken, long expiresIn) {
            this.accessToken = accessToken;
            this.refreshToken = refreshToken;
            this.expiresIn = expiresIn;
        }
        
        public String getAccessToken() { return accessToken; }
        public String getRefreshToken() { return refreshToken; }
        public long getExpiresIn() { return expiresIn; }
    }
    
    public static class LoginResult {
        public String accessToken;
        public String refreshToken;
        public long expiresIn;
        
        public LoginResult(String accessToken, String refreshToken, long expiresIn) {
            this.accessToken = accessToken;
            this.refreshToken = refreshToken;
            this.expiresIn = expiresIn;
        }
        
        public String getAccessToken() { return accessToken; }
        public String getRefreshToken() { return refreshToken; }
        public long getExpiresIn() { return expiresIn; }
    }
}
