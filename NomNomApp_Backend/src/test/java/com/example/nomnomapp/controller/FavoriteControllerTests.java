package com.example.nomnomapp.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.sql.Date;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import com.example.nomnomapp.model.NomNomUser;
import com.example.nomnomapp.model.Recipe;
import com.example.nomnomapp.model.Recipe.RecipeCategory;
import com.example.nomnomapp.model.RecipeList;
import com.example.nomnomapp.model.RecipeList.ListCategory;
import com.example.nomnomapp.repository.RecipeListRepository;
import com.example.nomnomapp.repository.RecipeRepository;
import com.example.nomnomapp.repository.UserRepository;
import com.example.nomnomapp.service.FavoriteService;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class FavoriteControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FavoriteService favoriteService;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private RecipeRepository recipeRepository;
    
    @Autowired
    private RecipeListRepository recipeListRepository;

    private NomNomUser testUser;
    private Recipe testRecipe;
    private RecipeList testFavoritesList;

    @BeforeEach
    public void setup() {
        // Setup test user
        testUser = new NomNomUser("testUser", "test@example.com", "password123");
        testUser = userRepository.save(testUser);

        // Setup test recipe
        testRecipe = new Recipe();
        testRecipe.setTitle("Test Recipe");
        testRecipe.setDescription("A test recipe for favorites");
        testRecipe.setInstructions("Test instructions");
        testRecipe.setCategory(RecipeCategory.Dinner);
        testRecipe.setCreationDate(new Date(System.currentTimeMillis()));
        testRecipe.setNomNomUser(testUser);
        testRecipe = recipeRepository.save(testRecipe);

        // Setup test favorites list
        testFavoritesList = new RecipeList();
        testFavoritesList.setName("My Favorites");
        testFavoritesList.setCategory(ListCategory.Favorites);
        testFavoritesList.setNomNomUser(testUser);
        testFavoritesList = recipeListRepository.save(testFavoritesList);
    }

    @AfterEach
    public void cleanup() {
        // Clean up the test data
        recipeListRepository.deleteAll();
        recipeRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void testAddToFavorites() throws Exception {
        // Perform request to add recipe to favorites
        mockMvc.perform(post("/api/favorites/" + testUser.getUserId() + "/" + testRecipe.getRecipeID())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // Verify the recipe was added to favorites
        List<RecipeList> favoritesLists = recipeListRepository.findByNomNomUser(testUser);
        boolean found = false;
        for (RecipeList list : favoritesLists) {
            if (list.getCategory() == ListCategory.Favorites) {
                for (Recipe recipe : list.getRecipes()) {
                    if (recipe.getRecipeID() == testRecipe.getRecipeID()) {
                        found = true;
                        break;
                    }
                }
            }
        }
        assertTrue(found, "Recipe should be added to favorites");
    }

    @Test
    public void testAddToFavoritesWithInvalidData() throws Exception {
        // Perform request with non-existent user
        int nonExistentUserId = 9999;
        mockMvc.perform(post("/api/favorites/" + nonExistentUserId + "/" + testRecipe.getRecipeID())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testRemoveFromFavorites() throws Exception {
        // First add recipe to favorites
        favoriteService.addToFavorites(testUser.getUserId(), testRecipe.getRecipeID());
        
        // Then remove it
        mockMvc.perform(delete("/api/favorites/" + testUser.getUserId() + "/" + testRecipe.getRecipeID())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // Verify the recipe was removed from favorites
        List<RecipeList> favoritesLists = recipeListRepository.findByNomNomUser(testUser);
        boolean found = false;
        for (RecipeList list : favoritesLists) {
            if (list.getCategory() == ListCategory.Favorites) {
                for (Recipe recipe : list.getRecipes()) {
                    if (recipe.getRecipeID() == testRecipe.getRecipeID()) {
                        found = true;
                        break;
                    }
                }
            }
        }
        assertFalse(found, "Recipe should be removed from favorites");
    }

    @Test
    public void testIsRecipeFavorited() throws Exception {
        // First add recipe to favorites
        favoriteService.addToFavorites(testUser.getUserId(), testRecipe.getRecipeID());
        
        // Check if recipe is favorited
        MvcResult result = mockMvc.perform(get("/api/favorites/" + testUser.getUserId() + "/" + testRecipe.getRecipeID())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Verify result
        assertEquals("true", result.getResponse().getContentAsString());
    }

    @Test
    public void testGetFavoriteRecipes() throws Exception {
        // First add recipe to favorites
        favoriteService.addToFavorites(testUser.getUserId(), testRecipe.getRecipeID());
        
        // Get favorite recipes and verify status
        mockMvc.perform(get("/api/favorites/" + testUser.getUserId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        
        // Directly verify with the service instead of parsing the response
        List<Recipe> favoriteRecipes = favoriteService.getFavoriteRecipes(testUser.getUserId());
        assertEquals(1, favoriteRecipes.size());
        assertEquals(testRecipe.getRecipeID(), favoriteRecipes.get(0).getRecipeID());
    }

    @Test
    public void testGetFavoriteRecipesByUsername() throws Exception {
        // First add recipe to favorites
        favoriteService.addToFavorites(testUser.getUserId(), testRecipe.getRecipeID());
        
        // Get favorite recipes by username and verify status
        mockMvc.perform(get("/api/favorites/username/" + testUser.getUsername())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        
        // Directly verify with the service instead of parsing the response
        List<Recipe> favoriteRecipes = favoriteService.getFavoriteRecipesByUsername(testUser.getUsername());
        assertEquals(1, favoriteRecipes.size());
        assertEquals(testRecipe.getRecipeID(), favoriteRecipes.get(0).getRecipeID());
    }
} 