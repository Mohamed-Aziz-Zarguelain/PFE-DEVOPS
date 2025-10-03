package com.example.backendbeetrack.repositories;

import com.example.backendbeetrack.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findOptionalByEmail(String email);
    User findByEmail(String email);


}