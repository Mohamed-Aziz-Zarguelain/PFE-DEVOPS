package com.example.backendbeetrack.controllers;

import com.example.backendbeetrack.entities.Revenu;
import com.example.backendbeetrack.entities.User;
import com.example.backendbeetrack.services.RevenuService;
import com.example.backendbeetrack.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/revenus")
@PreAuthorize("hasAnyRole('ADMIN', 'APICULTEUR')")
public class RevenuController {

    @Autowired
    private RevenuService revenuService;

    @Autowired
    private UserService userService;

    @GetMapping
    public List<Revenu> getAllRevenus() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        // Vérifie si l'utilisateur est ADMIN
        boolean isAdmin = SecurityContextHolder.getContext()
                .getAuthentication()
                .getAuthorities()
                .stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        if (isAdmin) {
            return revenuService.getAllRevenus();
        } else {
            return revenuService.getRevenusByUserEmail(email);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Revenu> getRevenuById(@PathVariable Long id) {
        Optional<Revenu> revenu = revenuService.getRevenuById(id);
        return revenu.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Revenu> createRevenu(@RequestBody Revenu revenu) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> user = userService.findByEmail(email);
        revenu.setUser(user.get());
        return ResponseEntity.ok(revenuService.createRevenu(revenu));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Revenu> updateRevenu(@PathVariable Long id, @RequestBody Revenu revenuDetails) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> user = userService.findByEmail(email);

        revenuDetails.setUser(user.get());
        Revenu updatedRevenu = revenuService.updateRevenu(id, revenuDetails);
        return ResponseEntity.ok(updatedRevenu);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRevenu(@PathVariable Long id) {
        revenuService.deleteRevenu(id);
        return ResponseEntity.noContent().build();
    }
}