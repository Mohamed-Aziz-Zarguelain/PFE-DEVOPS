package com.example.backendbeetrack.security;

import lombok.Data;

@Data
public class AuthRequest {
    private String email;   
    private String motDePasse;
}