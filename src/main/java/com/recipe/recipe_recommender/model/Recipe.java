package com.recipe.recipe_recommender.model;

import java.util.List;

public class Recipe {

    private int id;
    private String title;
    private List<String> cuisines;
    private String difficulty;

    public Recipe() {}

    public Recipe(int id, String title, List<String> cuisines, String difficulty) {
        this.id = id;
        this.title = title;
        this.cuisines = cuisines;
        this.difficulty = difficulty;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public List<String> getCuisines() { return cuisines; }
    public void setCuisines(List<String> cuisines) { this.cuisines = cuisines; }

    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }

    @Override
    public String toString() {
        return "Recipe{id=" + id + ", title='" + title + "', cuisines=" + cuisines + ", difficulty='" + difficulty + "'}";
    }
}