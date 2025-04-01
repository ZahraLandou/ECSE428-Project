package com.example.nomnomapp.service;


import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.nomnomapp.model.Recipe;
import com.example.nomnomapp.model.Recipe.RecipeCategory;
import com.example.nomnomapp.repository.RecipeRepository;
// import com.example.nomnomapp.repository.CollectionRepository;

import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;

import com.example.nomnomapp.exception.NomNomException;
import com.example.nomnomapp.model.Comment;


@Service
public class RecipeService {

    @Autowired
    private RecipeRepository recipeRepository;
    // private CollectionRepository collectionRepository;

    public Recipe createRecipe(Recipe recipe){
        return recipeRepository.save(recipe);
    }

    // public List<Recipe> createRecipeCollection(List<Recipe> collection){
    //     return collectionRepository.save(collection);
    // }

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

    // public void deleteCollectionByCollectionId(Integer collectionId) {
    //     Optional<List<Recipe>> collection = collectionRepository.findById(collectionId);
    //     if (collection.isPresent()) {
    //         collectionRepository.delete(collection.get());
    //     } else {
    //         throw new IllegalArgumentException("Collection with ID '" + collectionId + "' not found.");
    //     }
    // }

    public Recipe getRecipeByID(int recipeID) {
        return recipeRepository.findByRecipeId(recipeID);
    }
    // public List<Recipe> getCollectionById(int collectionID) {
    //     return collectionRepository.findByCollectionId(collectionID);
    // }
    
    // public List<Recipe> getCollectionByName(String collectionName) {
    //     return collectionRepository.findCollectionByTitle(collectionName);
    // }

    public List<Recipe> getAllRecipes() {
        return (List<Recipe>) recipeRepository.findAll();
    }

    public List<Recipe>  getRecipesByTitle(String title) {
        List<Recipe> recipes = recipeRepository.findRecipeByTitle(title);
        if (recipes.isEmpty() ) {
            throw new IllegalArgumentException("No recipes found with title: " + title);
        }
        return recipes;
    }
    
    public List<Recipe> getRecipesByCategory(RecipeCategory recipeCategory) {
        return recipeRepository.findRecipeByCategory(recipeCategory);
    }

    public Recipe likeRecipe(int recipeID) {
        Recipe recipe = recipeRepository.findByRecipeId(recipeID);
        if (recipe == null) {
            throw new RuntimeException("Recipe not found");
        }
        recipe.setLikes(recipe.getLikes() + 1);
        return recipeRepository.save(recipe);
    }

    public Recipe unlikeRecipe(int recipeID) {
        Recipe recipe = recipeRepository.findByRecipeId(recipeID);
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
        Recipe recipe = recipeRepository.findByRecipeId(recipeID);
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
        Recipe recipe = recipeRepository.findByRecipeId(aRecipeID);
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
        return recipeRepository.save(recipe);
              //  recipe;
    }
    public void deleteAllRecipes() {
        recipeRepository.deleteAll();
    }


    /**
     * Method to get recipes containing specific list of ingredients
     * Assuming that the ingredients names are provided as a string separated with coma
     * If there's 2+ ingredients, make sure that the matching recipes contain at least 2 of them
     * @param ingredientNames
     * @return matchingRecipes
     */
    @Transactional
    public List<Recipe> getRecipesByIngredients(String ingredientNames) {
        if (ingredientNames == null || ingredientNames.trim().isEmpty()) {
            throw new IllegalArgumentException("Ingredient names cannot be empty");
        }

        String[] ingredientsArray = ingredientNames.toLowerCase().split(",");
        List<String> ingredientList = Arrays.asList(ingredientsArray);

        List<Recipe> allRecipes = (List<Recipe>) recipeRepository.findAll();
        List<Recipe> matchingRecipes = new ArrayList<>();


        for (Recipe recipe: allRecipes){

            long matchingCount = recipe.getRecipeIngredients().stream()
                    .map(recipeIngredient -> recipeIngredient.getIngredient().getName().toLowerCase()) //retrieve recipe's ingredients
                    .filter(ingredientList::contains) // filter matching ingredients
                    .count(); // count the number of match

            if (matchingCount >= Math.min(2, ingredientList.size())) {
                matchingRecipes.add(recipe);
            }

        }
        if (matchingRecipes.isEmpty()) {
            throw new IllegalArgumentException("No recipes found containing the specified ingredients: " + ingredientNames);
        }
        return  matchingRecipes;

    }
    
}
