package com.tsp.repository;

import com.tsp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    
    // Method to find user by email (instead of username)
    Optional<User> findByEmail(String email);
}
