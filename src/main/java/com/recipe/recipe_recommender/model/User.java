package com.recipe.recipe_recommender.model;

public class User {
    private String name;
    private String surname;
    private String cookingSkillLevel;
    private String preferredCuisine;

    public User() {}

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getSurname() { return surname; }
    public void setSurname(String surname) { this.surname = surname; }
    public String getCookingSkillLevel() { return cookingSkillLevel; }
    public void setCookingSkillLevel(String cookingSkillLevel) { this.cookingSkillLevel = cookingSkillLevel; }
    public String getPreferredCuisine() { return preferredCuisine; }
    public void setPreferredCuisine(String preferredCuisine) { this.preferredCuisine = preferredCuisine; }
}