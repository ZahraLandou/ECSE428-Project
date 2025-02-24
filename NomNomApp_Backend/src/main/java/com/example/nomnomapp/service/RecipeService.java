package com.example.nomnomapp.service;


import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.nomnomapp.model.Recipe;
import com.example.nomnomapp.model.Recipe.RecipeCategory;
import com.example.nomnomapp.repository.RecipeRepository;

import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;

import com.example.nomnomapp.exception.NomNomException;
import com.example.nomnomapp.model.Comment;


@Service
public class RecipeService {

    @Autowired
    private RecipeRepository recipeRepository;

    public Recipe createRecipe(Recipe recipe){
        return recipeRepository.save(recipe);

    }

    /**
     * Deletes a recipe by id.
     *
     * @param recipeId id of the recipe to delete.
     * @throws IllegalArgumentException if the recipe does not exist.
    */
    public void deleteRecipeById(Integer recipeId) {
        Optional<Recipe> recipe = recipeRepository.findById(recipeId);
        if (recipe.isPresent()) {
            recipeRepository.delete(recipe.get());
        } else {
            throw new IllegalArgumentException("Recipe with ID '" + recipeId + "' not found.");
        }
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

    public Recipe likeRecipe(int recipeID) {
        Recipe recipe = recipeRepository.findRecipeById(recipeID);
        if (recipe == null) {
            throw new RuntimeException("Recipe not found");
        }
        recipe.setLikes(recipe.getLikes() + 1);
        return recipeRepository.save(recipe);
    }

    public Recipe unlikeRecipe(int recipeID) {
        Recipe recipe = recipeRepository.findRecipeById(recipeID);
        if (recipe == null) {
            throw new RuntimeException("Recipe not found");
        }
        // Ensure that likes don’t go negative
        if (recipe.getLikes() > 0) {
            recipe.setLikes(recipe.getLikes() - 1);
        }
        return recipeRepository.save(recipe);
    }

    public int getLikes(int recipeID) {
        Recipe recipe = recipeRepository.findRecipeById(recipeID);
        if (recipe == null) {
            throw new RuntimeException("Recipe not found");
        }
        return recipe.getLikes();
    }

    @Transactional
    public Recipe updateAverageRating(int aRecipeID){
        if (aRecipeID<0) {
            throw new NomNomException(HttpStatus.BAD_REQUEST, "Recipe ID is not valid.");
        }
        Recipe recipe = recipeRepository.findRecipeById(aRecipeID);
        List<Comment> comments = recipe.getComments();
        OptionalDouble averageRating = comments.stream().mapToDouble(Comment::getRating).average();
        if(averageRating.isPresent()){
            // mapToDouble generated a non-empty list
            // https://docs.oracle.com/javase/8/docs/api/java/util/stream/DoubleStream.html#average--
            if(averageRating.getAsDouble() == Double.NaN){
                throw new NomNomException(HttpStatus.INTERNAL_SERVER_ERROR, "Averaging Ratings returned a NaN");
            }else{
                recipe.setAverageRating(averageRating.getAsDouble());
            }
        }else{
            // mapToDouble generated an empty list
            recipe.setAverageRating(0);
        }
        return recipe;
    }
    
}
