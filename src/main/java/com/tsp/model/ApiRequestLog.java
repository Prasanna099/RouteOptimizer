package com.tsp.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "api_request_logs")
public class ApiRequestLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @Column(columnDefinition = "json")
    private String cities;

    @Column(columnDefinition = "json")
    private String responseData;

    private Double totalDistance;

    private LocalDateTime createdAt = LocalDateTime.now();
}
