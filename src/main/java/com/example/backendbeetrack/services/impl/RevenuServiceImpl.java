package com.example.backendbeetrack.services.impl;

import com.example.backendbeetrack.entities.Revenu;
import com.example.backendbeetrack.repositories.RevenuRepository;
import com.example.backendbeetrack.services.RevenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class RevenuServiceImpl implements RevenuService {

    @Autowired
    private RevenuRepository revenuRepository;

    @Override
    public List<Revenu> getAllRevenus() {
        return revenuRepository.findAll();
    }
@Override
public List<Revenu> getRevenusByUserEmail(String email) {
    return revenuRepository.findByUserEmail(email);
}
    @Override
    public Optional<Revenu> getRevenuById(Long id) {
        return revenuRepository.findById(id);
    }

    @Override
    public Revenu createRevenu(Revenu revenu) {
        return revenuRepository.save(revenu);
    }

    @Override
    public Revenu updateRevenu(Long id, Revenu revenuDetails) {
        Revenu revenu = revenuRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Revenu not found"));
        revenu.setSource(revenuDetails.getSource());
        revenu.setMontant(revenuDetails.getMontant());
        revenu.setDate(revenuDetails.getDate());
        revenu.setUser(revenuDetails.getUser());
        return revenuRepository.save(revenu);
    }

    @Override
    public void deleteRevenu(Long id) {
        revenuRepository.deleteById(id);
    }
}