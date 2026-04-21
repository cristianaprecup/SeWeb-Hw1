package com.recipe.recipe_recommender.xml;

import com.recipe.recipe_recommender.model.Recipe;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class XMLParser {

    public static List<Recipe> parseRecipes(String xmlPath) {
        List<Recipe> recipes = new ArrayList<>();

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File(xmlPath));
            doc.getDocumentElement().normalize();

            NodeList recipeNodes = doc.getElementsByTagName("recipe");

            parse(recipes, recipeNodes);
        } catch (Exception e) {
            System.err.println("Parsing failed: " + e.getMessage());
        }

        return recipes;
    }

    static void parse(List<Recipe> recipes, NodeList recipeNodes) {
        XMLQueryService.parse(recipeNodes, recipes);
    }
}