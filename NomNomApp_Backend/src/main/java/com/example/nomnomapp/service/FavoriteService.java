package com.example.nomnomapp.service;

import com.example.nomnomapp.model.NomNomUser;
import com.example.nomnomapp.model.Recipe;
import com.example.nomnomapp.model.RecipeList;
import com.example.nomnomapp.model.RecipeList.ListCategory;
import com.example.nomnomapp.repository.RecipeListRepository;
import com.example.nomnomapp.repository.RecipeRepository;
import com.example.nomnomapp.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service class specifically dedicated to handling favorites-related operations.
 */
@Service
public class FavoriteService {

    @Autowired
    private RecipeListRepository recipeListRepository;

    @Autowired
    private RecipeRepository recipeRepository;
    
    @Autowired
    private UserRepository userRepository;

    /**
     * Find or create a favorites list for a user.
     * 
     * @param user The user to find or create a favorites list for
     * @return The user's favorites list
     */
    @Transactional
    public RecipeList findOrCreateFavoritesList(NomNomUser user) {
        // Check if user already has a Favorites list
        List<RecipeList> userLists = recipeListRepository.findByNomNomUser(user);
        List<RecipeList> favoritesLists = new ArrayList<>();
        
        for (RecipeList list : userLists) {
            if (list.getCategory() == ListCategory.Favorites) {
                favoritesLists.add(list);
            }
        }
        
        if (!favoritesLists.isEmpty()) {
            return favoritesLists.get(0); // Return the first favorites list
        }
        
        // Create a new Favorites list for the user
        RecipeList favoritesList = new RecipeList();
        favoritesList.setName("Favorites");
        favoritesList.setCategory(ListCategory.Favorites);
        favoritesList.setNomNomUser(user);
        return recipeListRepository.save(favoritesList);
    }

    /**
     * Add a recipe to a user's favorites.
     * 
     * @param userId The ID of the user
     * @param recipeId The ID of the recipe to favorite
     * @return The updated favorites list
     * @throws IllegalArgumentException if user or recipe not found
     */
    @Transactional
    public RecipeList addToFavorites(int userId, int recipeId) {
        // Get the user
        Optional<NomNomUser> userOptional = userRepository.findById(userId);
        if (!userOptional.isPresent()) {
            throw new IllegalArgumentException("User with ID " + userId + " not found");
        }
        NomNomUser user = userOptional.get();
        
        // Get the recipe
        Recipe recipe = recipeRepository.findByRecipeId(recipeId);
        if (recipe == null) {
            throw new IllegalArgumentException("Recipe with ID " + recipeId + " not found");
        }
        
        // Get or create the favorites list
        RecipeList favoritesList = findOrCreateFavoritesList(user);
        
        // Check if recipe is already in favorites
        if (favoritesList.getRecipes().contains(recipe)) {
            return favoritesList; // Already favorited, just return the list
        }
        
        // Add the recipe to favorites
        favoritesList.addRecipe(recipe);
        recipe.addRecipeList(favoritesList);
        
        // Save both entities
        recipeRepository.save(recipe);
        return recipeListRepository.save(favoritesList);
    }

    /**
     * Remove a recipe from a user's favorites.
     * 
     * @param userId The ID of the user
     * @param recipeId The ID of the recipe to unfavorite
     * @return The updated favorites list
     * @throws IllegalArgumentException if user or recipe not found
     */
    @Transactional
    public RecipeList removeFromFavorites(int userId, int recipeId) {
        // Get the user
        Optional<NomNomUser> userOptional = userRepository.findById(userId);
        if (!userOptional.isPresent()) {
            throw new IllegalArgumentException("User with ID " + userId + " not found");
        }
        NomNomUser user = userOptional.get();
        
        // Get the recipe
        Recipe recipe = recipeRepository.findByRecipeId(recipeId);
        if (recipe == null) {
            throw new IllegalArgumentException("Recipe with ID " + recipeId + " not found");
        }
        
        // Find the user's favorites list
        List<RecipeList> userLists = recipeListRepository.findByNomNomUser(user);
        List<RecipeList> favoritesLists = new ArrayList<>();
        
        for (RecipeList list : userLists) {
            if (list.getCategory() == ListCategory.Favorites) {
                favoritesLists.add(list);
            }
        }
        
        if (favoritesLists.isEmpty()) {
            // User doesn't have a favorites list, nothing to remove
            throw new IllegalArgumentException("User does not have a favorites list");
        }
        
        RecipeList favoritesList = favoritesLists.get(0);
        
        // Remove the recipe from favorites
        if (favoritesList.getRecipes().contains(recipe)) {
            favoritesList.removeRecipe(recipe);
            recipe.removeRecipeList(favoritesList);
            
            // Save both entities
            recipeRepository.save(recipe);
            return recipeListRepository.save(favoritesList);
        }
        
        // Recipe not in favorites, just return the list
        return favoritesList;
    }

    /**
     * Check if a recipe is in a user's favorites.
     * 
     * @param userId The ID of the user
     * @param recipeId The ID of the recipe to check
     * @return true if the recipe is in the user's favorites, false otherwise
     * @throws IllegalArgumentException if user or recipe not found
     */
    @Transactional(readOnly = true)
    public boolean isRecipeFavorited(int userId, int recipeId) {
        // Get the user
        Optional<NomNomUser> userOptional = userRepository.findById(userId);
        if (!userOptional.isPresent()) {
            throw new IllegalArgumentException("User with ID " + userId + " not found");
        }
        NomNomUser user = userOptional.get();
        
        // Get the recipe
        Recipe recipe = recipeRepository.findByRecipeId(recipeId);
        if (recipe == null) {
            throw new IllegalArgumentException("Recipe with ID " + recipeId + " not found");
        }
        
        // Find the user's favorites list
        List<RecipeList> userLists = recipeListRepository.findByNomNomUser(user);
        List<RecipeList> favoritesLists = new ArrayList<>();
        
        for (RecipeList list : userLists) {
            if (list.getCategory() == ListCategory.Favorites) {
                favoritesLists.add(list);
            }
        }
        
        if (favoritesLists.isEmpty()) {
            return false; // User doesn't have a favorites list
        }
        
        RecipeList favoritesList = favoritesLists.get(0);
        
        // Check if recipe is in favorites
        return favoritesList.getRecipes().contains(recipe);
    }

    /**
     * Get a user's favorite recipes.
     * 
     * @param userId The ID of the user
     * @return List of favorite recipes
     * @throws IllegalArgumentException if user not found
     */
    @Transactional(readOnly = true)
    public List<Recipe> getFavoriteRecipes(int userId) {
        // Get the user
        Optional<NomNomUser> userOptional = userRepository.findById(userId);
        if (!userOptional.isPresent()) {
            throw new IllegalArgumentException("User with ID " + userId + " not found");
        }
        NomNomUser user = userOptional.get();
        
        // Find the user's favorites list
        List<RecipeList> userLists = recipeListRepository.findByNomNomUser(user);
        List<RecipeList> favoritesLists = new ArrayList<>();
        
        for (RecipeList list : userLists) {
            if (list.getCategory() == ListCategory.Favorites) {
                favoritesLists.add(list);
            }
        }
        
        if (favoritesLists.isEmpty()) {
            return new ArrayList<>(); // Return empty list if user doesn't have favorites
        }
        
        RecipeList favoritesList = favoritesLists.get(0);
        
        // Return the recipes in the favorites list
        return new ArrayList<>(favoritesList.getRecipes());
    }

    /**
     * Get a user's favorite recipes by username.
     * 
     * @param username The username of the user
     * @return List of favorite recipes
     * @throws IllegalArgumentException if user not found
     */
    @Transactional(readOnly = true)
    public List<Recipe> getFavoriteRecipesByUsername(String username) {
        // Get the user
        Optional<NomNomUser> userOpt = userRepository.findByUsername(username);
        if (!userOpt.isPresent()) {
            throw new IllegalArgumentException("User with username '" + username + "' not found");
        }
        
        NomNomUser user = userOpt.get();
        
        // Find the user's favorites list
        List<RecipeList> userLists = recipeListRepository.findByNomNomUser(user);
        List<RecipeList> favoritesLists = new ArrayList<>();
        
        for (RecipeList list : userLists) {
            if (list.getCategory() == ListCategory.Favorites) {
                favoritesLists.add(list);
            }
        }
        
        if (favoritesLists.isEmpty()) {
            return new ArrayList<>(); // Return empty list if user doesn't have favorites
        }
        
        RecipeList favoritesList = favoritesLists.get(0);
        
        // Return the recipes in the favorites list
        return new ArrayList<>(favoritesList.getRecipes());
    }
} 