package com.example.nomnomapp.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.nomnomapp.model.Ingredient;
import com.example.nomnomapp.model.Recipe;
import com.example.nomnomapp.model.RecipeIngredients;

@SpringBootTest
public class RecipeIngredientsRepositoryTests {

    @Autowired
    private RecipeIngredientsRepository recipeIngredientsRepository;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private IngredientRepository ingredientRepository;

    @AfterEach
    public void clearDatabase(){
        recipeIngredientsRepository.deleteAll();
        recipeRepository.deleteAll();
        ingredientRepository.deleteAll();
    }

    @Test
    public void testPersistAndLoadRecipeIngredient(){
        // Create and save a test Ingredient
        Ingredient tomato = new Ingredient("tomato", "vegetable");
        tomato = ingredientRepository.save(tomato);

        // Create and save a test Recipe
        Recipe recipe = new Recipe();
        recipe.setTitle("Tomato Soup");
        recipe.setDescription("A delicious tomato soup");
        recipe.setInstructions("Boil tomatoes, blend, and serve hot");
        recipe.setLikes(0);
        recipe.setAverageRating(0.0);
        recipe = recipeRepository.save(recipe);

        // Create and save a RecipeIngredients record
        RecipeIngredients recipeIngredient = new RecipeIngredients(2.0, "cups", recipe, tomato);
        recipeIngredient = recipeIngredientsRepository.save(recipeIngredient);

        // Retrieve the RecipeIngredients record by its ID
        RecipeIngredients saved = recipeIngredientsRepository.findById(recipeIngredient.getRecipeIngredientID())
                .orElse(null);
        assertNotNull(saved);
        assertEquals(2.0, saved.getQuantity());
        assertEquals("cups", saved.getUnit());
        assertEquals(recipe.getRecipeID(), saved.getRecipe().getRecipeID());
        assertEquals(tomato.getName(), saved.getIngredient().getName());

        // Retrieve RecipeIngredients by Recipe ID using the custom finder method
        List<RecipeIngredients> byRecipe = recipeIngredientsRepository.findIngredientsByRecipeRecipeId(recipe.getRecipeID());
        assertNotNull(byRecipe);
        assertEquals(1, byRecipe.size());

        // Retrieve RecipeIngredients by Ingredient name using the custom finder method
        List<RecipeIngredients> byIngredient = recipeIngredientsRepository.findByIngredientName("tomato");
        assertNotNull(byIngredient);
        assertEquals(1, byIngredient.size());
    }
}

