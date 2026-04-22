package com.recipe.recipe_recommender;

public class Constants {
    public static final String BBC_URL = "https://www.bbcgoodfood.com/recipes/collection/budget-autumn";

    public static final String XML_OUTPUT_PATH = "src/main/resources/recipes.xml";
    public static final String XML_INDENT = "4";
    public static final String XML_INDENT_PROPERTY = "{http://xml.apache.org/xslt}indent-amount";

    public static final String[] CUISINE_TYPES = {
            "Italian", "Asian", "British", "French", "Mexican",
            "Mediterranean", "Indian", "Chinese", "Japanese",
            "American", "Middle Eastern", "Thai", "Greek", "Spanish"
    };

    public static final String[] DIFFICULTY_LEVELS = {
            "Beginner", "Intermediate", "Advanced"
    };

    public static final String XML_SCHEMA_PATH = "src/main/resources/recipes.xsd";

    public static final String XPATH_FIRST_USER_SKILL = "(//user)[1]/cookingSkillLevel";
    public static final String XPATH_RECOMMEND_BY_SKILL = "//recipe[difficulty = '%s']";
    public static final String XPATH_RECOMMEND_BY_SKILL_AND_CUISINE = "//recipe[difficulty = '%s' and cuisines/cuisine = '%s']";
    public static final String XPATH_FIRST_USER_CUISINE = "(//user)[1]/preferredCuisine";
    public static final String XPATH_RECIPE_BY_ID = "//recipe[@id = '%s']";
    public static final String XPATH_RECIPES_BY_CUISINE = "//recipe[cuisines/cuisine = '%s']";
}