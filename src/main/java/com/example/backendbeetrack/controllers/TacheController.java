package com.example.backendbeetrack.controllers;

import com.example.backendbeetrack.entities.Tache;
import com.example.backendbeetrack.entities.User;
import com.example.backendbeetrack.services.TacheService;
import com.example.backendbeetrack.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/taches")
public class TacheController {

    @Autowired
    private TacheService tacheService;

    @Autowired
    private UserService userService;
    @GetMapping("/statut")
    public ResponseEntity<Map<String, Object>> getTachesStatut(Authentication auth) {
        String email = auth.getName();
        return ResponseEntity.ok(tacheService.getTachesStatutByEmail(email));
    }

    @GetMapping("/en-cours")
    public ResponseEntity<List<Tache>> getTachesEnCours(Authentication auth) {
        String email = auth.getName();
        return ResponseEntity.ok(tacheService.getTachesEnCoursByEmail(email));
    }
    @GetMapping
    public List<Tache> getAllTaches() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByEmail(email).orElseThrow();

        if (user.getRole().name().equals("ROLE_ADMIN")) {
            // Retourner toutes les tâches
            return tacheService.getAllTaches();
        } else {
            // Retourner uniquement les tâches de l'utilisateur connecté
            return tacheService.getAllTachesByUser(user.getId());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tache> getTacheById(@PathVariable Long id) {
        Optional<Tache> tache = tacheService.getTacheById(id);
        return tache.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Tache> createTache(@RequestBody Tache tache) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> user = userService.findByEmail(email);

        tache.setUser(user.get());
        return ResponseEntity.ok(tacheService.createTache(tache));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Tache> updateTache(@PathVariable Long id, @RequestBody Tache tacheDetails) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> user = userService.findByEmail(email);

        tacheDetails.setUser(user.get());
        Tache updatedTache = tacheService.updateTache(id, tacheDetails);
        return ResponseEntity.ok(updatedTache);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTache(@PathVariable Long id) {
        tacheService.deleteTache(id);
        return ResponseEntity.noContent().build();
    }
}