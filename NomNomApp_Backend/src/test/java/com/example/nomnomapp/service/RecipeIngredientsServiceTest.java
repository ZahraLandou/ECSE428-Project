package com.example.nomnomapp.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.example.nomnomapp.model.Ingredient;
import com.example.nomnomapp.model.Recipe;
import com.example.nomnomapp.model.RecipeIngredients;
import com.example.nomnomapp.repository.IngredientRepository;
import com.example.nomnomapp.repository.RecipeIngredientsRepository;
import com.example.nomnomapp.repository.RecipeRepository;
import java.util.Optional;
import java.util.List;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)

public class RecipeIngredientsServiceTest {
    @Mock
    private RecipeIngredientsRepository recipeIngredientsRepo;

    @Mock
    private RecipeRepository recipeRepo;

    @Mock
    private IngredientRepository ingredientRepo;

    @InjectMocks
    private RecipeIngredientsService recipeIngredientsServ;

    private Recipe testRecipe;
    private Ingredient testIngredient;
    private RecipeIngredients testRecipeIngredient;
    @BeforeEach
    void setUp() {
        // Set up a dummy Recipe (assume setters are available)
        testRecipe = new Recipe();
        testRecipe.setRecipeID(1);
        testRecipe.setTitle("Test Recipe");
        testRecipe.setDescription("Test Recipe Description");
        testRecipe.setInstructions("Mix ingredients and bake");
        testRecipe.setLikes(10);
        testRecipe.setAverageRating(4.0);

        // Set up a dummy Ingredient
        testIngredient = new Ingredient("Sugar", "Sweetener");

        // Create a RecipeIngredients instance
        testRecipeIngredient = new RecipeIngredients(2.0, "cups", testRecipe, testIngredient);
    }

    // Test creation of a RecipeIngredient
    @Test
    void testCreateRecipeIngredient_Success() {
        when(recipeIngredientsRepo.save(testRecipeIngredient)).thenReturn(testRecipeIngredient);
        RecipeIngredients saved = recipeIngredientsServ.createRecipeIngredient(testRecipeIngredient);
        assertNotNull(saved);
        assertEquals(2.0, saved.getQuantity());
        assertEquals("cups", saved.getUnit());
        verify(recipeIngredientsRepo, times(1)).save(testRecipeIngredient);
    }
    // Test retrieval of a RecipeIngredient by a valid ID
    @Test
    void testGetRecipeIngredientById_Success() {
        int id = 1;
        when(recipeIngredientsRepo.findById(id)).thenReturn(Optional.of(testRecipeIngredient));
        RecipeIngredients found = recipeIngredientsServ.getRecipeIngredientById(id);
        assertNotNull(found);
        assertEquals(2.0, found.getQuantity());
        assertEquals("cups", found.getUnit());
    }
    // Test retrieval by an invalid ID (expecting an exception)
    @Test
    void testGetRecipeIngredientById_NotFound() {
        int invalidId = 99;
        when(recipeIngredientsRepo.findById(invalidId)).thenReturn(Optional.empty());
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            recipeIngredientsServ.getRecipeIngredientById(invalidId);
        });
        assertEquals("RecipeIngredient not found with id: 99", exception.getMessage());
    }
    @Test
    void testUpdateRecipeIngredient_Success() {


        testRecipeIngredient.setQuantity(3.5);
        testRecipeIngredient.setUnit("liters");
        when(recipeIngredientsRepo.save(testRecipeIngredient)).thenReturn(testRecipeIngredient);
        RecipeIngredients updated = recipeIngredientsServ.updateRecipeIngredient(testRecipeIngredient);
        assertNotNull(updated);
        assertEquals(3.5, updated.getQuantity());
        assertEquals("liters", updated.getUnit());
    }
    // Test deletion of an existing RecipeIngredient
    @Test
    void testDeleteRecipeIngredient_Success() {
        int id = 1;
        when(recipeIngredientsRepo.findById(anyInt())).thenReturn(Optional.of(testRecipeIngredient));
        doNothing().when(recipeIngredientsRepo).deleteById(id);
        assertDoesNotThrow(() -> recipeIngredientsServ.deleteRecipeIngredient(id));
        verify(recipeIngredientsRepo, times(1)).deleteById(id);
    }

    // Test deletion of a non-existent RecipeIngredient (expecting an exception)
    @Test
    void testDeleteRecipeIngredient_NotFound() {
        int invalidId = 99;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            recipeIngredientsServ.deleteRecipeIngredient(invalidId);
        });
        assertEquals("RecipeIngredient not found with id: 99", exception.getMessage());
        verify(recipeIngredientsRepo, never()).delete(any());
    }
}
