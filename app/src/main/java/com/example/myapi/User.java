package com.example.myapi;

import java.io.Serializable;

public class User implements Serializable {
    private int id;
    private String username;
    private String password;
    private String secretAnswer;

    // Constructor
    public User(int id, String username, String password, String secretAnswer) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.secretAnswer = secretAnswer;
    }

    public User(String username, String password, String secretAnswer) {
        this.username = username;
        this.password = password;
        this.secretAnswer = secretAnswer;
    }

    // Getters
    public int getId() {
        return id;
    }
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getSecretAnswer() {
        return secretAnswer;
    }
}
