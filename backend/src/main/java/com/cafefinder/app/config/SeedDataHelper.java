package com.cafefinder.app.config;

import com.cafefinder.app.model.Cafe;
import com.cafefinder.app.model.MenuItem;
import com.cafefinder.app.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class SeedDataHelper {
    
    public static List<User> createSampleUsers(PasswordEncoder encoder) {
        List<User> sampleUsers = new ArrayList<>();
        
        // Create admin users
        User admin1 = new User();
        admin1.setUsername("admin");
        admin1.setEmail("admin@cafefinder.com");
        admin1.setPasswordHash(encoder.encode("admin123"));
        admin1.setFirstName("Admin");
        admin1.setLastName("User");
        admin1.setCreatedAt(Instant.now().minus(60, ChronoUnit.DAYS));
        admin1.setActive(true);
        admin1.setVerified(true);
        admin1.setRoles(Set.of("ADMIN", "USER"));
        admin1.setReputation(500);
        admin1.setTotalReviews(0);
        admin1.setLocation("Atlanta, GA");
        admin1.setBio("CafeFinder Administrator");
        sampleUsers.add(admin1);
        
        // Second admin account
        User admin2 = new User();
        admin2.setUsername("admin2");
        admin2.setEmail("admin2@cafefinder.com");
        admin2.setPasswordHash(encoder.encode("Admin2024!"));
        admin2.setFirstName("Sarah");
        admin2.setLastName("Johnson");
        admin2.setCreatedAt(Instant.now().minus(45, ChronoUnit.DAYS));
        admin2.setActive(true);
        admin2.setVerified(true);
        admin2.setRoles(Set.of("ADMIN", "USER"));
        admin2.setReputation(450);
        admin2.setTotalReviews(12);
        admin2.setLocation("Atlanta, GA");
        admin2.setBio("Senior Administrator - Content Moderation");
        sampleUsers.add(admin2);
        
        // Third admin account
        User admin3 = new User();
        admin3.setUsername("admin3");
        admin3.setEmail("admin3@cafefinder.com");
        admin3.setPasswordHash(encoder.encode("SecurePass123!"));
        admin3.setFirstName("Michael");
        admin3.setLastName("Chen");
        admin3.setCreatedAt(Instant.now().minus(30, ChronoUnit.DAYS));
        admin3.setActive(true);
        admin3.setVerified(true);
        admin3.setRoles(Set.of("ADMIN", "USER"));
        admin3.setReputation(400);
        admin3.setTotalReviews(8);
        admin3.setLocation("Atlanta, GA");
        admin3.setBio("System Administrator - Technical Operations");
        sampleUsers.add(admin3);
        
        // Create regular users
        String[] usernames = {"coffeelover", "atlantalocal", "workremote", "espressofan", "reviewer123"};
        String[] firstNames = {"Alice", "Bob", "Carol", "David", "Emma"};
        String[] lastNames = {"Johnson", "Smith", "Davis", "Wilson", "Brown"};
        
        for (int i = 0; i < usernames.length; i++) {
            User user = new User();
            user.setUsername(usernames[i]);
            user.setEmail(usernames[i] + "@example.com");
            user.setPasswordHash(encoder.encode("password123"));
            user.setFirstName(firstNames[i]);
            user.setLastName(lastNames[i]);
            user.setCreatedAt(Instant.now().minus(30, ChronoUnit.DAYS));
            user.setActive(true);
            user.setVerified(true);
            user.setRoles(Set.of("USER"));
            user.setReputation(50 + i * 20);
            user.setTotalReviews(5 + i * 3);
            user.setLocation("Atlanta, GA");
            sampleUsers.add(user);
        }
        
        return sampleUsers;
    }
    
    // Separate method to create just admin users (for adding to existing database)
    public static List<User> createAdminUsers(PasswordEncoder encoder) {
        List<User> adminUsers = new ArrayList<>();
        
        // First admin account
        User admin1 = new User();
        admin1.setUsername("admin");
        admin1.setEmail("admin@cafefinder.com");
        admin1.setPasswordHash(encoder.encode("admin123"));
        admin1.setFirstName("Admin");
        admin1.setLastName("User");
        admin1.setCreatedAt(Instant.now().minus(60, ChronoUnit.DAYS));
        admin1.setActive(true);
        admin1.setVerified(true);
        admin1.setRoles(Set.of("ADMIN", "USER"));
        admin1.setReputation(500);
        admin1.setTotalReviews(0);
        admin1.setLocation("Atlanta, GA");
        admin1.setBio("CafeFinder Administrator");
        adminUsers.add(admin1);
        
        // Second admin account
        User admin2 = new User();
        admin2.setUsername("admin2");
        admin2.setEmail("admin2@cafefinder.com");
        admin2.setPasswordHash(encoder.encode("Admin2024!"));
        admin2.setFirstName("Sarah");
        admin2.setLastName("Johnson");
        admin2.setCreatedAt(Instant.now().minus(45, ChronoUnit.DAYS));
        admin2.setActive(true);
        admin2.setVerified(true);
        admin2.setRoles(Set.of("ADMIN", "USER"));
        admin2.setReputation(450);
        admin2.setTotalReviews(12);
        admin2.setLocation("Atlanta, GA");
        admin2.setBio("Senior Administrator - Content Moderation");
        adminUsers.add(admin2);
        
        // Third admin account
        User admin3 = new User();
        admin3.setUsername("admin3");
        admin3.setEmail("admin3@cafefinder.com");
        admin3.setPasswordHash(encoder.encode("SecurePass123!"));
        admin3.setFirstName("Michael");
        admin3.setLastName("Chen");
        admin3.setCreatedAt(Instant.now().minus(30, ChronoUnit.DAYS));
        admin3.setActive(true);
        admin3.setVerified(true);
        admin3.setRoles(Set.of("ADMIN", "USER"));
        admin3.setReputation(400);
        admin3.setTotalReviews(8);
        admin3.setLocation("Atlanta, GA");
        admin3.setBio("System Administrator - Technical Operations");
        adminUsers.add(admin3);
        
        return adminUsers;
    }

    public static List<Cafe> createSampleCafes() {
        List<Cafe> sampleCafes = new ArrayList<>();

        // Real Atlanta Coffee Shops
        sampleCafes.add(createCafe("BRASH Coffee", "540 W Marietta St NW", "Atlanta", "GA", "30318", 
            33.7731, -84.4044, "$$", "Minimalist design with commitment to direct trade practices and high-quality, small-batch roasted coffee",
            true, true, true, true, false, "paid_lot",
            List.of("oat", "almond", "soy"), List.of("espresso", "pour_over", "drip"),
            List.of("vegan"), List.of("minimalist", "direct_trade", "artisan", "roastery")));

        sampleCafes.add(createCafe("Chrome Yellow Trading Co.", "489 Edgewood Ave SE", "Atlanta", "GA", "30312",
            33.7536, -84.3621, "$$", "Community-centric coffee shop celebrated for seasonal lattes and neighborhood atmosphere",
            true, true, true, true, true, "street",
            List.of("oat", "almond", "soy"), List.of("espresso", "drip", "seasonal_specials"),
            List.of("vegan", "gluten_free"), List.of("community", "edgewood", "seasonal", "neighborhood")));

        sampleCafes.add(createCafe("Momo Cafe", "1345 Piedmont Ave NE", "Atlanta", "GA", "30309",
            33.7835, -84.3733, "$$", "Japanese-inspired café in Midtown offering unique beverages like matcha lattes and mochi donuts",
            true, true, true, true, false, "paid_garage",
            List.of("oat", "almond", "soy"), List.of("matcha", "espresso", "pour_over"),
            List.of("vegan", "gluten_free"), List.of("japanese", "midtown", "matcha", "mochi", "unique")));

        sampleCafes.add(createCafe("Finca to Filter", "652 Boulevard NE", "Atlanta", "GA", "30308",
            33.7659, -84.3631, "$$", "Old Fourth Ward café emphasizing community engagement with variety of specialty drinks",
            true, true, true, true, true, "free_street",
            List.of("oat", "almond", "soy"), List.of("espresso", "pour_over", "cold_brew"),
            List.of("vegan", "vegetarian"), List.of("old_fourth_ward", "community", "specialty", "local")));

        sampleCafes.add(createCafe("Con Leche", "1261 Caroline St NE", "Atlanta", "GA", "30307",
            33.7589, -84.3475, "$$", "Reynoldstown gem sourcing beans from local roasters in cozy environment with toasts and specialty lattes",
            true, true, true, true, true, "free_street",
            List.of("oat", "almond", "soy"), List.of("espresso", "pour_over", "drip"),
            List.of("vegan", "gluten_free"), List.of("reynoldstown", "local_roasters", "cozy", "toast")));

        sampleCafes.add(createCafe("Hodgepodge Coffee House", "720 Moreland Ave SE", "Atlanta", "GA", "30316",
            33.7512, -84.3533, "$$", "East Atlanta Village coffee house known for eclectic atmosphere and strong community presence",
            true, true, true, true, true, "free_street",
            List.of("almond", "soy"), List.of("espresso", "drip", "cold_brew"),
            List.of("vegan"), List.of("east_atlanta", "eclectic", "community", "village")));

        sampleCafes.add(createCafe("Taproom Coffee", "1132 Howell Mill Rd NW", "Atlanta", "GA", "30318",
            33.7851, -84.4126, "$$", "West Midtown coffee shop with industrial vibe and exceptional single-origin offerings",
            true, true, true, true, false, "free_lot",
            List.of("oat", "almond", "soy"), List.of("single_origin", "espresso", "pour_over"),
            List.of("vegan"), List.of("west_midtown", "industrial", "single_origin", "modern")));

        sampleCafes.add(createCafe("Muchacho", "1 Park Pl NE", "Atlanta", "GA", "30309",
            33.7735, -84.3839, "$$", "Piedmont Park area café with Mexican-inspired coffee drinks and relaxed outdoor seating",
            true, true, true, true, true, "paid_garage",
            List.of("oat", "almond", "horchata"), List.of("espresso", "mexican_coffee", "cold_brew"),
            List.of("vegan", "gluten_free"), List.of("piedmont_park", "mexican_inspired", "outdoor", "relaxed")));

        sampleCafes.add(createCafe("Condesa Coffee", "469 Flat Shoals Ave SE", "Atlanta", "GA", "30316",
            33.7462, -84.3484, "$$", "East Atlanta spot known for exceptional breakfast and carefully curated coffee selection",
            true, true, false, true, false, "street",
            List.of("oat", "almond", "soy"), List.of("espresso", "drip", "pour_over"),
            List.of("vegetarian"), List.of("east_atlanta", "breakfast", "curated", "exceptional")));

        sampleCafes.add(createCafe("Docent Coffee", "355 Moreland Ave NE", "Atlanta", "GA", "30307",
            33.7628, -84.3518, "$$", "Little Five Points institution with art-filled walls and consistently excellent coffee",
            true, true, true, true, true, "street",
            List.of("oat", "almond", "soy"), List.of("espresso", "pour_over", "drip"),
            List.of("vegan", "vegetarian"), List.of("little_five_points", "art", "institution", "consistent")));


        return sampleCafes;
    }

    private static Cafe createCafe(String name, String address, String city, String state, String zipCode,
                           double lat, double lng, String priceRange, String description,
                           boolean wifi, boolean seating, boolean workFriendly, boolean bathrooms,
                           boolean petFriendly, String parking, List<String> altMilks,
                           List<String> coffeeTypes, List<String> dietaryOptions, List<String> tags) {
        Cafe cafe = new Cafe();
        cafe.setName(name);
        cafe.setDescription(description);
        cafe.setAddress(address);
        cafe.setCity(city);
        cafe.setState(state);
        cafe.setZipCode(zipCode);
        cafe.setLatitude(lat);
        cafe.setLongitude(lng);
        cafe.setPriceRange(priceRange);
        
        cafe.setWifi(wifi);
        cafe.setSeating(seating);
        cafe.setWorkFriendly(workFriendly);
        cafe.setBathrooms(bathrooms);
        cafe.setPetFriendly(petFriendly);
        cafe.setWheelchairAccessible(true);
        cafe.setParking(parking);
        cafe.setAlternativeMilks(altMilks);
        cafe.setCoffeeTypes(coffeeTypes);
        cafe.setDietaryOptions(dietaryOptions);
        cafe.setTags(tags);
        
        // Add sample menu items
        List<MenuItem> menuItems = new ArrayList<>();
        menuItems.add(new MenuItem("Espresso", "Rich, bold shot", "coffee", 3.00));
        menuItems.add(new MenuItem("Latte", "Smooth espresso with steamed milk", "coffee", 4.50));
        menuItems.add(new MenuItem("Cold Brew", "Smooth, cold-extracted coffee", "coffee", 4.00));
        menuItems.add(new MenuItem("Avocado Toast", "Fresh avocado on artisan bread", "food", 8.00));
        menuItems.add(new MenuItem("Croissant", "Buttery, flaky pastry", "pastry", 3.50));
        cafe.setMenuItems(menuItems);
        
        // Business hours (all 7 days: 0=Sunday, 1=Monday, ..., 6=Saturday)
        Map<Integer, String> hours = new HashMap<>();
        for (int i = 0; i <= 6; i++) {
            hours.put(i, "7:00-19:00");
        }
        cafe.setHours(hours);
        
        cafe.setSocials(List.of("https://instagram.com/" + name.toLowerCase().replace(" ", "")));
        cafe.setCurrentStatus("unknown");
        cafe.setAvgRating(4.0 + Math.random()); // 4.0-5.0 initial rating
        cafe.setReviewsCount(0);
        
        // Initialize ownership fields
        cafe.setClaimed(false);
        cafe.setClaimStatus("UNCLAIMED");
        cafe.setVerified(false);
        
        return cafe;
    }
}
