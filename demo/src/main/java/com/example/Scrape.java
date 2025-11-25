package com.example;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class Scrape {
    public static void main(String[] args) {
        String username = "PathFound404";
        String url = "https://letterboxd.com/"+ username +"/films";
        
        try{
            Document doc = Jsoup.connect(url).get();
            
            List<Movie> movies = extractMovies(doc);

            for (Movie movie : movies){
                System.out.println(movie.getName() + "     " + movie.getRating());
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static List<Movie> extractMovies(Document doc) {
        List<Movie> movies = new ArrayList<>();

        //Get all grid items with movie data
        Elements gridItems = doc.select("li.griditem");

        for(Element gridItem : gridItems){
            Element posterComponent = gridItem.selectFirst("div.react-component[data-component-class=LazyPoster]");
            if(posterComponent != null){
                String movieName = posterComponent.attr("data-item-name");

                //Extract Rating 
                Double rating = 0.0;

                Element ratingElement = gridItem.selectFirst("span.rating.-micro.-darker");
            
                if(ratingElement != null){
                    String classAttr = ratingElement.className();
                    if(classAttr.contains("rated-")){
                        rating = Double.parseDouble(classAttr.split("rated-")[1].split(" ")[0]);
                    }
                }
                movies.add(new Movie(movieName, rating/2));
            }
        }
        return movies;
    }
}
