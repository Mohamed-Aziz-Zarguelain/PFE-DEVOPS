package com.example.backendbeetrack.repositories;

import com.example.backendbeetrack.entities.Intervention;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InterventionRepository extends JpaRepository<Intervention, Long> {
}