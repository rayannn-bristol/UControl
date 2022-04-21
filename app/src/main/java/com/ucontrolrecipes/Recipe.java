package com.ucontrolrecipes;

import java.io.Serializable;

public class Recipe implements Serializable {
    private int id;
    private String title;
    private String ingredientsList;
    private String instructions;
    private String imageURL;
    private int readyInMinutes;
    private int servings;

    public Recipe(){
    }

    public Recipe(int id, String title, String ingredientsList, String instructions, String imageURL, int readyInMinutes, int servings){
        setId(id);
        setTitle(title);
        setIngredientsList(ingredientsList);
        setInstructions(instructions);
        setImageURL(imageURL);
        setReadyInMinutes(readyInMinutes);
        setServings(servings);
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setIngredientsList(String ingredientsList) {
        this.ingredientsList = ingredientsList;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public void setReadyInMinutes(int readyInMinutes) {
        this.readyInMinutes = readyInMinutes;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getIngredientsList() {
        return ingredientsList;
    }

    public String getInstructions() {
        return instructions;
    }

    public String getImageURL() {
        return imageURL;
    }

    public int getReadyInMinutes() {
        return readyInMinutes;
    }

    public int getServings() {
        return servings;
    }
}
