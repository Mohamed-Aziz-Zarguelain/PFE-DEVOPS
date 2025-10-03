package com.example.backendbeetrack.services.impl;

import com.example.backendbeetrack.entities.Rucher;
import com.example.backendbeetrack.entities.User;
import com.example.backendbeetrack.repositories.RucherRepository;
import com.example.backendbeetrack.services.RucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class RucherServiceImpl implements RucherService {

    @Autowired
    private RucherRepository rucherRepository;

    @Override
    public List<Rucher> getAllRuchers() {
        return rucherRepository.findAll();
    }

    @Override
    public Optional<Rucher> getRucherByIdAndUserEmail(Long id, String email) {
        return rucherRepository.findByIdAndUserEmail(id, email);
    }
    @Override
    public List<Rucher> getRuchersByUser(User user) {
        return rucherRepository.findByUser(user);
    }

    @Override
    public Optional<Rucher> getRucherById(Long id) {
        return rucherRepository.findById(id);
    }

    @Override
    public Rucher createRucher(Rucher rucher) {
        return rucherRepository.save(rucher);
    }

    @Override
    public Rucher updateRucher(Long id, Rucher rucherDetails) {
        Rucher rucher = rucherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rucher not found"));
        rucher.setNom(rucherDetails.getNom());
        rucher.setLocalisation(rucherDetails.getLocalisation());
        rucher.setUser(rucherDetails.getUser());
        return rucherRepository.save(rucher);
    }

    @Override
    public boolean deleteRucher(Long id) {
        rucherRepository.deleteById(id);
        return false;
    }
}