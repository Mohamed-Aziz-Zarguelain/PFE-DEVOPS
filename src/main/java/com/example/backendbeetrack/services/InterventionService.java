package com.example.backendbeetrack.services;

import com.example.backendbeetrack.entities.Intervention;
import java.util.List;
import java.util.Optional;

public interface InterventionService {
    List<Intervention> getAllInterventions();
    Optional<Intervention> getInterventionById(Long id);
    Intervention createIntervention(Intervention intervention);
    Intervention updateIntervention(Long id, Intervention interventionDetails);
    void deleteIntervention(Long id);
}