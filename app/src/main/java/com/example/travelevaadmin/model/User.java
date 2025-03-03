package com.example.travelevaadmin.model;

// User model class
public class User {
    public String userId;
    public String name;
    public String email;
    public String password;

    // Default constructor required for Firebase
    public User() {
    }

    public User(String userId, String name, String email, String password) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = password;
    }
}
