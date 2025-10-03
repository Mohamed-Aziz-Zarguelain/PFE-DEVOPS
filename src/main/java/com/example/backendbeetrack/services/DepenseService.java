package com.example.backendbeetrack.services;

import com.example.backendbeetrack.entities.Depense;
import java.util.List;
import java.util.Optional;

public interface DepenseService {
    List<Depense> getAllDepenses();
    Optional<Depense> getDepenseById(Long id);
    Depense createDepense(Depense depense);
     List<Depense> getDepensesByUserEmail(String email) ;
    Depense updateDepense(Long id, Depense depenseDetails);
    void deleteDepense(Long id);
}