package com.example.auth.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

/**
 * Logging configuration with correlation ID injection.
 * Implements Constitution Principle V: Observability, Resilience, and Operational Consistency.
 * Uses structured logging with correlation IDs for request tracing.
 */
@Configuration
public class LoggingConfig extends OncePerRequestFilter {
    
    private static final String CORRELATION_ID_HEADER = "X-Correlation-ID";
    private static final String CORRELATION_ID_LOG_VAR_NAME = "correlationId";
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        // Generate or extract correlation ID
        String correlationId = request.getHeader(CORRELATION_ID_HEADER);
        if (correlationId == null || correlationId.isEmpty()) {
            correlationId = UUID.randomUUID().toString();
        }
        
        // Add correlation ID to response for client reference
        response.setHeader(CORRELATION_ID_HEADER, correlationId);
        
        // Set correlation ID in MDC (Mapped Diagnostic Context) for structured logging
        try {
            org.slf4j.MDC.put(CORRELATION_ID_LOG_VAR_NAME, correlationId);
            filterChain.doFilter(request, response);
        } finally {
            org.slf4j.MDC.remove(CORRELATION_ID_LOG_VAR_NAME);
        }
    }
}
