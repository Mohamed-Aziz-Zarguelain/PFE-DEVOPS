package com.example.backendbeetrack.services;

import com.example.backendbeetrack.entities.Recolte;
import java.util.List;
import java.util.Optional;

public interface RecolteService {
    List<Recolte> getAllRecoltes();
    Optional<Recolte> getRecolteById(Long id);
    Recolte createRecolte(Recolte recolte);
    Recolte updateRecolte(Long id, Recolte recolteDetails);
    void deleteRecolte(Long id);
}