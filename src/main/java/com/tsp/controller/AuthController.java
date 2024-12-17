package com.tsp.controller;

import com.tsp.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AuthService authService;

    // Show login page
    @GetMapping("/login")
    public String loginPage() {
        return "login"; // Return login.html page
    }

    // Process login form submission
    @PostMapping("/login")
    public String loginSubmit(@RequestParam String email, @RequestParam String password, Model model) {
        try {
            // Manually authenticate user using AuthenticationManager
            Authentication authentication = new UsernamePasswordAuthenticationToken(email, password);
            Authentication auth = authenticationManager.authenticate(authentication);

            // Set authentication in SecurityContextHolder
            SecurityContextHolder.getContext().setAuthentication(auth);

            // Redirect to welcome page after successful login
            return "redirect:/welcome"; // Redirect to /welcome after successful login
        } catch (BadCredentialsException e) {
            // Handle bad credentials
            model.addAttribute("error", "Invalid email or password");
            return "login";
        }
    }

    // Show register page
    @GetMapping("/register")
    public String showRegisterPage() {
        return "register"; // Return register.html page
    }

    // Process register form submission
    @PostMapping("/register")
    public String registerSubmit(@RequestParam String email, @RequestParam String password, Model model) {
        try {
            // Call the AuthService to save user details with encrypted password
            authService.registerUser(email, password);

            // Redirect to login page after successful registration
            return "redirect:/login"; // Redirect to login page after successful registration
        } catch (IllegalArgumentException e) {
            // Handle user already exists error
            model.addAttribute("error", "User with this email already exists.");
            return "register";
        }
    }
}
