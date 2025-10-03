package com.example.backendbeetrack.controllers;

import com.example.backendbeetrack.entities.Intervention;
import com.example.backendbeetrack.entities.Ruche;
import com.example.backendbeetrack.services.InterventionService;
import com.example.backendbeetrack.services.RucheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("interventions")
public class InterventionController {

    @Autowired
    private InterventionService interventionService;

    @Autowired
    private RucheService rucheService;

    @GetMapping
    public List<Intervention> getAllInterventions() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"));

        if (isAdmin) {
            // ADMIN → toutes les interventions
            return interventionService.getAllInterventions();
        } else {
            // USER → uniquement ses interventions
            return interventionService.getAllInterventions().stream()
                    .filter(intervention -> intervention.getRuche()
                            .getRucher()
                            .getUser()
                            .getEmail()
                            .equals(email))
                    .toList();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Intervention> getInterventionById(@PathVariable Long id) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Intervention> intervention = interventionService.getInterventionById(id)
                .filter(i -> i.getRuche().getRucher().getUser().getEmail().equals(email));
        return intervention.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Intervention> createIntervention(@RequestBody Intervention intervention) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Ruche ruche = rucheService.getRucheById(intervention.getRuche().getId())
                .filter(r -> r.getRucher().getUser().getEmail().equals(email))
                .orElseThrow(() -> new RuntimeException("Ruche not found or not authorized"));
        intervention.setRuche(ruche);
        return ResponseEntity.ok(interventionService.createIntervention(intervention));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Intervention> updateIntervention(@PathVariable Long id, @RequestBody Intervention interventionDetails) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Ruche ruche = rucheService.getRucheById(interventionDetails.getRuche().getId())
                .filter(r -> r.getRucher().getUser().getEmail().equals(email))
                .orElseThrow(() -> new RuntimeException("Ruche not found or not authorized"));
        interventionDetails.setRuche(ruche);
        Intervention updatedIntervention = interventionService.updateIntervention(id, interventionDetails);
        return ResponseEntity.ok(updatedIntervention);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIntervention(@PathVariable Long id) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Intervention> intervention = interventionService.getInterventionById(id)
                .filter(i -> i.getRuche().getRucher().getUser().getEmail().equals(email));
        if (intervention.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        interventionService.deleteIntervention(id);
        return ResponseEntity.noContent().build();
    }
}