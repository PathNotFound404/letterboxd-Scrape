package com.example;

import java.util.*;

public class MovieUtils {

    public static List<String> getCommonMovieThreshold(List<User> users, Double threshold){
        if(users == null || users.size() <=1){
            //Build feature to just return the one users list if it is just one user!!!!
            return new ArrayList<>();
        }
        
        
        //Get movies names from first user above the threshold  
        Set<String> eligibleMovieNames = new HashSet<>();
            for(Movie movie : users.get(0).getFilms()){
                if(movie.getRating() >= threshold){
                    eligibleMovieNames.add(movie.getName());
                }
            }

        //For each other user keep only matching movies above the threshold
        for(int i = 1; i < users.size(); i++){
            Set<String> curUserEligibleNames = new HashSet<>();

            for(Movie movie : users.get(i).getFilms()){
                if(movie.getRating() >= threshold){
                    curUserEligibleNames.add(movie.getName());
                }
            }

            //Keep ONLY matching names
            eligibleMovieNames.retainAll(curUserEligibleNames);
        }
        

        List<String> result = new ArrayList<>();
        for(Movie movie : users.get(0).getFilms()){
            if (eligibleMovieNames.contains(movie.getName())){
                result.add(movie.getName());
            }
        }
        
        return result;
    }



}


