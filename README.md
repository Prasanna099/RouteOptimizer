# RouteOptimizer - A Travel Planner and TSP Solver

## Overview

RouteOptimizer is a Spring Boot-based web application that serves as a travel planner and solves the **Travelling Salesman Problem (TSP)**. Users can input a list of cities, and the application calculates the optimal travel route using a **nearest neighbor algorithm** with real-time distance data from the **OpenRouteService API**.

## Features

- **User Authentication**: Secure login and registration functionality.
- **Real-time Distance Calculation**: Fetches real-time distance data using OpenRouteService API.
- **TSP Solver**: Optimizes travel routes using the nearest neighbor algorithm.
- **JSON Input/Output**: Accepts JSON data and returns the optimal route and total distance.
- **Error Handling**: Provides user-friendly error messages.

## Technologies Used

- **Backend**: Java, Spring Boot
- **Database**: MySQL
- **API Integration**: OpenRouteService API
- **Security**: Spring Security, OAuth2 (Google Login)
- **Tools & Libraries**: Lombok, Maven, RestTemplate

## Endpoints

The following endpoints are available in the project:

| Method | Endpoint     | Description                                    |
| ------ | ------------ | ---------------------------------------------- |
| POST   | `/login`     | Logs in a user                                 |
| POST   | `/register`  | Registers a new user                           |
| POST   | `/tsp/solve` | Solves the TSP and returns the optimized route |

### Sample JSON Input for `/tsp/solve`

```json
{
    "cities": ["New York", "Los Angeles", "Chicago", "Houston"]
}
```

### Sample JSON Output for `/tsp/solve`

```json
{
    "route": ["New York", "Los Angeles", "Chicago", "Houston", "New York"],
    "distance": 4520.34
}
```

## How to Run the Project

Follow these steps to set up and run the project locally:

1. **Clone the Repository**

   ```bash
   git clone https://github.com/<your-username>/RouteOptimizer.git
   cd RouteOptimizer
   ```

2. **Configure the Database**

   - Create a MySQL database named `tsp_db`.
   - Update the database connection details (username, password, URL) in the `application.properties` file:
     ```
     spring.datasource.url=jdbc:mysql://localhost:3306/tsp_db
     spring.datasource.username=your_db_username
     spring.datasource.password=your_db_password
     ```

3. **Add OpenRouteService API Key**

   - Replace the placeholder API key in `DistanceFetcherService.java` with your valid OpenRouteService API key.

4. **Build and Run the Project**
   Use Maven to build and start the project:

   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

5. **Access the Application**
   Open your browser and visit:

   ```
   http://localhost:8080
   ```

6. **Test the Endpoints**

   - Use tools like **Postman** or **cURL** to test the endpoints:
     - Login: `POST http://localhost:8080/login`
     - Register: `POST http://localhost:8080/register`
     - Solve TSP: `POST http://localhost:8080/tsp/solve`

## Acknowledgements

- [OpenRouteService](https://openrouteservice.org/) for the geocoding and directions API.
- Spring Boot for providing a robust backend framework.

---

Happy optimizing your routes with **RouteOptimizer**! 🚀

