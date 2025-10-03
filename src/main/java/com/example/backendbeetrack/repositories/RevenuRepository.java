package com.example.backendbeetrack.repositories;

import com.example.backendbeetrack.entities.Depense;
import com.example.backendbeetrack.entities.Revenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RevenuRepository extends JpaRepository<Revenu, Long> {
    List<Revenu> findByUserEmail(String email);
    @Query("SELECT COALESCE(SUM(r.montant), 0.0) FROM Revenu r WHERE r.user.id = :userId")
    Double sumMontantByUserId(@Param("userId") Long userId);

}