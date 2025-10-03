package com.example.backendbeetrack.security;

import lombok.Data;

@Data
public class AuthResponse {
    private String token;
    private String role;
    private String email;
}