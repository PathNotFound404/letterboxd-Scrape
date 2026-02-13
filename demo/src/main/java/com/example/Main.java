package com.example;

import org.openqa.selenium.WebDriver;

import java.util.*;

public class Main {
        static String url;
    
    public static void main(String[] args) {
        String[] users = {"PathFound404", "AlexTheAlex12"};
        ArrayList<User> usersList = new ArrayList<>();


        for(String username : users){
            WebDriver driver = Scrape.inintializeWebDriver();
            url = "https://letterboxd.com/"+ username +"/films";
            User u = new User(username, new ArrayList<>(Scrape.getMovies(driver, url)));
            usersList.add(u);
            driver.quit();

            //System.out.println("\n\n");
            //System.out.println("--------------" + u.getUsername() + "--------------");

            // for(Movie movie : u.getFilms()){
            //     System.out.println((movie.getName() + " ------ " + movie.getRating() + " ------ " + movie.getLink()));
            // }
            System.out.println("Data Intialized");

        }


        List<String> common = MovieUtils.getCommonMovieThreshold(usersList, 4.0);
        
        for(String s : common){
            System.out.println(s);
        }

    }
}
