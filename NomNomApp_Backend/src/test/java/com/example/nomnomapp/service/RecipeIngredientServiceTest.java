package com.example.nomnomapp.service;

import com.example.nomnomapp.model.Ingredient;
import com.example.nomnomapp.model.Recipe;
import com.example.nomnomapp.model.RecipeIngredients;
import com.example.nomnomapp.repository.IngredientRepository;
import com.example.nomnomapp.repository.RecipeIngredientsRepository;
import com.example.nomnomapp.repository.RecipeRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
public class RecipeIngredientsServiceTest {

    @Autowired
    private RecipeIngredientsService recipeIngredientsService;

    @Autowired
    private RecipeIngredientsRepository recipeIngredientsRepository;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private IngredientRepository ingredientRepository;

    private Recipe testRecipe;
    private Ingredient testIngredient;

    @BeforeAll
    public void setup() {
        // Clear existing data to have a fresh test context
        recipeIngredientsRepository.deleteAll();
        recipeRepository.deleteAll();
        ingredientRepository.deleteAll();

        // Create and save a test Recipe
        testRecipe = new Recipe();
        testRecipe.setTitle("Test Recipe");
        testRecipe.setDescription("Test Description");
        testRecipe.setInstructions("Test Instructions");
        testRecipe.setLikes(0);
        testRecipe.setAverageRating(0.0);
        testRecipe = recipeRepository.save(testRecipe);

        // Create and save a test Ingredient
        testIngredient = new Ingredient();
        testIngredient.setName("Salt");
        testIngredient.setType("Spice");
        testIngredient = ingredientRepository.save(testIngredient);
    }

    @BeforeEach
    public void clearRecipeIngredients() {
        recipeIngredientsRepository.deleteAll();
    }

    @Test
    public void testCreateAndGetRecipeIngredient() {
        // Create a new RecipeIngredients instance
        RecipeIngredients ingredient = new RecipeIngredients(2.0, "tsp", testRecipe, testIngredient);
        RecipeIngredients created = recipeIngredientsService.createRecipeIngredient(ingredient);
        assertNotNull(created);
        int id = created.getRecipeIngredientID();

        // Retrieve the created entity using the service
        RecipeIngredients found = recipeIngredientsService.getRecipeIngredientById(id);
        assertNotNull(found);
        assertEquals(2.0, found.getQuantity());
        assertEquals("tsp", found.getUnit());
        assertEquals(testRecipe.getRecipeID(), found.getRecipe().getRecipeID());
        assertEquals(testIngredient.getName(), found.getIngredient().getName());
    }

    @Test
    public void testGetAllRecipeIngredients() {
        // Create two RecipeIngredients entries
        RecipeIngredients ri1 = new RecipeIngredients(1.0, "cup", testRecipe, testIngredient);
        RecipeIngredients ri2 = new RecipeIngredients(0.5, "tbsp", testRecipe, testIngredient);
        recipeIngredientsService.createRecipeIngredient(ri1);
        recipeIngredientsService.createRecipeIngredient(ri2);

        Iterable<RecipeIngredients> all = recipeIngredientsService.getAllRecipeIngredients();
        int count = 0;
        for (RecipeIngredients r : all) {
            count++;
        }
        assertEquals(2, count);
    }

    @Test
    public void testGetRecipeIngredientsByRecipeId() {
        // Create a RecipeIngredients for the testRecipe
        RecipeIngredients ri1 = new RecipeIngredients(1.5, "grams", testRecipe, testIngredient);
        recipeIngredientsService.createRecipeIngredient(ri1);

        // Create another RecipeIngredients for a different recipe
        Recipe anotherRecipe = new Recipe();
        anotherRecipe.setTitle("Another Recipe");
        anotherRecipe.setDescription("Another Description");
        anotherRecipe.setInstructions("Another Instructions");
        anotherRecipe.setLikes(0);
        anotherRecipe.setAverageRating(0.0);
        anotherRecipe = recipeRepository.save(anotherRecipe);
        RecipeIngredients ri2 = new RecipeIngredients(3.0, "ml", anotherRecipe, testIngredient);
        recipeIngredientsService.createRecipeIngredient(ri2);

        // Retrieve only the RecipeIngredients for testRecipe
        List<RecipeIngredients> list = recipeIngredientsService.getRecipeIngredientsByRecipeId(testRecipe.getRecipeID());
        assertEquals(1, list.size());
        RecipeIngredients found = list.get(0);
        assertEquals(1.5, found.getQuantity());
    }

    @Test
    public void testUpdateRecipeIngredient() {
        // Create a RecipeIngredient
        RecipeIngredients ingredient = new RecipeIngredients(1.0, "cup", testRecipe, testIngredient);
        RecipeIngredients created = recipeIngredientsService.createRecipeIngredient(ingredient);
        int id = created.getRecipeIngredientID();

        // Update its quantity and unit
        created.setQuantity(2.0);
        created.setUnit("cups");
        RecipeIngredients updated = recipeIngredientsService.updateRecipeIngredient(created);
        assertNotNull(updated);
        assertEquals(2.0, updated.getQuantity());
        assertEquals("cups", updated.getUnit());

        // Verify via retrieval
        RecipeIngredients found = recipeIngredientsService.getRecipeIngredientById(id);
        assertEquals(2.0, found.getQuantity());
        assertEquals("cups", found.getUnit());
    }

    @Test
    public void testDeleteRecipeIngredient() {
        // Create a RecipeIngredient
        RecipeIngredients ingredient = new RecipeIngredients(0.5, "tsp", testRecipe, testIngredient);
        RecipeIngredients created = recipeIngredientsService.createRecipeIngredient(ingredient);
        int id = created.getRecipeIngredientID();

        // Delete the record
        recipeIngredientsService.deleteRecipeIngredient(id);

        // Ensure it no longer exists (expecting an exception)
        Exception exception = assertThrows(RuntimeException.class, () -> {
            recipeIngredientsService.getRecipeIngredientById(id);
        });
        String expectedMessage = "RecipeIngredient not found with id: " + id;
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
}
