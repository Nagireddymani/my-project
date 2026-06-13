package com.example.auth.adapter.in.web.dto;

/**
 * Request DTO for password reset.
 */
public class ResetPasswordRequest {
    public String resetToken;
    public String newPassword;
    
    public ResetPasswordRequest() {}
    
    public ResetPasswordRequest(String resetToken, String newPassword) {
        this.resetToken = resetToken;
        this.newPassword = newPassword;
    }
    
    public String getResetToken() { return resetToken; }
    public void setResetToken(String resetToken) { this.resetToken = resetToken; }
    
    public String getNewPassword() { return newPassword; }
    public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
}
