package com.example.myapi;

import java.io.Serializable;

public class Event implements Serializable {
    private String title, date, description, location;
    private int reminderTime, importance, id, idUser;

    public Event(int id, int idUser, String title, String date, int importance, String description, String location, int reminderTime) {
        this.id = id;
        this.idUser = idUser;
        this.title = title;
        this.date = date;
        this.importance = importance;
        this.description = description;
        this.location = location;
        this.reminderTime = reminderTime;
    }

    public Event(int idUser, String title, String date, int importance, String description, String location, int reminderTime) {
        this.idUser = idUser;
        this.title = title;
        this.date = date;
        this.importance = importance;
        this.description = description;
        this.location = location;
        this.reminderTime = reminderTime;
    }

    public int getIdUser() {
        return idUser;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public int getImportance() {
        return importance;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public int getReminderTime() {
        return reminderTime;
    }

    @Override
    public String toString() {
        return this.title;
    }
}
