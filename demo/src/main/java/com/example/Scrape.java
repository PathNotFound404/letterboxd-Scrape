package com.example;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import java.io.FileWriter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class Scrape {
    // public static void main(String[] args) {
    //     String username = "PathFound404";
    //     String url = "https://letterboxd.com/"+ username +"/films/size/large";
        
    //         List<Movie> movies = extractMovies(url);
    //         User user = new User(username, new ArrayList<>(movies));

    //         for (Movie movie : movies){
    //             System.out.println(movie.getName() + "------" + movie.getRating());
    //         }
        
    //         //Simple Output For A users movies
    //         for(int i=0; i<user.getFilms().size(); i++){
    //             Movie movie = user.getMovie(i);
    //             System.out.println((i+1) + ". " + movie.getName() + " ------ " + movie.getRating());
    //         }
    // }



    public static List<Movie> extractMovies(String baseURL){
        List<Movie> movies = new ArrayList<>();

        int page = 1;
        boolean hasNextPage = true;

        while (hasNextPage) {
            String url = baseURL + "/page/" + String.valueOf(page);

            try {
                Document doc = Jsoup.connect(url).get();
                List<Movie> pageMovies= extractPage(doc);

                if(pageMovies.isEmpty()){
                    hasNextPage = false;
                }else {
                    movies.addAll(pageMovies);
                    page++;
                }
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            
        }
 
        return movies;
    }

    public static List<Movie> extractPage(Document doc) {
        List<Movie> pageMovies = new ArrayList<>();

        //Get all grid items with movie data
        Elements gridItems = doc.select("li.griditem");

        for(Element gridItem : gridItems){
            Element posterComponent = gridItem.selectFirst("div.react-component[data-component-class=LazyPoster]");
            if(posterComponent != null){
                String movieName = posterComponent.attr("data-item-name");

                //Extract Rating 
                Double rating = 0.0;

                Element ratingElement = gridItem.selectFirst("span.rating");
            
                if(ratingElement != null){
                    String classAttr = ratingElement.className();
                    if(classAttr.contains("rated-")){
                        rating = Double.parseDouble(classAttr.split("rated-")[1].split(" ")[0]);
                    }
                }
                pageMovies.add(new Movie(movieName, rating/2));
            }
        }
        return pageMovies;
    }
}
