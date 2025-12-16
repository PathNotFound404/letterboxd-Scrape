package com.example;

import java.util.List;
import java.util.ArrayList;

public class Main {
        static String url;
    
    public static void main(String[] args) {
        String[] users = {"PathFound404", "AlexTheAlex12"};
        ArrayList<User> usersList = new ArrayList<>();

        for(String username : users){
            url = "https://letterboxd.com/"+ username +"/films";
            User u = new User(username, new ArrayList<>(Scrape.extractMovies(url)));
            usersList.add(u);

            System.out.println("\n\n");
            System.out.println("--------------" + u.getUsername() + "--------------");

            for(Movie movie : u.getFilms()){
                System.out.println((movie.getName() + "------" + movie.getRating() + "------" + movie.getLink()));
            }

            

        }
    }



}
