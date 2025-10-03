package com.example.backendbeetrack.controllers;

import com.example.backendbeetrack.entities.Recolte;
import com.example.backendbeetrack.entities.Rucher;
import com.example.backendbeetrack.services.RecolteService;
import com.example.backendbeetrack.services.RucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/recoltes")
public class RecolteController {

    @Autowired
    private RecolteService recolteService;

    @Autowired
    private RucherService rucherService;

    @GetMapping
    public List<Recolte> getAllRecoltes() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"));

        if (isAdmin) {
            // ADMIN → toutes les récoltes
            return recolteService.getAllRecoltes();
        } else {
            // USER → uniquement ses récoltes
            return recolteService.getAllRecoltes().stream()
                    .filter(recolte -> recolte.getRucher().getUser().getEmail().equals(email))
                    .toList();
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<Recolte> getRecolteById(@PathVariable Long id) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Recolte> recolte = recolteService.getRecolteById(id)
                .filter(r -> r.getRucher().getUser().getEmail().equals(email));
        return recolte.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Recolte> createRecolte(@RequestBody Recolte recolte) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Rucher rucher = rucherService.getRucherById(recolte.getRucher().getId())
                .filter(r -> r.getUser().getEmail().equals(email))
                .orElseThrow(() -> new RuntimeException("Rucher not found or not authorized"));
        recolte.setRucher(rucher);
        return ResponseEntity.ok(recolteService.createRecolte(recolte));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Recolte> updateRecolte(@PathVariable Long id, @RequestBody Recolte recolteDetails) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Rucher rucher = rucherService.getRucherById(recolteDetails.getRucher().getId())
                .filter(r -> r.getUser().getEmail().equals(email))
                .orElseThrow(() -> new RuntimeException("Rucher not found or not authorized"));
        recolteDetails.setRucher(rucher);
        Recolte updatedRecolte = recolteService.updateRecolte(id, recolteDetails);
        return ResponseEntity.ok(updatedRecolte);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecolte(@PathVariable Long id) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Recolte> recolte = recolteService.getRecolteById(id)
                .filter(r -> r.getRucher().getUser().getEmail().equals(email));
        if (recolte.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        recolteService.deleteRecolte(id);
        return ResponseEntity.noContent().build();
    }
}