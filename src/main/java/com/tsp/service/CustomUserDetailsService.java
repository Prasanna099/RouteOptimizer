package com.tsp.service;

import com.tsp.model.User;
import com.tsp.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Fetch user by email (use the field 'email' instead of 'username')
        User user = userRepository.findByEmail(email)  // Ensure this method is present in the repository
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        
        // Return a UserDetails object with the user data
        UserBuilder builder = org.springframework.security.core.userdetails.User.withUsername(user.getEmail());
        builder.password(user.getPassword());  // Ensure your User entity has getPassword()
        builder.roles("USER");  // You can customize roles based on your requirements

        return builder.build();
    }
}
