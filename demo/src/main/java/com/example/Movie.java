package com.example;

public class Movie {
    private String name;
    private Double rating;
    private String link;

    // Constructors
    public Movie(String name, Double rating) {
        this.name = name;
        this.rating = rating;
    }

    public Movie(String name, Double rating, String link) {
        this.name = name;
        this.rating = rating;
        this.link = link;
    }



    // Getters
    public String getName() {
        return name;
    }

    public Double getRating() {
        return rating;
    }

    public String getLink() {
        return link;
    }
}
