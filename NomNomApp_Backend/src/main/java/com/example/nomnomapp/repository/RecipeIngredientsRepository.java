package com.example.nomnomapp.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.example.nomnomapp.model.RecipeIngredients;

public interface RecipeIngredientsRepository extends CrudRepository<RecipeIngredients, Integer> {
    List<RecipeIngredients> findIngredientsByRecipeRecipeId(int recipeId);
    List<RecipeIngredients> findByIngredientName(String ingredientName);
}