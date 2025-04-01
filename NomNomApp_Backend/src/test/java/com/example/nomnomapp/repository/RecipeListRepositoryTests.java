package com.example.nomnomapp.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.nomnomapp.model.NomNomUser;
import com.example.nomnomapp.model.Recipe;
import com.example.nomnomapp.model.RecipeList;
import com.example.nomnomapp.model.RecipeList.ListCategory;
import com.example.nomnomapp.model.Recipe.RecipeCategory;

import jakarta.transaction.Transactional;

@SpringBootTest
public class RecipeListRepositoryTests {
    @Autowired
    private RecipeListRepository recipeListRepository;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @Transactional
    public void testCreateFavoritesList() {
        // Create and save a user
        NomNomUser user = new NomNomUser("testUser", "test@example.com", "password123");
        user = userRepository.save(user);

        // Create a favorites list
        RecipeList favoritesList = new RecipeList();
        favoritesList.setName("My Favorites");
        favoritesList.setCategory(ListCategory.Favorites);
        favoritesList.setNomNomUser(user);

        // Save the favorites list
        favoritesList = recipeListRepository.save(favoritesList);

        // Retrieve the list
        Optional<RecipeList> retrievedListOpt = recipeListRepository.findById(favoritesList.getRecipeListID());
        
        // Assert that it exists and has the correct properties
        assertTrue(retrievedListOpt.isPresent());
        RecipeList retrievedList = retrievedListOpt.get();
        assertEquals("My Favorites", retrievedList.getName());
        assertEquals(ListCategory.Favorites, retrievedList.getCategory());
        assertEquals(user.getUserId(), retrievedList.getNomNomUser().getUserId());
    }

    @Test
    @Transactional
    public void testFindFavoritesListsByUser() {
        // Create and save a user
        NomNomUser user = new NomNomUser("favoritesUser", "favorites@example.com", "password123");
        user = userRepository.save(user);

        // Create two recipe lists for the user, one favorites and one regular
        RecipeList favoritesList = new RecipeList();
        favoritesList.setName("My Favorites");
        favoritesList.setCategory(ListCategory.Favorites);
        favoritesList.setNomNomUser(user);
        recipeListRepository.save(favoritesList);

        RecipeList regularList = new RecipeList();
        regularList.setName("Regular Recipes");
        regularList.setCategory(ListCategory.Regular);
        regularList.setNomNomUser(user);
        recipeListRepository.save(regularList);

        // Find all lists by user
        List<RecipeList> userLists = recipeListRepository.findByNomNomUser(user);
        assertEquals(2, userLists.size());

        // Find favorites lists by category
        List<RecipeList> favoritesLists = recipeListRepository.findByCategory(ListCategory.Favorites);
        assertTrue(favoritesLists.size() >= 1);
        
        // Make sure there is at least one favorites list for the user
        boolean userHasFavorites = false;
        for (RecipeList list : favoritesLists) {
            if (list.getNomNomUser().getUserId() == user.getUserId()) {
                userHasFavorites = true;
                break;
            }
        }
        assertTrue(userHasFavorites);
    }

    @Test
    @Transactional
    public void testAddRecipeToFavorites() {
        // Create and save a user
        NomNomUser user = new NomNomUser("recipeUser", "recipe@example.com", "password123");
        user = userRepository.save(user);

        // Create a favorites list
        RecipeList favoritesList = new RecipeList();
        favoritesList.setName("My Favorites");
        favoritesList.setCategory(ListCategory.Favorites);
        favoritesList.setNomNomUser(user);
        favoritesList = recipeListRepository.save(favoritesList);

        // Create a recipe
        Recipe recipe = new Recipe();
        recipe.setTitle("Test Recipe");
        recipe.setDescription("A test recipe for favorites");
        recipe.setInstructions("Test instructions");
        recipe.setCategory(RecipeCategory.Dinner);
        recipe.setCreationDate(new Date(System.currentTimeMillis()));
        recipe.setNomNomUser(user);
        recipe = recipeRepository.save(recipe);

        // Add the recipe to favorites
        favoritesList.addRecipe(recipe);
        recipe.addRecipeList(favoritesList);
        
        // Save both entities
        recipeRepository.save(recipe);
        recipeListRepository.save(favoritesList);

        // Retrieve the updated favorites list
        RecipeList updatedList = recipeListRepository.findById(favoritesList.getRecipeListID()).get();
        
        // Check if the recipe is in the favorites list
        boolean recipeFound = false;
        for (Recipe r : updatedList.getRecipes()) {
            if (r.getRecipeID() == recipe.getRecipeID()) {
                recipeFound = true;
                break;
            }
        }
        assertTrue(recipeFound);
        
        // Also check from the recipe side
        Recipe updatedRecipe = recipeRepository.findByRecipeId(recipe.getRecipeID());
        boolean listFound = false;
        for (RecipeList list : updatedRecipe.getRecipeLists()) {
            if (list.getRecipeListID() == favoritesList.getRecipeListID()) {
                listFound = true;
                break;
            }
        }
        assertTrue(listFound);
    }

    @Test
    @Transactional
    public void testRemoveRecipeFromFavorites() {
        // Create and save a user
        NomNomUser user = new NomNomUser("unfavUser", "unfav@example.com", "password123");
        user = userRepository.save(user);

        // Create a favorites list
        RecipeList favoritesList = new RecipeList();
        favoritesList.setName("Favorites to Remove From");
        favoritesList.setCategory(ListCategory.Favorites);
        favoritesList.setNomNomUser(user);
        favoritesList = recipeListRepository.save(favoritesList);

        // Create a recipe
        Recipe recipe = new Recipe();
        recipe.setTitle("Recipe to Unfavorite");
        recipe.setDescription("A recipe to remove from favorites");
        recipe.setInstructions("Test instructions");
        recipe.setCategory(RecipeCategory.Lunch);
        recipe.setCreationDate(new Date(System.currentTimeMillis()));
        recipe.setNomNomUser(user);
        recipe = recipeRepository.save(recipe);

        // Add the recipe to favorites
        favoritesList.addRecipe(recipe);
        recipe.addRecipeList(favoritesList);
        recipeRepository.save(recipe);
        recipeListRepository.save(favoritesList);

        // Verify recipe is in favorites
        RecipeList savedList = recipeListRepository.findById(favoritesList.getRecipeListID()).get();
        assertEquals(1, savedList.getRecipes().size());

        // Remove the recipe from favorites
        savedList.removeRecipe(recipe);
        recipe.removeRecipeList(savedList);
        recipeRepository.save(recipe);
        recipeListRepository.save(savedList);

        // Verify recipe is removed from favorites
        RecipeList updatedList = recipeListRepository.findById(favoritesList.getRecipeListID()).get();
        assertEquals(0, updatedList.getRecipes().size());
        
        // Also check from the recipe side
        Recipe updatedRecipe = recipeRepository.findByRecipeId(recipe.getRecipeID());
        assertEquals(0, updatedRecipe.getRecipeLists().size());
    }
} 