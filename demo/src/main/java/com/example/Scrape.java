package com.example;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


//Selenium Imports
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.time.Duration;




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
                String movieLink = posterComponent.attr("data-item-link");

                pageMovies.add(new Movie(movieName, rating/2, movieLink));
            }
        }
        return pageMovies;
    }


    public static WebDriver inintializeWebDriver (){
        
        try{
        Logger.getLogger("org.openqa.selenium").setLevel(Level.SEVERE);

        // All the options to make it look like a real user
        // Needed to run in headless mode, otherwise it works fine without them
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.addArguments("--user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/145.0.0.0 Safari/537.36");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        // Remove automation flags
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
        options.setExperimentalOption("useAutomationExtension", false);
        WebDriver driver = new ChromeDriver(options );
        return driver;
        } catch (Exception e) {
            e.printStackTrace(); 
            return null;
        }
    }



    public static List<Movie> getMovies (WebDriver driver, String baseUrl) {
        List<Movie> movies = new ArrayList<>();

        try {
            driver.get(baseUrl);

            // Wait for movies to load in 
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.presenceOfElementLocated(By.className("griditem")));

            // Find all elements with the class name "griditem" 
            // These are the movie elements on the page
            List<WebElement> gridItems = driver.findElements(By.className("griditem"));
            System.out.println("Found " + gridItems.size() + " grid items.");

            for (WebElement item : gridItems) {
                WebElement reactDiv = item.findElement(By.cssSelector("div.react-component"));
                WebElement rating = item.findElement(By.cssSelector("span.rating"));
                
                // Extract various data attributes
                String title = reactDiv.getAttribute("data-item-name");
                String movieUrl = reactDiv.getAttribute("data-item-link");
                movieUrl = "https://letterboxd.com" + movieUrl; // Prepend base URL

                String ratingClass = rating.getAttribute("class");
                Double ratingValue = (extractRatingValue(ratingClass)/2.0); 

                movies.add(new Movie(title, ratingValue, movieUrl));
                
            }

            System.out.println("Title: " + driver.getTitle());
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
        return movies;  
    }


    private static Double extractRatingValue(String ratingClass) {
        Double ratingValue = 0.0;

        if(ratingClass.contains("rated-")){
            ratingValue = Double.parseDouble(ratingClass.split("rated-")[1].split(" ")[0]);
        }
        return ratingValue;
    }



}
