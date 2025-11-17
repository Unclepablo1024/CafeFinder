package com.cafefinder.app.config;

import com.cafefinder.app.model.Cafe;
import com.cafefinder.app.model.User;
import com.cafefinder.app.repo.CafeRepo;
import com.cafefinder.app.repo.UserRepo;
import com.cafefinder.app.repo.ReviewRepo;
import com.cafefinder.app.repo.BusyRepo;
import com.cafefinder.app.service.GooglePlacesService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.*;

@Configuration
public class SeedData {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    CommandLineRunner init(CafeRepo cafes, UserRepo users, ReviewRepo reviews, BusyRepo busyRepo, PasswordEncoder encoder, GooglePlacesService googlePlacesService){
        return args -> {
            // Create sample users if none exist
            if(users.count() == 0){
                List<User> sampleUsers = SeedDataHelper.createSampleUsers(encoder);
                for (User user : sampleUsers) {
                    users.save(user);
                }
                System.out.println("Created " + sampleUsers.size() + " sample users");
            } else {
                // Ensure admin accounts exist even if users already exist
                List<User> adminUsers = SeedDataHelper.createAdminUsers(encoder);
                int createdCount = 0;
                for (User admin : adminUsers) {
                    if (!users.findByUsername(admin.getUsername()).isPresent()) {
                        users.save(admin);
                        createdCount++;
                        System.out.println("Created admin account: " + admin.getUsername());
                    }
                }
                if (createdCount > 0) {
                    System.out.println("Created " + createdCount + " additional admin accounts");
                }
            }

            // Create sample cafes - try Google Places API first, but always fallback to static data
            if(cafes.count() == 0){
                List<Cafe> sampleCafes = null;
                
                // Try Google Places API only if API key is configured (optional)
                String apiKey = System.getenv("GOOGLE_PLACES_API_KEY");
                if (apiKey != null && !apiKey.isBlank()) {
                    try {
                        System.out.println("Fetching cafes from Google Places API...");
                        sampleCafes = googlePlacesService.searchCafes("coffee shops in Atlanta");
                        if (sampleCafes != null && !sampleCafes.isEmpty()) {
                            for (Cafe cafe : sampleCafes) {
                                cafes.save(cafe);
                            }
                            System.out.println("Created " + sampleCafes.size() + " cafes from Google Places API");
                        }
                    } catch (Exception e) {
                        System.err.println("Failed to fetch cafes from Google Places API: " + e.getMessage());
                        System.out.println("Falling back to static sample cafes...");
                        sampleCafes = null; // Force fallback
                    }
                } else {
                    System.out.println("Google Places API key not configured, using static sample cafes...");
                }
                
                // Always use fallback if Google Places failed or API key not set
                if (sampleCafes == null || sampleCafes.isEmpty()) {
                    sampleCafes = SeedDataHelper.createSampleCafes();
                    for (Cafe cafe : sampleCafes) {
                        cafes.save(cafe);
                    }
                    System.out.println("Created " + sampleCafes.size() + " static sample cafes");
                }
            } else {
                // Update existing cafes with correct hours (including Sunday)
                System.out.println("Updating hours for existing cafes to include Sunday...");
                for (Cafe cafe : cafes.findAll()) {
                    Map<Integer, String> hours = new HashMap<>();
                    for (int i = 0; i <= 6; i++) {  // 0=Sunday through 6=Saturday
                        hours.put(i, "7:00-19:00");
                    }
                    cafe.setHours(hours);
                    cafes.save(cafe);
                }
                System.out.println("Updated hours for all cafes");
            }
        };
    }
}
