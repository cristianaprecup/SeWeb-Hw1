package com.recipe.recipe_recommender.scraper;

import com.recipe.recipe_recommender.Constants;
import com.recipe.recipe_recommender.model.Recipe;
import com.recipe.recipe_recommender.xml.XMLGenerator;
import com.recipe.recipe_recommender.xml.XMLParser;
import com.recipe.recipe_recommender.xml.XMLValidator;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RecipeScraper {

    public static List<String> scrapeRecipeTitles() {
        List<String> titles = new ArrayList<>();

        try {
            Document doc = Jsoup.connect(Constants.BBC_URL)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
                    .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8")
                    .header("Accept-Language", "en-US,en;q=0.5")
                    .header("Connection", "keep-alive")
                    .referrer("https://www.google.com")
                    .timeout(15000)
                    .followRedirects(true)
                    .ignoreHttpErrors(true)
                    .get();

            Elements cards = doc.select("a[href*='/recipes/'] h2.heading-4");
            for (Element card : cards) {
                String title = card.text().trim();
                if (!title.isEmpty()) {
                    titles.add(title);
                }
            }

            System.out.println("Scraped " + titles.size() + " titles.");

        } catch (Exception e) {
            System.err.println("Scraping failed: " + e.getMessage());
            System.out.println("Using fallback list.");
            titles = getFallbackTitles();
        }

        return titles;
    }

    private static List<String> getFallbackTitles() {
        return new ArrayList<>(Arrays.asList(
                "Slow-Cooker Beef Stew",
                "Pumpkin Soup",
                "Spaghetti Bolognese",
                "One-Pot Chicken and Rice",
                "Vegetable Curry",
                "Leek and Potato Soup",
                "Sausage Casserole",
                "Mushroom Risotto",
                "Bean and Chorizo Stew",
                "Pasta e Fagioli",
                "Roasted Root Vegetable Tart",
                "Lentil Dhal",
                "Sweet Potato and Chickpea Curry",
                "Minestrone Soup",
                "Cottage Pie",
                "Butternut Squash Lasagne",
                "Chicken Noodle Soup",
                "Lamb Tagine",
                "Creamy Tomato Pasta",
                "Toad in the Hole",
                "French Onion Soup",
                "Stuffed Peppers"
        ));
    }

    // testing
    public static void main(String[] args) {
        List<String> titles = RecipeScraper.scrapeRecipeTitles();
        XMLGenerator.generateXML(titles);
        XMLValidator.validate(Constants.XML_OUTPUT_PATH, Constants.XML_SCHEMA_PATH);
        List<Recipe> recipes = XMLParser.parseRecipes(Constants.XML_OUTPUT_PATH);
        recipes.forEach(System.out::println);
    }
}
