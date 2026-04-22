package com.recipe.recipe_recommender.xml;

import com.recipe.recipe_recommender.Constants;
import com.recipe.recipe_recommender.model.Recipe;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class XMLQueryService {

    private Document loadDocument() throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new File(Constants.XML_OUTPUT_PATH));
        doc.getDocumentElement().normalize();
        return doc;
    }

    public String getFirstUserSkillLevel() throws Exception {
        Document doc = loadDocument();
        XPath xpath = XPathFactory.newInstance().newXPath();
        return (String) xpath.evaluate(Constants.XPATH_FIRST_USER_SKILL, doc, XPathConstants.STRING);
    }

    public List<Recipe> recommendBySkill(String skillLevel) throws Exception {
        Document doc = loadDocument();
        XPath xpath = XPathFactory.newInstance().newXPath();

        String expression = String.format(Constants.XPATH_RECOMMEND_BY_SKILL, skillLevel);
        NodeList nodes = (NodeList) xpath.evaluate(expression, doc, XPathConstants.NODESET);

        List<Recipe> recipes = new ArrayList<>();
        XMLParser.parse(recipes, nodes);

        return recipes;
    }


    public String getFirstUserPreferredCuisine() throws Exception {
        Document doc = loadDocument();
        XPath xpath = XPathFactory.newInstance().newXPath();
        return (String) xpath.evaluate(Constants.XPATH_FIRST_USER_CUISINE, doc, XPathConstants.STRING);
    }

    public List<Recipe> recommendBySkillAndCuisine(String skillLevel, String cuisine) throws Exception {
        Document doc = loadDocument();
        XPath xpath = XPathFactory.newInstance().newXPath();

        String expression = String.format(Constants.XPATH_RECOMMEND_BY_SKILL_AND_CUISINE, skillLevel, cuisine);
        NodeList nodes = (NodeList) xpath.evaluate(expression, doc, XPathConstants.NODESET);

        List<Recipe> recipes = new ArrayList<>();
        parse(nodes, recipes);

        return recipes;
    }

    static void parse(NodeList nodes, List<Recipe> recipes) {
        for (int i = 0; i < nodes.getLength(); i++) {
            Element recipeEl = (Element) nodes.item(i);

            int id = Integer.parseInt(recipeEl.getAttribute("id"));
            String title = recipeEl.getElementsByTagName("title").item(0).getTextContent();
            String difficulty = recipeEl.getElementsByTagName("difficulty").item(0).getTextContent();

            NodeList cuisineNodes = recipeEl.getElementsByTagName("cuisine");
            List<String> cuisines = new ArrayList<>();
            for (int j = 0; j < cuisineNodes.getLength(); j++) {
                cuisines.add(cuisineNodes.item(j).getTextContent());
            }

            recipes.add(new Recipe(id, title, cuisines, difficulty));
        }
    }

    public Recipe getRecipeById(int id) throws Exception {
        Document doc = loadDocument();
        XPath xpath = XPathFactory.newInstance().newXPath();

        String expression = String.format(Constants.XPATH_RECIPE_BY_ID, id);
        NodeList nodes = (NodeList) xpath.evaluate(expression, doc, XPathConstants.NODESET);

        if (nodes.getLength() == 0) {
            return null;
        }

        List<Recipe> recipes = new ArrayList<>();
        parse(nodes, recipes);
        return recipes.getFirst();
    }

    public List<Recipe> getRecipesByCuisine(String cuisine) {
        List<Recipe> result = new ArrayList<>();
        try {
            javax.xml.parsers.DocumentBuilderFactory factory = javax.xml.parsers.DocumentBuilderFactory.newInstance();
            org.w3c.dom.Document doc = factory.newDocumentBuilder().parse(new java.io.File(Constants.XML_OUTPUT_PATH));

            javax.xml.xpath.XPath xPath = javax.xml.xpath.XPathFactory.newInstance().newXPath();
            String expression = String.format(Constants.XPATH_RECIPES_BY_CUISINE, cuisine);

            org.w3c.dom.NodeList nodes = (org.w3c.dom.NodeList) xPath.compile(expression).evaluate(doc, javax.xml.xpath.XPathConstants.NODESET);

            XMLParser.parse(result, nodes);
        } catch (Exception e) {
            System.err.println("XPath filtering failed: " + e.getMessage());
        }
        return result;
    }
}