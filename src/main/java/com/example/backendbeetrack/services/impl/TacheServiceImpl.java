package com.example.backendbeetrack.services.impl;

import com.example.backendbeetrack.entities.Tache;
import com.example.backendbeetrack.entities.User;
import com.example.backendbeetrack.repositories.*;
import com.example.backendbeetrack.services.TacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class TacheServiceImpl implements TacheService {

    @Autowired
    private TacheRepository tacheRepository;

    private final UserRepository userRepository;

    public TacheServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Override
    public Map<String, Object> getTachesStatutByEmail(String email) {
        User user = userRepository.findByEmail(email);
        long accomplies = tacheRepository.countAccompliesByUserId(user.getId());
        long enCours = tacheRepository.countEnCoursByUserId(user.getId());

        return Map.of(
                "accomplies", accomplies,
                "enCours", enCours,
                "total", accomplies + enCours
        );
    }

    @Override
    public List<Tache> getTachesEnCoursByEmail(String email) {
        User user = userRepository.findByEmail(email) ;
        return tacheRepository.findEnCoursByUserId(user.getId());
    }
    @Override
    public List<Tache> getAllTachesByUser(Long userId) {
        return tacheRepository.findByUserId(userId);
    }
    @Override
    public List<Tache> getAllTaches() {
        return tacheRepository.findAll();
    }

    @Override
    public Optional<Tache> getTacheById(Long id) {
        return tacheRepository.findById(id);
    }

    @Override
    public Tache createTache(Tache tache) {
        return tacheRepository.save(tache);
    }

    @Override
    public Tache updateTache(Long id, Tache tacheDetails) {
        Tache tache = tacheRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tache not found"));
        tache.setTitre(tacheDetails.getTitre());
        tache.setDescription(tacheDetails.getDescription());
        tache.setDate(tacheDetails.getDate());
        tache.setAccomplie(tacheDetails.isAccomplie());
        tache.setUser(tacheDetails.getUser());
        return tacheRepository.save(tache);
    }

    @Override
    public void deleteTache(Long id) {
        tacheRepository.deleteById(id);
    }
}