package com.example.auth.adapter.in.web.dto;

/**
 * Request DTO for password recovery.
 */
public class ForgotPasswordRequest {
    public String email;
    
    public ForgotPasswordRequest() {}
    
    public ForgotPasswordRequest(String email) {
        this.email = email;
    }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
