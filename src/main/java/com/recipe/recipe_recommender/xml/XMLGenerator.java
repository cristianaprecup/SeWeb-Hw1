package com.recipe.recipe_recommender.xml;

import com.recipe.recipe_recommender.Constants;
import com.recipe.recipe_recommender.model.Recipe;
import com.recipe.recipe_recommender.model.User;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.List;
import java.util.Random;

public class XMLGenerator {

    public static void generateXML(List<String> titles) {
        Random random = new Random();

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            org.w3c.dom.Document doc = builder.newDocument();

            Element root = doc.createElement("data");
            doc.appendChild(root);

            Element recipesEl = doc.createElement("recipes");
            root.appendChild(recipesEl);

            for (int i = 0; i < titles.size(); i++) {

                Element recipeEl = doc.createElement("recipe");
                recipeEl.setAttribute("id", String.valueOf(i+1));

                Element titleEl = doc.createElement("title");
                titleEl.setTextContent(titles.get(i));
                recipeEl.appendChild(titleEl);

                Element cuisinesEl = doc.createElement("cuisines");
                String cuisine1 = Constants.CUISINE_TYPES[random.nextInt(Constants.CUISINE_TYPES.length)];
                String cuisine2;
                do {
                    cuisine2 = Constants.CUISINE_TYPES[random.nextInt(Constants.CUISINE_TYPES.length)];
                } while (cuisine2.equals(cuisine1));

                Element c1 = doc.createElement("cuisine");
                c1.setTextContent(cuisine1);
                Element c2 = doc.createElement("cuisine");
                c2.setTextContent(cuisine2);
                cuisinesEl.appendChild(c1);
                cuisinesEl.appendChild(c2);
                recipeEl.appendChild(cuisinesEl);

                Element difficultyEl = doc.createElement("difficulty");
                difficultyEl.setTextContent(Constants.DIFFICULTY_LEVELS[random.nextInt(Constants.DIFFICULTY_LEVELS.length)]);
                recipeEl.appendChild(difficultyEl);

                recipesEl.appendChild(recipeEl);
            }

            Element usersEl = doc.createElement("users");
            root.appendChild(usersEl);

            Element userEl = doc.createElement("user");
            userEl.setAttribute("id", "1");

            Element nameEl = doc.createElement("name");
            nameEl.setTextContent("John");
            userEl.appendChild(nameEl);

            Element surnameEl = doc.createElement("surname");
            surnameEl.setTextContent("Doe");
            userEl.appendChild(surnameEl);

            Element skillEl = doc.createElement("cookingSkillLevel");
            skillEl.setTextContent("Intermediate");
            userEl.appendChild(skillEl);

            Element cuisineEl = doc.createElement("preferredCuisine");
            cuisineEl.setTextContent("Italian");
            userEl.appendChild(cuisineEl);

            usersEl.appendChild(userEl);

            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(Constants.XML_INDENT_PROPERTY, Constants.XML_INDENT);
            transformer.transform(new DOMSource(doc), new StreamResult(new File(Constants.XML_OUTPUT_PATH)));
        } catch (Exception e) {
            System.err.println("XML generation failed: " + e.getMessage());
        }
    }

    public static boolean addRecipeAndValidate(Recipe newRecipe, String xmlPath, String xsdPath) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            org.w3c.dom.Document doc = builder.parse(new File(xmlPath));

            org.w3c.dom.NodeList recipesNodes = doc.getElementsByTagName("recipes");
            if (recipesNodes.getLength() == 0) return false;
            Element recipesEl = (Element) recipesNodes.item(0);

            org.w3c.dom.NodeList allRecipes = doc.getElementsByTagName("recipe");
            int newId = allRecipes.getLength() + 1;
            newRecipe.setId(newId);

            Element recipeEl = doc.createElement("recipe");
            recipeEl.setAttribute("id", String.valueOf(newId));

            Element titleEl = doc.createElement("title");
            titleEl.setTextContent(newRecipe.getTitle());
            recipeEl.appendChild(titleEl);

            Element cuisinesEl = doc.createElement("cuisines");
            for (String cuisine : newRecipe.getCuisines()) {
                Element c = doc.createElement("cuisine");
                c.setTextContent(cuisine);
                cuisinesEl.appendChild(c);
            }
            recipeEl.appendChild(cuisinesEl);

            Element difficultyEl = doc.createElement("difficulty");
            difficultyEl.setTextContent(newRecipe.getDifficulty());
            recipeEl.appendChild(difficultyEl);

            recipesEl.appendChild(recipeEl);

            String tempXmlPath = xmlPath + ".tmp";
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(new DOMSource(doc), new StreamResult(new File(tempXmlPath)));

            try {
                XMLValidator.validate(tempXmlPath, xsdPath);
            } catch (Exception e) {
                new File(tempXmlPath).delete();
                System.err.println("Validation failed, the recipe does not match the schema!");
                return false;
            }

            File oldFile = new File(xmlPath);
            File tempFile = new File(tempXmlPath);
            if (oldFile.delete()) {
                tempFile.renameTo(oldFile);
            }

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static synchronized void saveUserToXML(User user) throws Exception {
        File file = new File(Constants.XML_OUTPUT_PATH);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        org.w3c.dom.Document doc = builder.parse(file);

        org.w3c.dom.NodeList userNodes = doc.getElementsByTagName("user");
        Element userEl;

        if (userNodes.getLength() > 0) {
            userEl = (Element) userNodes.item(0);
        } else {
            Element usersEl = (Element) doc.getElementsByTagName("users").item(0);
            userEl = doc.createElement("user");
            userEl.setAttribute("id", "1");
            usersEl.appendChild(userEl);
        }

        updateElement(doc, userEl, "name", user.getName());
        updateElement(doc, userEl, "surname", user.getSurname());
        updateElement(doc, userEl, "cookingSkillLevel", user.getCookingSkillLevel());
        updateElement(doc, userEl, "preferredCuisine", user.getPreferredCuisine());

        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(Constants.XML_INDENT_PROPERTY, Constants.XML_INDENT);
        transformer.transform(new DOMSource(doc), new StreamResult(file));
    }

    private static void updateElement(org.w3c.dom.Document doc, Element parent, String tagName, String value) {
        org.w3c.dom.NodeList list = parent.getElementsByTagName(tagName);
        if (list.getLength() > 0) {
            list.item(0).setTextContent(value);
        } else {
            Element el = doc.createElement(tagName);
            el.setTextContent(value);
            parent.appendChild(el);
        }
    }
}