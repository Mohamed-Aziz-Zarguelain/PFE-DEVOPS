package com.example.backendbeetrack.repositories;

import com.example.backendbeetrack.entities.Ruche;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RucheRepository extends JpaRepository<Ruche, Long> {
    @Query("SELECT COUNT(r) FROM Ruche r WHERE r.rucher.user.id = :userId")
    long countByRucherUserId(@Param("userId") Long userId);
    @Query("SELECT r.etat, COUNT(r) FROM Ruche r WHERE r.rucher.user.id = :userId GROUP BY r.etat")
    List<Object[]> countByEtatAndUserId(@Param("userId") Long userId);
}