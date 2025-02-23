package com.example.nomnomapp.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.nomnomapp.model.RecipeIngredients;
import com.example.nomnomapp.repository.RecipeIngredientsRepository;

@Service
public class RecipeIngredientsService {

    @Autowired
    private RecipeIngredientsRepository recipeIngredientsRepository;

    /**
     * Create a new RecipeIngredients record.
     *
     * @param recipeIngredient the RecipeIngredients entity to create
     * @return the saved RecipeIngredients entity
     */
    public RecipeIngredients createRecipeIngredient(RecipeIngredients recipeIngredient) {
        return recipeIngredientsRepository.save(recipeIngredient);
    }

    /**
     * Retrieve a RecipeIngredients record by its ID.
     *
     * @param id the RecipeIngredients ID
     * @return the found RecipeIngredients entity
     */
    public RecipeIngredients getRecipeIngredientById(int id) {
        Optional<RecipeIngredients> result = recipeIngredientsRepository.findById(id);
        return result.orElseThrow(() -> new RuntimeException("RecipeIngredient not found with id: " + id));
    }

    /**
     * Retrieve all RecipeIngredients records.
     *
     * @return an Iterable of RecipeIngredients entities
     */
    public Iterable<RecipeIngredients> getAllRecipeIngredients() {
        return recipeIngredientsRepository.findAll();
    }

    /**
     * Retrieve all RecipeIngredients records associated with a specific recipe.
     *
     * @param recipeID the recipe ID
     * @return a list of RecipeIngredients for the given recipe
     */
    public List<RecipeIngredients> getRecipeIngredientsByRecipeId(int recipeID) {
        return recipeIngredientsRepository.findByRecipeRecipeID(recipeID);
    }

    /**
     * Update an existing RecipeIngredients record.
     *
     * @param recipeIngredient the RecipeIngredients entity with updated data
     * @return the updated RecipeIngredients entity
     */
    public RecipeIngredients updateRecipeIngredient(RecipeIngredients recipeIngredient) {
        // This will update the record if the entity exists
        return recipeIngredientsRepository.save(recipeIngredient);
    }

    /**
     * Delete a RecipeIngredients record by its ID.
     *
     * @param id the RecipeIngredients ID to delete
     */
    public void deleteRecipeIngredient(int id) {
        recipeIngredientsRepository.deleteById(id);
    }
}
