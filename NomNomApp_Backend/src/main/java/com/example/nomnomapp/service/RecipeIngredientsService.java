package com.example.nomnomapp.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.nomnomapp.model.Recipe;
import com.example.nomnomapp.model.RecipeIngredients;
import com.example.nomnomapp.repository.RecipeIngredientsRepository;
import jakarta.transaction.Transactional;


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
    /**
     * Create a new RecipeIngredients record.
     *
     * @param recipeIngredient the RecipeIngredients entity to create
     * @return the saved RecipeIngredients entity
     */
    public RecipeIngredients createRecipeIngredient(RecipeIngredients recipeIngredient) {
        if (recipeIngredient.getQuantity() <= 0) {
            throw new IllegalArgumentException("Recipe ingredient quantity cannot be empty");
        }
        if (recipeIngredient.getUnit() == null || recipeIngredient.getUnit().trim().isEmpty()) {
            throw new IllegalArgumentException("Recipe ingredient unit cannot be empty");
        }
        // Validate the associated Ingredient's name.
        if (recipeIngredient.getIngredient() == null ||
                recipeIngredient.getIngredient().getName() == null ||
                recipeIngredient.getIngredient().getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Ingredient name cannot be empty");
        }

        return recipeIngredientsRepository.save(recipeIngredient);
    }




    /**
     * Retrieve a RecipeIngredients record by its ID.
     *
     * @param id the RecipeIngredients ID
     * @return the found RecipeIngredients entity
     */
    public RecipeIngredients getRecipeIngredientById(int id) {
        return recipeIngredientsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("RecipeIngredient not found with id: " + id));
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
        return recipeIngredientsRepository.findIngredientsByRecipeRecipeId(recipeID);
    }

    /**
     * Update an existing RecipeIngredients record.
     *
     * @param updateRi the RecipeIngredients entity with updated data
     * @return the updated RecipeIngredients entity
     */
    public RecipeIngredients updateRecipeIngredient(RecipeIngredients updateRi) {
        // This will update the record if the entity exists
        int id = updateRi.getRecipeIngredientID();
        RecipeIngredients existing = recipeIngredientsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("RecipeIngredient not found with id: " + id));
        existing.setQuantity(updateRi.getQuantity());
        existing.setUnit(updateRi.getUnit());

        return recipeIngredientsRepository.save(existing);
    }

    /**
     * Delete a RecipeIngredients record by its ID.
     *
     * @param id the RecipeIngredients ID to delete
     */
    public void deleteRecipeIngredient(int id) {
        RecipeIngredients ri = recipeIngredientsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("RecipeIngredient not found with id: " + id));
        recipeIngredientsRepository.deleteById(id);
    }

    @Transactional
    public void deleteAllRecipeIngredients() {

        recipeIngredientsRepository.deleteAll();
    }
}
