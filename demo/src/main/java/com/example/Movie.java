package com.example;

public class Movie {
    private String name;
    private Double rating;


    public Movie(String name, Double rating) {
        this.name = name;
        this.rating = rating;
    }

    public String getName() {
        return name;
    }

    public Double getRating() {
        return rating;
    }
}
