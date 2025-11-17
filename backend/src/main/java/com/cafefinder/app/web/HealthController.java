package com.cafefinder.app.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
public class HealthController {
    @GetMapping("/api/health")
    public Map<String,String> health(){
        return Map.of("status","ok");
    }
    
    @GetMapping("/")
    public Map<String,String> root(){
        return Map.of(
            "message", "Cafe Finder API",
            "status", "running",
            "health", "/api/health",
            "docs", "All API endpoints start with /api/"
        );
    }
}
