package com.tsp.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;  // Password should be encoded before saving

    // Add a method to encode the password before setting it
    public void setPassword(String password) {
        this.password = password;  // Here you can encode the password using BCryptPasswordEncoder before setting it
    }
}
