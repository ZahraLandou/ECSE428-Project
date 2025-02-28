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
    private RecipeRepository recipeRepository;

    @InjectMocks
    private RecipeService recipeService;

    private Recipe testRecipe;
    private NomNomUser testUser;

    @BeforeEach
    void setUp() {
        testUser = new NomNomUser("testUser", "test@example.com", "password123");
        testRecipe = new Recipe("French Cheese cake",
                                "hi my name is Thierry and I want to share this family recipe from my father Pierre",
                                "1-Put cheese, 2-Put cake, 3-Bake for 10 minutes",
                                Recipe.RecipeCategory.Dessert,
                                203,
                                "picture",
                                3.4,
                                testUser);
    }

    //createRecipe (check that the new recipe contains the right info)
    @Test
    void testCreateRecipe_Success() {
        when(recipeRepository.save(testRecipe)).thenReturn(testRecipe);

        Recipe savedRecipe = recipeService.createRecipe(testRecipe);

        assertNotNull(savedRecipe);
        assertEquals("French Cheese cake", savedRecipe.getTitle());
        assertEquals(203, savedRecipe.getLikes());
        assertEquals(0, savedRecipe.getRecipeID());
        assertEquals("hi my name is Thierry and I want to share this family recipe from my father Pierre", savedRecipe.getDescription());
        verify(recipeRepository, times(1)).save(testRecipe);
    }
    //Successfully deletes an existing recipe
    @Test
    void testDeleteRecipeByTitle_Success() {
        when(recipeRepository.findById(0)).thenReturn(Optional.of(testRecipe));

        assertDoesNotThrow(() -> recipeService.deleteRecipeById(0));

        verify(recipeRepository, times(1)).delete(testRecipe);
    }

    //delete non-existent recipe (return exception)
    @Test
    void testDeleteRecipeById_RecipeNotFound() {
        when(recipeRepository.findById(0)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> recipeService.deleteRecipeById(0));

        assertEquals("Recipe with ID '0' not found.", exception.getMessage());
        verify(recipeRepository, never()).delete(any());
    }
    
    //delete with a null ID
    @Test
    void testDeleteRecipeById_NullId() {
        Integer nullId = null;
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> recipeService.deleteRecipeById(nullId));

        assertEquals("Recipe with ID 'null' not found.", exception.getMessage());
        verify(recipeRepository, never()).delete(any());
    }
    //getAllRecipes
    @Test
    void testGetAllRecipes_Success() {
        List<Recipe> recipes = Arrays.asList(testRecipe, new Recipe());
        when(recipeRepository.findAll()).thenReturn(recipes);

        List<Recipe> result = recipeService.getAllRecipes();

        assertEquals(2, result.size());
        verify(recipeRepository, times(1)).findAll();
    }
    //getRecipesByTitle (No Results)
    @Test
    void testGetRecipesByTitle_NotFound() {
        when(recipeRepository.findRecipeByTitle("Strawberry Cake")).thenReturn(List.of());

        List<Recipe> recipes = recipeService.getRecipesByTitle("Strawberry Cake");

        assertTrue(recipes.isEmpty());
    }

    //getRecipesByCategory (Success)
    @Test
    void testGetRecipesByCategory_Success() {
        when(recipeRepository.findRecipeByCategory(RecipeCategory.Dessert)).thenReturn(List.of(testRecipe));

        List<Recipe> recipes = recipeService.getRecipesByCategory(RecipeCategory.Dessert);

        assertEquals(1, recipes.size());
        assertEquals(RecipeCategory.Dessert, recipes.get(0).getCategory());
    }

    //getRecipesByCategory (No Results)
    @Test
    void testGetRecipesByCategory_NotFound() {
        when(recipeRepository.findRecipeByCategory(RecipeCategory.Breakfast)).thenReturn(List.of());

        List<Recipe> recipes = recipeService.getRecipesByCategory(RecipeCategory.Breakfast);

        assertTrue(recipes.isEmpty());
    }
}
