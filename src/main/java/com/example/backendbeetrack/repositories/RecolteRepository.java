package com.example.backendbeetrack.repositories;

import com.example.backendbeetrack.entities.Recolte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecolteRepository extends JpaRepository<Recolte, Long> {
    @Query("SELECT COUNT(r) FROM Recolte r WHERE r.rucher.user.id = :userId")
    long countByRucherUserId(@Param("userId") Long userId);

    @Query("SELECT r FROM Recolte r WHERE r.rucher.user.id = :userId ")
    List<Recolte> findMielRecoltesByUserId(@Param("userId") Long userId);
}