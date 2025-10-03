package com.example.backendbeetrack.controllers;

import com.example.backendbeetrack.entities.Rucher;
import com.example.backendbeetrack.entities.User;
import com.example.backendbeetrack.services.RucherService;
import com.example.backendbeetrack.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
@RestController
@RequestMapping("/ruchers")
public class RucherController {

    @Autowired
    private RucherService rucherService;

    @Autowired
    private UserService userService;

    @GetMapping
    public List<Rucher> getAllRuchers() {
        // Récupérer l'authentification
        var auth = SecurityContextHolder.getContext().getAuthentication();

        // Vérifier si l'utilisateur est ADMIN
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
        auth.getAuthorities().forEach(grantedAuthority ->
                System.out.println("ROLE = " + grantedAuthority.getAuthority())
        );
        if (isAdmin) {
            // Si ADMIN, retourner tous les ruchers
            return rucherService.getAllRuchers();
        } else {
            // Sinon, retourner uniquement les ruchers de l'utilisateur connecté
            String email = auth.getName();
            User user = userService.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
            return rucherService.getRuchersByUser(user);
        }
    }

    @GetMapping("/my")
    public List<Rucher> getAllRuchersByUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        return rucherService.getRuchersByUser(user);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Rucher> getRucherById(@PathVariable Long id) {
        Optional<Rucher> rucher = rucherService.getRucherById(id);
        return rucher.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Rucher> createRucher(@RequestBody Rucher rucher) {

        System.out.println("hahaahhaahha");
        String email =  SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println(email);
        Optional<User> user = userService.findByEmail(email);
        rucher.setUser(user.get());
        return ResponseEntity.ok(rucherService.createRucher(rucher));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Rucher> updateRucher(@PathVariable Long id, @RequestBody Rucher rucherDetails) {
        System.out.println("hahaahhaahha");
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> user = userService.findByEmail(email);
        System.out.println("hahaahhaahha");

        rucherDetails.setUser(user.get());
        Rucher updatedRucher = rucherService.updateRucher(id, rucherDetails);
        return ResponseEntity.ok(updatedRucher);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRucher(@PathVariable Long id) {
        rucherService.deleteRucher(id);
        return ResponseEntity.noContent().build();
    }
}