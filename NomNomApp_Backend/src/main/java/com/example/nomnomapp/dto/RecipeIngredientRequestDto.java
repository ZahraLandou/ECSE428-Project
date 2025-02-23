package com.example.nomnomapp.dto;



public class RecipeIngredientRequestDto {

    private double quantity;
    private String unit;
    private int recipeId;
    private String ingredientName;

    public RecipeIngredientRequestDto() {
        // Default constructor
    }

    public RecipeIngredientRequestDto(double quantity, String unit, int recipeId, String ingredientName) {
        this.quantity = quantity;
        this.unit = unit;
        this.recipeId = recipeId;
        this.ingredientName = ingredientName;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    public String getIngredientName() {
        return ingredientName;
    }

    public void setIngredientName(String ingredientName) {
        this.ingredientName = ingredientName;
    }
}
