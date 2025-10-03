package com.example.backendbeetrack.repositories;

import com.example.backendbeetrack.entities.Tache;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TacheRepository extends JpaRepository<Tache, Long> {
    List<Tache> findByUserId(Long userId);
    @Query("SELECT COUNT(t) FROM Tache t WHERE t.user.id = :userId AND t.accomplie = true")
    long countAccompliesByUserId(@Param("userId") Long userId);

    @Query("SELECT COUNT(t) FROM Tache t WHERE t.user.id = :userId AND t.accomplie = false")
    long countEnCoursByUserId(@Param("userId") Long userId);

    @Query("SELECT t FROM Tache t WHERE t.user.id = :userId AND t.accomplie = false ORDER BY t.date ASC")
    List<Tache> findEnCoursByUserId(@Param("userId") Long userId);
}