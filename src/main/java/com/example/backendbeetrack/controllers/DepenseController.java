package com.example.backendbeetrack.controllers;

import com.example.backendbeetrack.entities.Depense;
import com.example.backendbeetrack.entities.User;
import com.example.backendbeetrack.services.DepenseService;
import com.example.backendbeetrack.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/depenses")
@PreAuthorize("hasAnyRole('ADMIN', 'APICULTEUR')")
public class DepenseController {

    @Autowired
    private DepenseService depenseService;

    @Autowired
    private UserService userService;

    @GetMapping
    public List<Depense> getAllDepenses() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        // Vérifie le rôle
        boolean isAdmin = SecurityContextHolder.getContext()
                .getAuthentication()
                .getAuthorities()
                .stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        if (isAdmin) {
            return depenseService.getAllDepenses();
        } else {
            return depenseService.getDepensesByUserEmail(email);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Depense> getDepenseById(@PathVariable Long id) {
        Optional<Depense> depense = depenseService.getDepenseById(id);
        return depense.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Depense> createDepense(@RequestBody Depense depense) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> user = userService.findByEmail(email);

        depense.setUser(user.get());
        return ResponseEntity.ok(depenseService.createDepense(depense));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Depense> updateDepense(@PathVariable Long id, @RequestBody Depense depenseDetails) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> user = userService.findByEmail(email);

        depenseDetails.setUser(user.get());
        Depense updatedDepense = depenseService.updateDepense(id, depenseDetails);
        return ResponseEntity.ok(updatedDepense);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDepense(@PathVariable Long id) {
        depenseService.deleteDepense(id);
        return ResponseEntity.noContent().build();
    }
}