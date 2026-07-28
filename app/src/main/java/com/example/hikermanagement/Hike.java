package com.example.hikermanagement;

public class Hike {

    private int id;
    private String name;
    private String location;
    private String date;
    private String parking;
    private int length;
    private String difficulty;
    private String description;
    private String weather;
    private String duration;

    public Hike(
            int id,
            String name,
            String location,
            String date,
            String parking,
            int length,
            String difficulty,
            String description,
            String weather,
            String duration) {

        this.id = id;
        this.name = name;
        this.location = location;
        this.date = date;
        this.parking = parking;
        this.length = length;
        this.difficulty = difficulty;
        this.description = description;
        this.weather = weather;
        this.duration = duration;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getDate() {
        return date;
    }

    public String getParking() {
        return parking;
    }

    public int getLength() {
        return length;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public String getDescription() {
        return description;
    }

    public String getWeather() {
        return weather;
    }

    public String getDuration() {
        return duration;
    }
}