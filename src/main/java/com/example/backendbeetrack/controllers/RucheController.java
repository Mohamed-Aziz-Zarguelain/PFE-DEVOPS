package com.example.backendbeetrack.controllers;

import com.example.backendbeetrack.entities.Ruche;
import com.example.backendbeetrack.entities.Rucher;
import com.example.backendbeetrack.services.RucheService;
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
@RequestMapping("/ruches")
public class RucheController {

    @Autowired
    private RucheService rucheService;

    @Autowired
    private RucherService rucherService;

    @GetMapping
    public List<Ruche> getAllRuches() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"));

        if (isAdmin) {
            // Admin → toutes les ruches
            return rucheService.getAllRuches();
        } else {
            // Utilisateur normal → uniquement ses ruches
            return rucheService.getAllRuches().stream()
                    .filter(ruche -> ruche.getRucher().getUser().getEmail().equals(email))
                    .toList();
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<Ruche> getRucheById(@PathVariable Long id) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        Optional<Ruche> ruche = rucheService.getRucheById(id)
                .filter(r -> r.getRucher().getUser().getEmail().equals(email));
        return ruche.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Ruche> createRuche(@RequestBody Ruche ruche) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName(  );
        Rucher rucher = rucherService.getRucherById(ruche.getRucher().getId())
                .filter(r -> r.getUser().getEmail().equals(email))
                .orElseThrow(() -> new RuntimeException("Rucher not found or not authorized"));
        ruche.setRucher(rucher);
        return ResponseEntity.ok(rucheService.createRuche(ruche));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Ruche> updateRuche(@PathVariable Long id, @RequestBody Ruche rucheDetails) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Rucher rucher = rucherService.getRucherById(rucheDetails.getRucher().getId())
                .filter(r -> r.getUser().getEmail().equals(email))
                .orElseThrow(() -> new RuntimeException("Rucher not found or not authorized"));
        rucheDetails.setRucher(rucher);
        Ruche updatedRuche = rucheService.updateRuche(id, rucheDetails);
        return ResponseEntity.ok(updatedRuche);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRuche(@PathVariable Long id) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Ruche> ruche = rucheService.getRucheById(id)
                .filter(r -> r.getRucher().getUser().getEmail().equals(email));
        if (ruche.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        rucheService.deleteRuche(id);
        return ResponseEntity.noContent().build();
    }
}