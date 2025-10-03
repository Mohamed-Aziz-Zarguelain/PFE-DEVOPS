package com.example.backendbeetrack.services.impl;

import com.example.backendbeetrack.entities.Depense;
import com.example.backendbeetrack.repositories.DepenseRepository;
import com.example.backendbeetrack.services.DepenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class DepenseServiceImpl implements DepenseService {

    @Autowired
    private DepenseRepository depenseRepository;

    @Override
    public List<Depense> getAllDepenses() {
        return depenseRepository.findAll();
    }
    @Override
    public List<Depense> getDepensesByUserEmail(String email) {
        return depenseRepository.findByUserEmail(email);
    }
    @Override
    public Optional<Depense> getDepenseById(Long id) {
        return depenseRepository.findById(id);
    }

    @Override
    public Depense createDepense(Depense depense) {
        return depenseRepository.save(depense);
    }

    @Override
    public Depense updateDepense(Long id, Depense depenseDetails) {
        Depense depense = depenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Depense not found"));
        depense.setCategorie(depenseDetails.getCategorie());
        depense.setMontant(depenseDetails.getMontant());
        depense.setDate(depenseDetails.getDate());
        depense.setUser(depenseDetails.getUser());
        return depenseRepository.save(depense);
    }

    @Override
    public void deleteDepense(Long id) {
        depenseRepository.deleteById(id);
    }
}