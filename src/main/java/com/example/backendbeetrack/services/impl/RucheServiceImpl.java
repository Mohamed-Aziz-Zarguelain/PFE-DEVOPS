package com.example.backendbeetrack.services.impl;

import com.example.backendbeetrack.entities.Ruche;
import com.example.backendbeetrack.repositories.RucheRepository;
import com.example.backendbeetrack.services.RucheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class RucheServiceImpl implements RucheService {

    @Autowired
    private RucheRepository rucheRepository;

    @Override
    public List<Ruche> getAllRuches() {
        return rucheRepository.findAll();
    }

    @Override
    public Optional<Ruche> getRucheById(Long id) {
        return rucheRepository.findById(id);
    }

    @Override
    public Ruche createRuche(Ruche ruche) {
        return rucheRepository.save(ruche);
    }

    @Override
    public Ruche updateRuche(Long id, Ruche rucheDetails) {
        Ruche ruche = rucheRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ruche not found"));
        ruche.setCode(rucheDetails.getCode());
        ruche.setType(rucheDetails.getType());
        ruche.setEtat(rucheDetails.getEtat());
        ruche.setRucher(rucheDetails.getRucher());
        return rucheRepository.save(ruche);
    }

    @Override
    public void deleteRuche(Long id) {
        rucheRepository.deleteById(id);
    }
}