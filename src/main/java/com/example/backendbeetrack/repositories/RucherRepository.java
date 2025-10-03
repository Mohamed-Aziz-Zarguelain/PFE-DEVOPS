package com.example.backendbeetrack.repositories;

import com.example.backendbeetrack.entities.Rucher;
import com.example.backendbeetrack.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RucherRepository extends JpaRepository<Rucher, Long> {
    List<Rucher> findByUser(User user);
    Optional<Rucher> findByIdAndUserEmail(Long id, String email);

    List<Rucher> findByUserId(Long userId);

}