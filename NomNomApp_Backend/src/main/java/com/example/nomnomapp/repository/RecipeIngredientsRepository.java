package com.example.nomnomapp.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.example.nomnomapp.model.RecipeIngredients;

public interface RecipeIngredientsRepository extends CrudRepository<RecipeIngredients, Integer> {

    
    List<RecipeIngredients> findByRecipeRecipeID(int recipeID);
    List<RecipeIngredients> findByIngredientName(String ingredientName);
}