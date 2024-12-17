package com.tsp.service;

import com.tsp.model.TravelRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TspSolverService {

    @Autowired
    private DistanceFetcherService distanceFetcherService;

    public TravelRequest solveTsp(TravelRequest request, String userEmail) {
        List<String> cities = request.getCities();
        if (cities == null || cities.isEmpty()) {
            throw new RuntimeException("City list cannot be empty!");
        }

        // Step 1: Fetch coordinates for each city
        Map<String, String[]> cityCoordinates = new HashMap<>();
        for (String city : cities) {
            cityCoordinates.put(city, distanceFetcherService.fetchCoordinates(city));
        }

        // Step 2: Calculate distances between all pairs of cities
        int n = cities.size();
        double[][] distanceMatrix = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i != j) {
                    String[] originCoords = cityCoordinates.get(cities.get(i));
                    String[] destinationCoords = cityCoordinates.get(cities.get(j));
                    distanceMatrix[i][j] = distanceFetcherService.fetchDistance(originCoords, destinationCoords);
                } else {
                    distanceMatrix[i][j] = 0.0; // Distance to self is zero
                }
            }
        }

        // Step 3: Solve TSP using nearest neighbor heuristic
        List<String> visitedCities = new ArrayList<>();
        boolean[] visited = new boolean[n];
        int currentCityIndex = 0;
        visited[0] = true;
        visitedCities.add(cities.get(0));
        double totalDistance = 0.0;

        for (int count = 1; count < n; count++) {
            double minDistance = Double.MAX_VALUE;
            int nextCityIndex = -1;

            for (int j = 0; j < n; j++) {
                if (!visited[j] && distanceMatrix[currentCityIndex][j] < minDistance) {
                    minDistance = distanceMatrix[currentCityIndex][j];
                    nextCityIndex = j;
                }
            }

            visited[nextCityIndex] = true;
            visitedCities.add(cities.get(nextCityIndex));
            totalDistance += distanceMatrix[currentCityIndex][nextCityIndex];
            currentCityIndex = nextCityIndex;
        }

        // Step 4: Return to the starting city
        totalDistance += distanceMatrix[currentCityIndex][0];
        visitedCities.add(cities.get(0));

        // Step 5: Prepare response
        TravelRequest result = new TravelRequest();
        result.setRoute(visitedCities);
        result.setDistance(totalDistance);

        return result;
    }
}
