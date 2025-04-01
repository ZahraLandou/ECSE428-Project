package com.example.nomnomapp.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import com.example.nomnomapp.model.Recipe;
import com.example.nomnomapp.model.Recipe.RecipeCategory;
import com.example.nomnomapp.service.RecipeService;

@RestController
@RequestMapping("api/recipes")
public class RecipeController {
    private RecipeService recipeService;


    public RecipeController(RecipeService recipeService){
        this.recipeService = recipeService;
    }


    @PostMapping
    public ResponseEntity<Recipe> createRecipe(@RequestBody Recipe recipe){

        return ResponseEntity.ok(recipeService.createRecipe(recipe));


    }

    @GetMapping
    public ResponseEntity<List<Recipe>> getAllRecipes() {
        return ResponseEntity.ok(recipeService.getAllRecipes());
    }

    @GetMapping("/{recipeID}")
    public ResponseEntity<Recipe> getRecipeById(@PathVariable int recipeID) {

        return ResponseEntity.ok(recipeService.getRecipeByID(recipeID));

    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<Recipe>> getRecipesByCategory(@PathVariable RecipeCategory category) {
        return ResponseEntity.ok(recipeService.getRecipesByCategory(category));
    }

    /**
     * Increment the like count for a recipe.
     * Example: POST /recipes/{recipeID}/like
     */
    @PostMapping("/{recipeID}/like")
    public ResponseEntity<Recipe> likeRecipe(@PathVariable int recipeID) {
        Recipe recipe = recipeService.likeRecipe(recipeID);
        return ResponseEntity.ok(recipe);
    }


    /**
     * Decrement the like count for a recipe.
     * Example: DELETE /recipes/{recipeID}/like
     */
    @DeleteMapping("/{recipeID}/like")
    public ResponseEntity<Recipe> unlikeRecipe(@PathVariable int recipeID) {
        Recipe recipe = recipeService.unlikeRecipe(recipeID);
        return ResponseEntity.ok(recipe);
    }

    /**
     * Get the current like count for a recipe.
     * Example: GET /recipes/{recipeID}/likes
     */
    @GetMapping("/{recipeID}/likes")
    public ResponseEntity<Integer> getLikes(@PathVariable int recipeID) {
        int likes = recipeService.getLikes(recipeID);
        return ResponseEntity.ok(likes);
    }

    /**
     * Get recipes that contain a list of ingredients
     * If multiple ingredients are provided, at least two of them should be present in the returned recipes.
     * Example request: GET /recipes/ingredients?ingredientNames=garlic,onion,shrimp
     * @return list of corresponding ingredient
     */
    @GetMapping("/{ingredient}")
    public ResponseEntity<List<Recipe>> getRecipesByIngredients(@RequestParam String ingredientNames){
        List<Recipe> recipes = recipeService.getRecipesByIngredients(ingredientNames);
        return ResponseEntity.ok(recipes);
    }


}
