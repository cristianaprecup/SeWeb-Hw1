package com.recipe.recipe_recommender.controller;

import com.recipe.recipe_recommender.Constants;
import com.recipe.recipe_recommender.model.Recipe;
import com.recipe.recipe_recommender.model.User;
import com.recipe.recipe_recommender.xml.XMLParser;
import com.recipe.recipe_recommender.xml.XMLQueryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.recipe.recipe_recommender.xml.XMLGenerator;
import java.util.ArrayList;

import java.util.List;

@RestController
@RequestMapping("/api/recipes")
public class RecipeController {

    private final XMLQueryService xmlQueryService = new XMLQueryService();

    @GetMapping
    public List<Recipe> getAllRecipes() {
        return XMLParser.parseRecipes(Constants.XML_OUTPUT_PATH);
    }

    @GetMapping("/recommend/skill")
    public List<Recipe> recommendBySkill() {
        try {
            String skillLevel = xmlQueryService.getFirstUserSkillLevel();
            return xmlQueryService.recommendBySkill(skillLevel);
        } catch (Exception e) {
            return List.of();
        }
    }

    @GetMapping("/recommend/skill-cuisine")
    public List<Recipe> recommendBySkillAndCuisine() {
        try {
            String skillLevel = xmlQueryService.getFirstUserSkillLevel();
            String cuisine = xmlQueryService.getFirstUserPreferredCuisine();
            return xmlQueryService.recommendBySkillAndCuisine(skillLevel, cuisine);
        } catch (Exception e) {
            return List.of();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Recipe> getRecipeById(@PathVariable int id) {
        try {
            Recipe recipe = xmlQueryService.getRecipeById(id);
            if (recipe == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(recipe);
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @org.springframework.web.bind.annotation.PostMapping
    public ResponseEntity<String> addRecipe(@org.springframework.web.bind.annotation.RequestBody Recipe newRecipe) {
        boolean isSavedAndValid = com.recipe.recipe_recommender.xml.XMLGenerator.addRecipeAndValidate(
                newRecipe,
                Constants.XML_OUTPUT_PATH,
                Constants.XML_SCHEMA_PATH
        );

        if (isSavedAndValid) {
            return ResponseEntity.ok("Recipe successfully validated and added!");
        } else {
            return ResponseEntity.badRequest().body("Error: Recipe failed XSD validation!");
        }
    }

    @PostMapping("/user")
    public ResponseEntity<String> saveUser(@RequestBody User user) {
        try {
            java.io.File file = new java.io.File(Constants.XML_OUTPUT_PATH);
            javax.xml.parsers.DocumentBuilderFactory factory = javax.xml.parsers.DocumentBuilderFactory.newInstance();
            org.w3c.dom.Document doc = factory.newDocumentBuilder().parse(file);
            org.w3c.dom.Element root = doc.getDocumentElement();

            org.w3c.dom.NodeList nodes = doc.getElementsByTagName("user");
            org.w3c.dom.Element targetUser = null;

            for (int i = 0; i < nodes.getLength(); i++) {
                org.w3c.dom.Element el = (org.w3c.dom.Element) nodes.item(i);
                if (el.getElementsByTagName("name").item(0).getTextContent().equals(user.getName()) &&
                        el.getElementsByTagName("surname").item(0).getTextContent().equals(user.getSurname())) {
                    targetUser = el;
                    break;
                }
            }

            if (targetUser != null) {
                targetUser.getElementsByTagName("cookingSkillLevel").item(0).setTextContent(user.getCookingSkillLevel());
                targetUser.getElementsByTagName("preferredCuisine").item(0).setTextContent(user.getPreferredCuisine());
                root.insertBefore(targetUser, root.getFirstChild());
            } else {
                org.w3c.dom.Element newUser = doc.createElement("user");
                org.w3c.dom.Element name = doc.createElement("name");
                name.setTextContent(user.getName());
                newUser.appendChild(name);
                org.w3c.dom.Element surname = doc.createElement("surname");
                surname.setTextContent(user.getSurname());
                newUser.appendChild(surname);
                org.w3c.dom.Element skill = doc.createElement("cookingSkillLevel");
                skill.setTextContent(user.getCookingSkillLevel());
                newUser.appendChild(skill);
                org.w3c.dom.Element cuisine = doc.createElement("preferredCuisine");
                cuisine.setTextContent(user.getPreferredCuisine());
                newUser.appendChild(cuisine);
                root.insertBefore(newUser, root.getFirstChild());
            }

            saveXmlDocument(doc, file);
            return ResponseEntity.ok("User profile updated and set as active!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/user/select")
    public ResponseEntity<String> selectUser(@RequestBody User user) {
        try {
            java.io.File file = new java.io.File(Constants.XML_OUTPUT_PATH);
            javax.xml.parsers.DocumentBuilderFactory factory = javax.xml.parsers.DocumentBuilderFactory.newInstance();
            org.w3c.dom.Document doc = factory.newDocumentBuilder().parse(file);
            org.w3c.dom.Element root = doc.getDocumentElement();
            org.w3c.dom.NodeList nodes = doc.getElementsByTagName("user");

            for (int i = 0; i < nodes.getLength(); i++) {
                org.w3c.dom.Element el = (org.w3c.dom.Element) nodes.item(i);
                if (el.getElementsByTagName("name").item(0).getTextContent().equals(user.getName()) &&
                        el.getElementsByTagName("surname").item(0).getTextContent().equals(user.getSurname())) {
                    root.insertBefore(el, root.getFirstChild());
                    break;
                }
            }

            saveXmlDocument(doc, file);
            return ResponseEntity.ok("Profile switched!");
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    private void saveXmlDocument(org.w3c.dom.Document doc, java.io.File file) throws Exception {
        javax.xml.transform.Transformer transformer = javax.xml.transform.TransformerFactory.newInstance().newTransformer();
        transformer.transform(new javax.xml.transform.dom.DOMSource(doc), new javax.xml.transform.stream.StreamResult(file));
    }

    @GetMapping(value = "/view", produces = org.springframework.http.MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> getRecipesView() {
        try {
            javax.xml.transform.TransformerFactory factory = javax.xml.transform.TransformerFactory.newInstance();

            java.io.File xslFile = org.springframework.util.ResourceUtils.getFile("classpath:static/recipes.xsl");
            javax.xml.transform.Source xslt = new javax.xml.transform.stream.StreamSource(xslFile);
            javax.xml.transform.Transformer transformer = factory.newTransformer(xslt);

            javax.xml.transform.Source text = new javax.xml.transform.stream.StreamSource(new java.io.File(Constants.XML_OUTPUT_PATH));
            java.io.StringWriter writer = new java.io.StringWriter();
            transformer.transform(text, new javax.xml.transform.stream.StreamResult(writer));

            return ResponseEntity.ok(writer.toString());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error processing XSL: " + e.getMessage());
        }
    }

    @GetMapping("/filter/cuisine")
    public ResponseEntity<List<Recipe>> filterByCuisine(@org.springframework.web.bind.annotation.RequestParam String cuisine) {
        List<Recipe> filteredRecipes = xmlQueryService.getRecipesByCuisine(cuisine);
        return ResponseEntity.ok(filteredRecipes);
    }

    @GetMapping("/user/current")
    public ResponseEntity<User> getCurrentUser() {
        try {
            java.io.File file = new java.io.File(Constants.XML_OUTPUT_PATH);
            javax.xml.parsers.DocumentBuilderFactory factory = javax.xml.parsers.DocumentBuilderFactory.newInstance();
            org.w3c.dom.Document doc = factory.newDocumentBuilder().parse(file);

            org.w3c.dom.NodeList nodes = doc.getElementsByTagName("user");
            if (nodes.getLength() > 0) {
                org.w3c.dom.Element el = (org.w3c.dom.Element) nodes.item(0);
                User user = new User();
                user.setName(el.getElementsByTagName("name").item(0).getTextContent());
                user.setSurname(el.getElementsByTagName("surname").item(0).getTextContent());
                return ResponseEntity.ok(user);
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = new ArrayList<>();
        try {
            java.io.File file = new java.io.File(Constants.XML_OUTPUT_PATH);
            javax.xml.parsers.DocumentBuilderFactory factory = javax.xml.parsers.DocumentBuilderFactory.newInstance();
            org.w3c.dom.Document doc = factory.newDocumentBuilder().parse(file);
            org.w3c.dom.NodeList nodes = doc.getElementsByTagName("user");
            for (int i = 0; i < nodes.getLength(); i++) {
                org.w3c.dom.Element el = (org.w3c.dom.Element) nodes.item(i);
                User u = new User();
                u.setName(el.getElementsByTagName("name").item(0).getTextContent());
                u.setSurname(el.getElementsByTagName("surname").item(0).getTextContent());
                u.setCookingSkillLevel(el.getElementsByTagName("cookingSkillLevel").item(0).getTextContent());
                u.setPreferredCuisine(el.getElementsByTagName("preferredCuisine").item(0).getTextContent());
                users.add(u);
            }
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
}