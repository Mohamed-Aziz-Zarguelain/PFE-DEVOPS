package com.example.backendbeetrack.services;

import com.example.backendbeetrack.entities.Revenu;
import java.util.List;
import java.util.Optional;

public interface RevenuService {
    List<Revenu> getAllRevenus();
    Optional<Revenu> getRevenuById(Long id);
    Revenu createRevenu(Revenu revenu);
    Revenu updateRevenu(Long id, Revenu revenuDetails);

     List<Revenu> getRevenusByUserEmail(String email) ;
    void deleteRevenu(Long id);
}