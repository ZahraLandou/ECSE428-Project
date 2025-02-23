package com.example.nomnomapp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.nomnomapp.model.Recipe;
import com.example.nomnomapp.model.Recipe.RecipeCategory;
import com.example.nomnomapp.repository.RecipeRepository;

@Service
public class RecipeService {

    @Autowired
    private RecipeRepository recipeRepository;

    public Recipe createRecipe(Recipe recipe){
        return recipeRepository.save(recipe);

    }


    public Recipe getRecipeByID(int recipeID) {
        return recipeRepository.findRecipeById(recipeID);
    }
    

    public List<Recipe> getAllRecipes() {
        return (List<Recipe>) recipeRepository.findAll();
    }

    public List<Recipe>  getRecipesByTitle(String title) {
            return recipeRepository.findRecipeByTitle(title);

    }
    
    public List<Recipe> getRecipesByCategory(RecipeCategory recipeCategory) {
        return recipeRepository.findRecipeByCategory(recipeCategory);
    }

    
}
