package com.example.proyectoseguridaddllologin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {

    private boolean success;
    private String message;
    private String token;  // JWT token
    private UserInfo user;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserInfo {
        private Long id;
        private String username;
        private String fullName;
        private String race;
        private Long powerLevel;
        private String role;
    }

    public static LoginResponse success(String token, UserInfo user) {
        return new LoginResponse(true, "Login successful", token, user);
    }

    public static LoginResponse failure(String message) {
        return new LoginResponse(false, message, null, null);
    }
}
