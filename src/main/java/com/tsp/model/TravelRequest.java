package com.tsp.model;

import lombok.Data;
import java.util.List;

@Data
public class TravelRequest {
    private List<String> cities;  // List of cities to calculate the TSP route
    private double distance;      // Total distance of the TSP route
    private List<String> route;   // The ordered cities in the optimal route
}
