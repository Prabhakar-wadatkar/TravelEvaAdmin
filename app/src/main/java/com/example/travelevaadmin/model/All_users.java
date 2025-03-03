package com.example.travelevaadmin.model;

// All_users model class
public class All_users {
    private String userId;
    private String name;
    private String email;
    private String password;
    private boolean isActive;

    // Default constructor required for Firebase
    public All_users() {
    }

    // Constructor with fields
    public All_users(String userId, String name, String email, String password, boolean isActive) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = password;
        this.isActive = isActive;
    }

    // Getter methods
    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public boolean isActive() {
        return isActive;
    }
}
