package com.tsp.controller;

import com.tsp.model.TravelRequest;
import com.tsp.service.TspSolverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;

@RestController
public class TspController {

    @Autowired
    private TspSolverService tspSolverService;

    // Show the TSP solver page (GET)
    @GetMapping("/tsp")
    public String showTspPage() {
        return "tsp"; // Return the HTML page for users to enter cities
    }

    // Handle TSP form submission (POST)
    @PostMapping("/tsp/solve")
    public ResponseEntity<?> solveTsp(@RequestBody TravelRequest travelRequest, Authentication authentication) {
        try {
            // Ensure cities list is not empty
            if (travelRequest.getCities() == null || travelRequest.getCities().isEmpty()) {
                return ResponseEntity.badRequest().body("Please provide a list of cities.");
            }

            // Retrieve the user email from Authentication object (assuming email is used as user identifier)
            String userEmail = authentication.getName();  // Get the email (not userId)

            // Call TSP Solver Service to calculate the optimal route
            TravelRequest optimalRoute = tspSolverService.solveTsp(travelRequest, userEmail);

            // Return the optimal route and total distance as a JSON response
            return ResponseEntity.ok(optimalRoute);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error solving TSP: " + e.getMessage());
        }
    }
}
