package com.recipe.recipe_recommender.controller;

import com.recipe.recipe_recommender.Constants;
import com.recipe.recipe_recommender.model.Recipe;
import com.recipe.recipe_recommender.xml.XMLParser;
import com.recipe.recipe_recommender.xml.XMLQueryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}