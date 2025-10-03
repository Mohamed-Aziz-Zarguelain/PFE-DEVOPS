package com.example.backendbeetrack.services.impl;

import com.example.backendbeetrack.entities.Intervention;
import com.example.backendbeetrack.repositories.InterventionRepository;
import com.example.backendbeetrack.services.InterventionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class InterventionServiceImpl implements InterventionService {

    @Autowired
    private InterventionRepository interventionRepository;

    @Override
    public List<Intervention> getAllInterventions() {
        return interventionRepository.findAll();
    }

    @Override
    public Optional<Intervention> getInterventionById(Long id) {
        return interventionRepository.findById(id);
    }

    @Override
    public Intervention createIntervention(Intervention intervention) {
        return interventionRepository.save(intervention);
    }

    @Override
    public Intervention updateIntervention(Long id, Intervention interventionDetails) {
        Intervention intervention = interventionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Intervention not found"));
        intervention.setDate(interventionDetails.getDate());
        intervention.setRemarque(interventionDetails.getRemarque());
        intervention.setRuche(interventionDetails.getRuche());
        return interventionRepository.save(intervention);
    }

    @Override
    public void deleteIntervention(Long id) {
        interventionRepository.deleteById(id);
    }
}