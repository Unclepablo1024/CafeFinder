package com.cafefinder.app.service;

import com.cafefinder.app.dto.GooglePlacesResponse;
import com.cafefinder.app.dto.GooglePlacesResponse.Place;
import com.cafefinder.app.model.Cafe;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

@Service
public class GooglePlacesService {

    @Value("${google.places.api.key}")
    private String apiKey;

    @Value("${google.places.api.base.url}")
    private String baseUrl;

    private final RestTemplate restTemplate;

    public GooglePlacesService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<Cafe> searchCafes(String query) {
        List<Cafe> cafes = new ArrayList<>();

        // Try text search first (works for any query) - optimized to reduce API costs
        String textUrl = baseUrl + "/textsearch/json?query={query}&key={key}";
        GooglePlacesResponse textResponse = restTemplate.getForObject(textUrl, GooglePlacesResponse.class, query, apiKey);

        if (textResponse != null && "OK".equals(textResponse.getStatus()) && textResponse.getResults() != null) {
            cafes.addAll(mapPlacesToCafes(textResponse.getResults()));
        }

        // If we have a "coffee shops in [city]" query and results are limited (< 20), try nearby search for more results
        if (query.contains("coffee shops in ") && cafes.size() < 20) {
            String cityName = query.replace("coffee shops in ", "").trim();

            // Geocode the city to get coordinates
            double[] coordinates = geocodeCity(cityName);
            if (coordinates != null) {
                // Use nearby search for more results (up to 60) - optimizes API costs
                String nearbyUrl = baseUrl + "/nearbysearch/json?location={lat},{lng}&radius=20000&type=cafe&key={key}";
                GooglePlacesResponse nearbyResponse = restTemplate.getForObject(nearbyUrl, GooglePlacesResponse.class,
                    coordinates[0], coordinates[1], apiKey);

                if (nearbyResponse != null && "OK".equals(nearbyResponse.getStatus()) && nearbyResponse.getResults() != null) {
                    List<Cafe> nearbyCafes = mapPlacesToCafes(nearbyResponse.getResults());
                    // Add only cafes we don't already have
                    nearbyCafes.forEach(nearbyCafe -> {
                        boolean exists = cafes.stream().anyMatch(existing ->
                            existing.getName() != null && nearbyCafe.getName() != null &&
                            existing.getName().equalsIgnoreCase(nearbyCafe.getName()) &&
                            existing.getAddress() != null && nearbyCafe.getAddress() != null &&
                            existing.getAddress().equalsIgnoreCase(nearbyCafe.getAddress()));
                        if (!exists) {
                            cafes.add(nearbyCafe);
                        }
                    });
                }
            }
        }

        return cafes;
    }

    private double[] geocodeCity(String cityName) {
        try {
            String geocodeUrl = baseUrl.replace("/api/place", "/api/geocode/json?address={address}&key={key}");
            Map<String, Object> geocodeResponse = restTemplate.getForObject(geocodeUrl, Map.class, cityName, apiKey);

            if (geocodeResponse != null && "OK".equals(geocodeResponse.get("status"))) {
                @SuppressWarnings("unchecked")
                List<Map<String, Object>> results = (List<Map<String, Object>>) geocodeResponse.get("results");
                if (results != null && !results.isEmpty()) {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> geometry = (Map<String, Object>) results.get(0).get("geometry");
                    if (geometry != null) {
                        @SuppressWarnings("unchecked")
                        Map<String, Object> location = (Map<String, Object>) geometry.get("location");
                        if (location != null) {
                            double lat = ((Number) location.get("lat")).doubleValue();
                            double lng = ((Number) location.get("lng")).doubleValue();
                            return new double[]{lat, lng};
                        }
                    }
                }
            }
        } catch (Exception e) {
            // Geocoding failed, continue without location-based search
        }
        return null;
    }

    private List<Cafe> mapPlacesToCafes(List<Place> places) {
        List<Cafe> cafes = new ArrayList<>();
        for (Place place : places) {
            Cafe cafe = new Cafe();

            // Basic info
            cafe.setName(place.getName());
            cafe.setAvgRating(place.getRating() > 0 ? place.getRating() : 0.0);
            cafe.setReviewsCount(place.getUser_ratings_total());

            // Location
            cafe.setLatitude(place.getGeometry().getLocation().getLat());
            cafe.setLongitude(place.getGeometry().getLocation().getLng());

            // Address - parse formatted_address
            parseAddress(place.getFormatted_address(), cafe);

            // Contact
            cafe.setPhone(place.getFormatted_phone_number());
            cafe.setWebsite(place.getWebsite());

            // Photos - skip processing to save API costs since we have photos stored locally
            // Uncomment below if we ever need to pull photos from Google Places again
            /*
            if (place.getPhotos() != null && !place.getPhotos().isEmpty()) {
                List<String> photos = new ArrayList<>();
                for (GooglePlacesResponse.Place.Photo photo : place.getPhotos()) {
                    String url = baseUrl.replace("/api/place", "/api/place/photo") +
                                 "?maxwidth=800&photoreference=" + photo.getPhoto_reference() +
                                 "&key=" + apiKey;
                    photos.add(url);
                }
                cafe.setPhotos(photos);
            }
            */

            // Price level
            if (place.getPrice_level() != null) {
                int level = Integer.parseInt(place.getPrice_level());
                switch (level) {
                    case 0: cafe.setPriceRange("$"); break;
                    case 1: cafe.setPriceRange("$$"); break;
                    case 2: cafe.setPriceRange("$$$"); break;
                    case 3: cafe.setPriceRange("$$$$"); break;
                    default: cafe.setPriceRange("$$"); break;
                }
            } else {
                cafe.setPriceRange("$$");
            }

            // Hours
            if (place.getOpening_hours() != null && place.getOpening_hours().getWeekday_text() != null) {
                Map<Integer, String> hours = new HashMap<>();
                List<String> weekDays = place.getOpening_hours().getWeekday_text();
                for (int i = 0; i < weekDays.size() && i < 7; i++) {
                    String dayText = weekDays.get(i);
                    // Parse "Monday: 8:00 AM – 5:00 PM" -> "8:00-17:00"
                    String timeRange = parseTimeRange(dayText);
                    hours.put(i, timeRange);
                }
                cafe.setHours(hours);
            } else {
                // Default hours if not available
                Map<Integer, String> hours = new HashMap<>();
                for (int i = 0; i < 7; i++) {
                    hours.put(i, "7:00-19:00");
                }
                cafe.setHours(hours);
            }

            // Initialize defaults for fields not in Google Places
            cafe.setDescription("No Information Available");
            cafe.setWifi(true); // Assume most coffee shops have wifi
            cafe.setSeating(true);
            cafe.setWorkFriendly(true);
            cafe.setBathrooms(true);
            cafe.setPetFriendly(false);
            cafe.setWheelchairAccessible(true);
            cafe.setParking("street");
            cafe.setAlternativeMilks(List.of("mocha", "almond"));
            cafe.setCoffeeTypes(List.of("espresso", "drip", "pour_over"));
            cafe.setDietaryOptions(new ArrayList<>());
            cafe.setAvgCoffeeRating(0.0);
            cafe.setAvgTasteRating(0.0);
            cafe.setCurrentStatus("unknown");
            cafe.setClaimed(false);
            cafe.setClaimStatus("UNCLAIMED");
            cafe.setVerified(false);
            cafe.setTags(List.of("coffee", "cafe"));

            cafes.add(cafe);
        }
        return cafes;
    }

    private void parseAddress(String formattedAddress, Cafe cafe) {
        if (formattedAddress == null) return;

        String[] parts = formattedAddress.split(",");
        if (parts.length >= 2) {
            cafe.setAddress(parts[0].trim());
            String cityState = parts[1].trim();
            // Assume city, state zip format for last part
            String[] cityStateZip = cityState.split("\\s+");
            if (cityStateZip.length >= 2) {
                cafe.setCity(cityState);
                cafe.setState("GA"); // Default to GA since we're searching in Atlanta
                cafe.setZipCode("30301"); // Default
            }
        } else {
            cafe.setAddress(formattedAddress);
            cafe.setCity("Atlanta");
            cafe.setState("GA");
            cafe.setZipCode("30301");
        }
    }

    private String parseTimeRange(String dayText) {
        // e.g., "Monday: 8:00 AM – 5:00 PM" -> "8:00-17:00"
        try {
            if (dayText.contains("–")) {
                String[] parts = dayText.split("[:\\s]+");
                for (int i = 0; i < parts.length; i++) {
                    if (parts[i].equals("AM") || parts[i].equals("PM")) {
                        continue;
                    }
                    if (parts[i].contains(":")) {
                        // Find open and close
                        if (i + 2 < parts.length) {
                            String open = convertTo24Hour(parts[i], parts[i+1]);
                            String close = convertTo24Hour(parts[i+3], parts[i+4]);
                            return open + "-" + close;
                        }
                    }
                }
            }
        } catch (Exception e) {
            // Fall back to default
        }
        return "7:00-19:00";
    }

    private String convertTo24Hour(String time, String ampm) {
        try {
            String[] timeParts = time.split(":");
            int hour = Integer.parseInt(timeParts[0]);
            int minute = timeParts.length > 1 ? Integer.parseInt(timeParts[1]) : 0;

            if ("PM".equals(ampm) && hour != 12) {
                hour += 12;
            } else if ("AM".equals(ampm) && hour == 12) {
                hour = 0;
            }

            return String.format("%d:%02d", hour, minute);
        } catch (Exception e) {
            return "07:00";
        }
    }
}
