package com.example.auth.adapter.in.web.dto;

/**
 * Response DTO for successful authentication (signup or login).
 */
public class AuthenticationResponse {
    public String accessToken;
    public String refreshToken;
    public long expiresIn;
    
    public AuthenticationResponse() {}
    
    public AuthenticationResponse(String accessToken, String refreshToken, long expiresIn) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expiresIn = expiresIn;
    }
    
    public String getAccessToken() { return accessToken; }
    public void setAccessToken(String accessToken) { this.accessToken = accessToken; }
    
    public String getRefreshToken() { return refreshToken; }
    public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }
    
    public long getExpiresIn() { return expiresIn; }
    public void setExpiresIn(long expiresIn) { this.expiresIn = expiresIn; }
}
