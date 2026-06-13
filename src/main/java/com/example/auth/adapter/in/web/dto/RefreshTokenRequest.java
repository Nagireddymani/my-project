package com.example.auth.adapter.in.web.dto;

/**
 * Request DTO for token refresh.
 */
public class RefreshTokenRequest {
    public String refreshToken;
    
    public RefreshTokenRequest() {}
    
    public RefreshTokenRequest(String refreshToken) {
        this.refreshToken = refreshToken;
    }
    
    public String getRefreshToken() { return refreshToken; }
    public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }
}
