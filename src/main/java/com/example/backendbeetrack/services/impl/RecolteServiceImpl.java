package com.example.backendbeetrack.services.impl;

import com.example.backendbeetrack.entities.Recolte;
import com.example.backendbeetrack.repositories.RecolteRepository;
import com.example.backendbeetrack.services.RecolteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class RecolteServiceImpl implements RecolteService {

    @Autowired
    private RecolteRepository recolteRepository;

    @Override
    public List<Recolte> getAllRecoltes() {
        return recolteRepository.findAll();
    }

    @Override
    public Optional<Recolte> getRecolteById(Long id) {
        return recolteRepository.findById(id);
    }

    @Override
    public Recolte createRecolte(Recolte recolte) {
        return recolteRepository.save(recolte);
    }

    @Override
    public Recolte updateRecolte(Long id, Recolte recolteDetails) {
        Recolte recolte = recolteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recolte not found"));
        recolte.setDate(recolteDetails.getDate());
        recolte.setTypeMiel(recolteDetails.getTypeMiel());
        recolte.setQuantite(recolteDetails.getQuantite());
        recolte.setObservation(recolteDetails.getObservation());
        recolte.setRucher(recolteDetails.getRucher());
        return recolteRepository.save(recolte);
    }

    @Override
    public void deleteRecolte(Long id) {
        recolteRepository.deleteById(id);
    }
}