package com.example.travelevaadmin.model;

public class Booking {

    private String userId;
    // other fields

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    private String placeName;
    private String userName;

    // Default constructor required for Firebase
    public Booking() {}

    public Booking(String placeName, String userName) {
        this.placeName = placeName;
        this.userName = userName;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
