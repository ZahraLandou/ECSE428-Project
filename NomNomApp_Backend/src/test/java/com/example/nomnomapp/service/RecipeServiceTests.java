package com.example.nomnomapp.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.example.nomnomapp.model.NomNomUser;
import com.example.nomnomapp.model.Recipe;
import com.example.nomnomapp.model.Recipe.RecipeCategory;
import com.example.nomnomapp.repository.RecipeRepository;
import java.sql.Date;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.List;
import java.util.Arrays;

@ExtendWith(MockitoExtension.class)
public class RecipeServiceTests {

    @Mock
    private RecipeRepository recipeRepo;

    @InjectMocks
    private RecipeService recipeServ;

    private Recipe testRecipe;
    private NomNomUser testUser;

    @BeforeEach
    void setUp() {
        testUser = new NomNomUser("testUser", "test@example.com", "password123");
        testRecipe = new Recipe(0,
                                "French Cheese cake",
                                "hi my name is Thierry and I want to share this family recipe from my father Pierre",
                                "1-Put cheese, 2-Put cake, 3-Bake for 10 minutes",
                                new Date(System.currentTimeMillis()),
                                Recipe.RecipeCategory.Dessert,
                                203,
                                "picture",
                                3.4,
                                testUser);
    }

    //createRecipe (check that the new recipe contains the right info)
    @Test
    void testCreateRecipe_Success() {
        when(recipeRepo.save(testRecipe)).thenReturn(testRecipe);

        Recipe savedRecipe = recipeServ.createRecipe(testRecipe);

        assertNotNull(savedRecipe);
        assertEquals("French Cheese cake", savedRecipe.getTitle());
        assertEquals(203, savedRecipe.getLikes());
        assertEquals(0, savedRecipe.getRecipeID());
        assertEquals("hi my name is Thierry and I want to share this family recipe from my father Pierre", savedRecipe.getDescription());
        verify(recipeRepo, times(1)).save(testRecipe);
    }
    //Successfully deletes an existing recipe
    @Test
    void testDeleteRecipeByTitle_Success() {
        when(recipeRepo.findById(0)).thenReturn(Optional.of(testRecipe));

        assertDoesNotThrow(() -> recipeServ.deleteRecipeById(0));

        verify(recipeRepo, times(1)).delete(testRecipe);
    }

    //delete non-existent recipe (return exception)
    @Test
    void testDeleteRecipeById_RecipeNotFound() {
        when(recipeRepo.findById(0)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> recipeServ.deleteRecipeById(0));

        assertEquals("Recipe with ID '0' not found.", exception.getMessage());
        verify(recipeRepo, never()).delete(any());
    }
    
    //delete with a null ID
    @Test
    void testDeleteRecipeById_NullId() {
        Integer nullId = null;
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> recipeServ.deleteRecipeById(nullId));

        assertEquals("Recipe with ID 'null' not found.", exception.getMessage());
        verify(recipeRepo, never()).delete(any());
    }
    //getAllRecipes
    @Test
    void testGetAllRecipes_Success() {
        List<Recipe> recipes = Arrays.asList(testRecipe, new Recipe());
        when(recipeRepo.findAll()).thenReturn(recipes);

        List<Recipe> result = recipeServ.getAllRecipes();

        assertEquals(2, result.size());
        verify(recipeRepo, times(1)).findAll();
    }
    //getRecipesByTitle (No Results)
    @Test
    void testGetRecipesByTitle_NotFound() {
        when(recipeRepo.findRecipeByTitle("Strawberry Cake")).thenReturn(List.of());

        List<Recipe> recipes = recipeServ.getRecipesByTitle("Strawberry Cake");

        assertTrue(recipes.isEmpty());
    }

    //getRecipesByCategory (Success)
    @Test
    void testGetRecipesByCategory_Success() {
        when(recipeRepo.findRecipeByCategory(RecipeCategory.Dessert)).thenReturn(List.of(testRecipe));

        List<Recipe> recipes = recipeServ.getRecipesByCategory(RecipeCategory.Dessert);

        assertEquals(1, recipes.size());
        assertEquals(RecipeCategory.Dessert, recipes.get(0).getCategory());
    }

    //getRecipesByCategory (No Results)
    @Test
    void testGetRecipesByCategory_NotFound() {
        when(recipeRepo.findRecipeByCategory(RecipeCategory.Breakfast)).thenReturn(List.of());

        List<Recipe> recipes = recipeServ.getRecipesByCategory(RecipeCategory.Breakfast);

        assertTrue(recipes.isEmpty());
    }
}
