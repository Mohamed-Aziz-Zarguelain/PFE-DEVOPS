package com.example.backendbeetrack.services;

import com.example.backendbeetrack.entities.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Optional;

public interface    UserService {
    List<User> getAllUsers();
    Optional<User> getUserById(Long id);
    User createUser(User user);
    Optional<User> findByEmail(String email);
    boolean checkPassword(String rawPassword, String encodedPassword);



    User updateUser(Long id, User userDetails);
    void deleteUser(Long id);

}