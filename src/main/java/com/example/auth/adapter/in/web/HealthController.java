package com.example.auth.adapter.in.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Health and readiness check endpoints for Kubernetes and container orchestration.
 * Implements Constitution Principle V: Observability and health/readiness endpoints.
 */
@RestController
@RequestMapping("/health")
public class HealthController {
    
    /**
     * Liveness probe endpoint. Returns 200 OK if the service is running.
     */
    @GetMapping("/live")
    public ResponseEntity<String> liveness() {
        return ResponseEntity.ok("LIVE");
    }
    
    /**
     * Readiness probe endpoint. Returns 200 OK if the service is ready to handle requests.
     * In a real implementation, this would check database connectivity, cache availability, etc.
     */
    @GetMapping("/ready")
    public ResponseEntity<String> readiness() {
        return ResponseEntity.ok("READY");
    }
    
    /**
     * General health check endpoint.
     */
    @GetMapping
    public ResponseEntity<HealthResponse> health() {
        return ResponseEntity.ok(new HealthResponse("UP"));
    }
    
    public static class HealthResponse {
        public String status;
        
        public HealthResponse(String status) {
            this.status = status;
        }
        
        public String getStatus() {
            return status;
        }
    }
}
