package com.example.backendbeetrack.repositories;

import com.example.backendbeetrack.entities.Depense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepenseRepository extends JpaRepository<Depense, Long> {
    List<Depense> findByUserEmail(String email);
    @Query("SELECT COALESCE(SUM(d.montant), 0.0) FROM Depense d WHERE d.user.id = :userId")
    Double sumMontantByUserId(@Param("userId") Long userId);
}