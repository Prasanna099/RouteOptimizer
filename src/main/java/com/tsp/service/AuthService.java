package com.tsp.service;

import com.tsp.model.User;
import com.tsp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Register User Method
    public void registerUser(String email, String password) {
        // Check if user already exists
        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("User with this email already exists");
        }

        // Encode the password before saving
        String encodedPassword = passwordEncoder.encode(password);

        // Create and save user
        User user = new User();
        user.setEmail(email);
        user.setPassword(encodedPassword); // Store the encrypted password
        userRepository.save(user);
    }
}
