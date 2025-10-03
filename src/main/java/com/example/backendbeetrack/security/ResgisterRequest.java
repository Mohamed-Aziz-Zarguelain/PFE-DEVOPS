package com.example.backendbeetrack.security;

import lombok.Data;

@Data
public class ResgisterRequest {

    private String nom;
    private String prenom;
    private String email;
    private String password;
}
