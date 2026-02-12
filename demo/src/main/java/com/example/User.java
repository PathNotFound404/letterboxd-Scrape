package com.example;
import java.util.ArrayList;

public class User {
    private String username;
    private ArrayList<Movie> Films;

    public User(String username, ArrayList<Movie> Films) {
        this.username = username;
        this.Films = Films;
    }


    public String getUsername() {
        return username;
    }

    public ArrayList<Movie> getFilms() {
        return Films;
    }

    public Movie getMovie(int i){
        return Films.get(i);
    }
}
