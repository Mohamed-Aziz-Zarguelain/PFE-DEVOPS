

package com.example.backendbeetrack.controllers;

import com.example.backendbeetrack.entities.Role;
import com.example.backendbeetrack.security.AuthRequest;
import com.example.backendbeetrack.security.AuthResponse;
import com.example.backendbeetrack.entities.User;
import com.example.backendbeetrack.security.ResgisterRequest;
import com.example.backendbeetrack.services.UserService;
import com.example.backendbeetrack.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    // POST /api/auth/register
    @PostMapping("/register")
    public ResponseEntity<?> register(@Validated @RequestBody ResgisterRequest request) {
        try {
            User user = User.builder()
                    .nom(request.getNom())
                    .prenom(request.getPrenom())
                    .email(request.getEmail())
                    .motDePasse(request.getPassword())
                    .role(Role.APICULTEUR)
                    .build();

            User savedUser = userService.createUser(user);

            String token = jwtUtil.generateToken(savedUser.getEmail(), savedUser.getRole().toString());

            AuthResponse response = new AuthResponse();
            response.setToken(token);
            response.setEmail(savedUser.getEmail());
            response.setRole(savedUser.getRole().toString());

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            if (e.getMessage().contains("already exists")) {
                return ResponseEntity.status(409) // 409 Conflict: Resource already exists
                        .body(Map.of("error", "Email already exists"));
            }
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }
    // POST /auth/login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        try {
            // 1. Find user by email
            User user = userService.findByEmail(request.getEmail())
                    .orElse(null);

            if (user == null) {
                return ResponseEntity.status(401).body(
                        Map.of("error", "Invalid email or password")
                );
            }

            // 2. Check password
            if (!userService.checkPassword(request.getMotDePasse(), user.getMotDePasse())) {
                return ResponseEntity.status(401).body(
                        Map.of("error", "Invalid email or password")
                );
            }

            // 3. Generate JWT token
            String token = jwtUtil.generateToken(user.getEmail(), user.getRole().toString());

            // 4. Return response
            AuthResponse response = new AuthResponse();
            response.setToken(token);
            response.setEmail(user.getEmail());
            response.setRole(user.getRole().toString());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                    Map.of("error", "Login failed")
            );
        }
    }
}