package com.example.travelevaadmin.model;

public class Place {
    private String title;
    private String description;
    private String charges;
    private String parkingAvailable;
    private String imageUrl;
    private String categories;
    private String addedBy;
    private String timestamp;

    // Existing Constructor
    public Place(String title, String description, String charges, String parkingAvailable, String imageUrl) {
        this.title = title;
        this.description = description;
        this.charges = charges;
        this.parkingAvailable = parkingAvailable;
        this.imageUrl = imageUrl;
    }

    // New Constructor (Overloaded)
    public Place(String title, String description, String charges, String parkingAvailable, String imageUrl, String categories, String addedBy, String timestamp) {
        this.title = title;
        this.description = description;
        this.charges = charges;
        this.parkingAvailable = parkingAvailable;
        this.imageUrl = imageUrl;
        this.categories = categories;
        this.addedBy = addedBy;
        this.timestamp = timestamp;
    }

    // Getters for the new fields
    public String getCategories() {
        return categories;
    }

    public String getAddedBy() {
        return addedBy;
    }

    public String getTimestamp() {
        return timestamp;
    }

    // Existing Getters
    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getCharges() {
        return charges;
    }

    public String getParkingAvailable() {
        return parkingAvailable;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
