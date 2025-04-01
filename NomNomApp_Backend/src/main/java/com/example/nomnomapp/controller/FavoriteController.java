package com.example.nomnomapp.controller;

import com.example.nomnomapp.model.Recipe;
import com.example.nomnomapp.model.RecipeList;
import com.example.nomnomapp.service.FavoriteService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for handling favorite recipe operations.
 */
@RestController
@RequestMapping("/api/favorites")
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    /**
     * Add a recipe to a user's favorites.
     * 
     * @param userId The ID of the user
     * @param recipeId The ID of the recipe to favorite
     * @return Success message and the updated favorites list
     */
    @PostMapping("/{userId}/{recipeId}")
    public ResponseEntity<?> addToFavorites(@PathVariable int userId, @PathVariable int recipeId) {
        try {
            RecipeList updatedList = favoriteService.addToFavorites(userId, recipeId);
            return ResponseEntity.ok(updatedList);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error adding recipe to favorites: " + e.getMessage());
        }
    }

    /**
     * Remove a recipe from a user's favorites.
     * 
     * @param userId The ID of the user
     * @param recipeId The ID of the recipe to unfavorite
     * @return Success message and the updated favorites list
     */
    @DeleteMapping("/{userId}/{recipeId}")
    public ResponseEntity<?> removeFromFavorites(@PathVariable int userId, @PathVariable int recipeId) {
        try {
            RecipeList updatedList = favoriteService.removeFromFavorites(userId, recipeId);
            return ResponseEntity.ok(updatedList);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error removing recipe from favorites: " + e.getMessage());
        }
    }

    /**
     * Check if a recipe is in a user's favorites.
     * 
     * @param userId The ID of the user
     * @param recipeId The ID of the recipe to check
     * @return Boolean indicating if the recipe is favorited
     */
    @GetMapping("/{userId}/{recipeId}")
    public ResponseEntity<?> isRecipeFavorited(@PathVariable int userId, @PathVariable int recipeId) {
        try {
            boolean isFavorited = favoriteService.isRecipeFavorited(userId, recipeId);
            return ResponseEntity.ok(isFavorited);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error checking if recipe is favorited: " + e.getMessage());
        }
    }

    /**
     * Get all favorite recipes for a user.
     * 
     * @param userId The ID of the user
     * @return List of favorite recipes
     */
    @GetMapping("/{userId}")
    public ResponseEntity<?> getFavoriteRecipes(@PathVariable int userId) {
        try {
            List<Recipe> favorites = favoriteService.getFavoriteRecipes(userId);
            return ResponseEntity.ok(favorites);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error getting favorite recipes: " + e.getMessage());
        }
    }

    /**
     * Get all favorite recipes for a user by username.
     * 
     * @param username The username of the user
     * @return List of favorite recipes
     */
    @GetMapping("/username/{username}")
    public ResponseEntity<?> getFavoriteRecipesByUsername(@PathVariable String username) {
        try {
            List<Recipe> favorites = favoriteService.getFavoriteRecipesByUsername(username);
            return ResponseEntity.ok(favorites);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error getting favorite recipes: " + e.getMessage());
        }
    }
} 