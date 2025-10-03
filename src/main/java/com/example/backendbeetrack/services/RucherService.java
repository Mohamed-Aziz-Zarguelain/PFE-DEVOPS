package com.example.backendbeetrack.services;

import com.example.backendbeetrack.entities.Rucher;
import com.example.backendbeetrack.entities.User;

import java.util.List;
import java.util.Optional;

public interface RucherService {
    List<Rucher> getAllRuchers();

    Optional<Rucher> getRucherByIdAndUserEmail(Long id, String email);
    List<Rucher> getRuchersByUser(User user);

    Optional<Rucher> getRucherById(Long id);
    Rucher createRucher(Rucher rucher);
    Rucher updateRucher(Long id, Rucher rucherDetails);
    boolean deleteRucher(Long id);
}