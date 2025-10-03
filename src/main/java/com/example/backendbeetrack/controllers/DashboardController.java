package com.example.backendbeetrack.controllers;// com.example.backendbeetrack.controller.DashboardController.java

import com.example.backendbeetrack.entities.User;
import com.example.backendbeetrack.repositories.UserRepository;
import com.example.backendbeetrack.services.DashboardService;
import com.example.backendbeetrack.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@PreAuthorize("hasAnyRole('ADMIN', 'APICULTEUR')")

public class DashboardController {
    private final UserRepository userRepository;
    private final DashboardService dashboardService;

    @Autowired
    public DashboardController(DashboardService dashboardService,UserRepository userRepository) {
        this.dashboardService = dashboardService;
        this.userRepository = userRepository; // ← Spring injecte l'implémentation
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getDashboard(Authentication authentication) {
        // Récupère l'email de l'utilisateur connecté
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        // Cherche l'utilisateur en base par email
        User user = userRepository.findByEmail(email);
        Long userId = user.getId();
        System.out.println("sdsddsdsdsss" +userId);
        Map<String, Object> dashboardData = dashboardService.getDashboardData(userId);
        return ResponseEntity.ok(dashboardData);
    }
    @GetMapping("/etat-ruches")
    public ResponseEntity<Map<String, Object>> getEtatRuches(Authentication authentication) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Map<String, Object> etatData = dashboardService.getEtatSanteRuchesByEmail(email);
        return ResponseEntity.ok(etatData);
    }
}