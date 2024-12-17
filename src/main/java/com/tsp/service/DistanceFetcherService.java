package com.tsp.service;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class DistanceFetcherService {

    // Hardcoded OpenRouteService API key
    private static final String API_KEY = "YOUR_API_KEY";
    
    // OpenRouteService Geocoding URL and Directions URL
    private static final String GEOCODING_API_URL = "https://api.openrouteservice.org/geocode/search";
    private static final String DIRECTIONS_API_URL = "https://api.openrouteservice.org/v2/directions/driving-car";

    // RestTemplate instance for making HTTP requests
    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * Fetch coordinates (longitude, latitude) for a given city name using OpenRouteService Geocoding API.
     * @param cityName The name of the city whose coordinates are to be fetched.
     * @return A string array containing longitude and latitude as the first and second elements respectively.
     */
    public String[] fetchCoordinates(String cityName) {
        try {
            // Encode the city name to handle spaces and special characters
            String encodedCityName = URLEncoder.encode(cityName, StandardCharsets.UTF_8.toString());
            String url = GEOCODING_API_URL + "?api_key=" + API_KEY + "&text=" + encodedCityName;

            // Log the request URL
            System.out.println("Fetching coordinates for city: " + cityName);
            System.out.println("Request URL: " + url);

            // Fetch the response from the API
            String response = restTemplate.getForObject(url, String.class);

            // Log the raw API response for debugging purposes
            System.out.println("API Response: " + response);

            // Parse the response into a JSONObject
            JSONObject json = new JSONObject(response);
            JSONArray features = json.optJSONArray("features");

            // Check if there are valid features and extract coordinates
            if (features != null && features.length() > 0) {
                JSONObject geometry = features.getJSONObject(0).optJSONObject("geometry");

                if (geometry != null) {
                    JSONArray coordinates = geometry.optJSONArray("coordinates");

                    // If coordinates are present, extract and return them
                    if (coordinates != null && coordinates.length() >= 2) {
                        // Ensure coordinates are being retrieved correctly
                        double longitude = coordinates.optDouble(0, Double.NaN);
                        double latitude = coordinates.optDouble(1, Double.NaN);

                        if (!Double.isNaN(longitude) && !Double.isNaN(latitude)) {
                            System.out.println("Coordinates for " + cityName + ": " + longitude + ", " + latitude);
                            return new String[]{String.valueOf(longitude), String.valueOf(latitude)};
                        } else {
                            throw new RuntimeException("Invalid coordinates for city: " + cityName);
                        }
                    }
                }
            }

            // If coordinates are not found, throw an exception
            throw new RuntimeException("No valid coordinates found for city: " + cityName);
        } catch (UnsupportedEncodingException e) {
            // Handle encoding errors
            e.printStackTrace();
            throw new RuntimeException("Error encoding city name: " + cityName);
        } catch (Exception e) {
            // Catch any other exceptions and log the error
            e.printStackTrace();
            throw new RuntimeException("Error fetching coordinates for city: " + cityName);
        }
    }

    /**
     * Fetch the distance between two locations using OpenRouteService's Directions API.
     * @param origin The origin coordinates [longitude, latitude].
     * @param destination The destination coordinates [longitude, latitude].
     * @return The distance between the two locations in kilometers.
     */
    public double fetchDistance(String[] origin, String[] destination) {
        try {
            String originCoords = origin[0] + "," + origin[1];
            String destinationCoords = destination[0] + "," + destination[1];

            // OpenRouteService Directions API URL
            String url = DIRECTIONS_API_URL + "?api_key=" + API_KEY
                    + "&start=" + originCoords + "&end=" + destinationCoords;

            // Log the request URL for debugging
            System.out.println("Request URL for distance: " + url);

            // Fetch the response from the API
            String response = restTemplate.getForObject(url, String.class);

            // Log the raw API response
            System.out.println("API Response: " + response);

            // Parse the response and extract the distance
            JSONObject json = new JSONObject(response);
            JSONArray features = json.optJSONArray("features");

            if (features != null && features.length() > 0) {
                JSONObject properties = features.getJSONObject(0).optJSONObject("properties");
                if (properties != null) {
                    JSONArray segments = properties.optJSONArray("segments");
                    if (segments != null && segments.length() > 0) {
                        JSONObject segment = segments.getJSONObject(0);
                        double distance = segment.optDouble("distance", 0); // Get the distance from the segment
                        if (distance > 0) {
                            return distance / 1000;  // Convert meters to kilometers
                        }
                    }
                }
            }

            // Return a default value in case of failure
            return 0.0;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error fetching distance from OpenRouteService API");
        }
    }
}
