package com.example.backendbeetrack.services;

import com.example.backendbeetrack.entities.Ruche;
import java.util.List;
import java.util.Optional;

public interface RucheService {
    List<Ruche> getAllRuches();

    Optional<Ruche> getRucheById(Long id);
    Ruche createRuche(Ruche ruche);
    Ruche updateRuche(Long id, Ruche rucheDetails);
    void deleteRuche(Long id);
}