package com.example.backendbeetrack.services;

import com.example.backendbeetrack.entities.Tache;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface TacheService {
    List<Tache> getAllTaches();
    Optional<Tache> getTacheById(Long id);
    Tache createTache(Tache tache);
    Tache updateTache(Long id, Tache tacheDetails);
    Map<String, Object> getTachesStatutByEmail(String email);
    List<Tache> getTachesEnCoursByEmail(String email);
     List<Tache> getAllTachesByUser(Long userId) ;
    void deleteTache(Long id);
}