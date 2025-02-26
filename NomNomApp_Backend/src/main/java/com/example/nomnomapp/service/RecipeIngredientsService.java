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
    public List<RecipeIngredients> getAllRecipeIngredients() {
        return (List<RecipeIngredients>) recipeIngredientsRepository.findAll();
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
        // 1) Validate quantity
        if (updateRi.getQuantity() < 0) {
            throw new IllegalArgumentException("Invalid quantity: cannot be negative");
        }

        // 2) Validate unit not empty
        if (updateRi.getUnit() == null || updateRi.getUnit().trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid unit: cannot be empty");
        }

        // 3) Example: special mismatch rule for onion => can't have unit "clove"
        //    (Adjust or remove if this is purely illustrative.)
        if ("onion".equalsIgnoreCase(updateRi.getIngredient().getName())
                && "clove".equalsIgnoreCase(updateRi.getUnit())) {
            throw new IllegalArgumentException("Unit mismatch for ingredient 'onion' (example)");
        }

        // 4) Look up the existing record in the DB
        int id = updateRi.getRecipeIngredientID();
        RecipeIngredients existing = recipeIngredientsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "RecipeIngredient not found with id: " + id));

        // 5) Update fields
        existing.setQuantity(updateRi.getQuantity());
        existing.setUnit(updateRi.getUnit());

        // 6) Persist
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
