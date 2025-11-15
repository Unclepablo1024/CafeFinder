package com.cafefinder.app.dto;

import java.util.List;

public class GooglePlacesResponse {
    private List<Place> results;
    private String status;
    private String next_page_token;

    public static class Place {
        private Geometry geometry;
        private String name;
        private String formatted_address;
        private String place_id;
        private List<String> types;
        private double rating;
        private int user_ratings_total;
        private String website;
        private String formatted_phone_number;
        private OpeningHours opening_hours;
        private List<Photo> photos;
        private String price_level; // 0 to 4

        public static class Geometry {
            private Location location;

            public static class Location {
                private double lat;
                private double lng;

                public double getLat() { return lat; }
                public void setLat(double lat) { this.lat = lat; }
                public double getLng() { return lng; }
                public void setLng(double lng) { this.lng = lng; }
            }

            public Location getLocation() { return location; }
            public void setLocation(Location location) { this.location = location; }
        }

        public static class OpeningHours {
            private List<String> weekday_text;
            private boolean open_now;

            public List<String> getWeekday_text() { return weekday_text; }
            public void setWeekday_text(List<String> weekday_text) { this.weekday_text = weekday_text; }
            public boolean isOpen_now() { return open_now; }
            public void setOpen_now(boolean open_now) { this.open_now = open_now; }
        }

        public static class Photo {
            private String photo_reference;
            private int width;
            private int height;

            public String getPhoto_reference() { return photo_reference; }
            public void setPhoto_reference(String photo_reference) { this.photo_reference = photo_reference; }
            public int getWidth() { return width; }
            public void setWidth(int width) { this.width = width; }
            public int getHeight() { return height; }
            public void setHeight(int height) { this.height = height; }
        }

        // Getters and setters
        public Geometry getGeometry() { return geometry; }
        public void setGeometry(Geometry geometry) { this.geometry = geometry; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getFormatted_address() { return formatted_address; }
        public void setFormatted_address(String formatted_address) { this.formatted_address = formatted_address; }
        public String getPlace_id() { return place_id; }
        public void setPlace_id(String place_id) { this.place_id = place_id; }
        public List<String> getTypes() { return types; }
        public void setTypes(List<String> types) { this.types = types; }
        public double getRating() { return rating; }
        public void setRating(double rating) { this.rating = rating; }
        public int getUser_ratings_total() { return user_ratings_total; }
        public void setUser_ratings_total(int user_ratings_total) { this.user_ratings_total = user_ratings_total; }
        public String getWebsite() { return website; }
        public void setWebsite(String website) { this.website = website; }
        public String getFormatted_phone_number() { return formatted_phone_number; }
        public void setFormatted_phone_number(String formatted_phone_number) { this.formatted_phone_number = formatted_phone_number; }
        public OpeningHours getOpening_hours() { return opening_hours; }
        public void setOpening_hours(OpeningHours opening_hours) { this.opening_hours = opening_hours; }
        public List<Photo> getPhotos() { return photos; }
        public void setPhotos(List<Photo> photos) { this.photos = photos; }
        public String getPrice_level() { return price_level; }
        public void setPrice_level(String price_level) { this.price_level = price_level; }
    }

    public List<Place> getResults() { return results; }
    public void setResults(List<Place> results) { this.results = results; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getNext_page_token() { return next_page_token; }
    public void setNext_page_token(String next_page_token) { this.next_page_token = next_page_token; }
}
