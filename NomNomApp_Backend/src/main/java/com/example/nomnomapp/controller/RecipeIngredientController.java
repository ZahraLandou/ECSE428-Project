package com.example.nomnomapp.controller;

import com.example.nomnomapp.model.RecipeIngredients;
import com.example.nomnomapp.service.RecipeIngredientsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/recipeIngredients")

public class RecipeIngredientController {
    @Autowired
    private RecipeIngredientsService recipeIngredientsService;
    /**
     * Create a new RecipeIngredient.
     * Example: POST /api/recipeIngredients
     */
    @PostMapping
    public ResponseEntity<RecipeIngredients> createRecipeIngredient(@RequestBody RecipeIngredients recipeIngredient) {
        RecipeIngredients created = recipeIngredientsService.createRecipeIngredient(recipeIngredient);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }
    /**
     * Retrieve a RecipeIngredient by its ID.
     * Example: GET /api/recipeIngredients/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<RecipeIngredients> getRecipeIngredientById(@PathVariable int id) {
        RecipeIngredients ingredient = recipeIngredientsService.getRecipeIngredientById(id);
        return ResponseEntity.ok(ingredient);
    }
    /**
     * Retrieve all RecipeIngredients.
     * Example: GET /api/recipeIngredients
     */
    @GetMapping
    public ResponseEntity<List<RecipeIngredients>> getAllRecipeIngredients() {
        List<RecipeIngredients> ingredients = (List<RecipeIngredients>) recipeIngredientsService.getAllRecipeIngredients();
        return ResponseEntity.ok(ingredients);
    }
    /**
     * Retrieve all RecipeIngredients for a specific Recipe.
     * Example: GET /api/recipeIngredients/recipe/{recipeId}
     */
    @GetMapping("/recipe/{recipeId}")
    public ResponseEntity<List<RecipeIngredients>> getRecipeIngredientsByRecipeId(@PathVariable int recipeId) {
        List<RecipeIngredients> ingredients = recipeIngredientsService.getRecipeIngredientsByRecipeId(recipeId);
        return ResponseEntity.ok(ingredients);
    }
    /**
     * Update an existing RecipeIngredient.
     * Example: PUT /api/recipeIngredients/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<RecipeIngredients> updateRecipeIngredient(@PathVariable int id,
                                                                    @RequestBody RecipeIngredients recipeIngredient) {
        // Ensure the provided RecipeIngredient has the correct ID.
        recipeIngredient.setRecipeIngredientID(id);
        RecipeIngredients updated = recipeIngredientsService.updateRecipeIngredient(recipeIngredient);
        return ResponseEntity.ok(updated);
    }
    /**
     * Delete a RecipeIngredient by its ID.
     * Example: DELETE /api/recipeIngredients/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecipeIngredient(@PathVariable int id) {
        recipeIngredientsService.deleteRecipeIngredient(id);
        return ResponseEntity.noContent().build();
    }




}
