package com.example.nomnomapp.stepdefinitions;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

import com.example.nomnomapp.model.Ingredient;
import com.example.nomnomapp.model.Recipe;
import com.example.nomnomapp.model.*;
import com.example.nomnomapp.model.RecipeIngredients;
import com.example.nomnomapp.repository.IngredientRepository;
import com.example.nomnomapp.repository.RecipeRepository;
import com.example.nomnomapp.repository.UserRepository;
import com.example.nomnomapp.service.RecipeIngredientsService;
import io.cucumber.java.en.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import io.cucumber.java.After;
import io.cucumber.java.Before;


@SpringBootTest
public class RecipeIngredientsStepDefinitions {

    @Autowired
    private RecipeIngredientsService recipeIngredientsService;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private UserRepository UserRepository;

    @Autowired
    private CommonStepDefinitions commonSteps;


    private Recipe currentRecipe;
    private RecipeIngredients currentRecipeIngredient;
    private Exception exception;

    private final Map<String, Ingredient> ingredientDatabase = new HashMap<>(); // In-memory map to simulate ingredient
    private RecipeIngredients resultIngredient;


    // storage by name
    @Before
    public void setUp() {
        ingredientDatabase.clear();
        resultIngredient = null;
        recipeIngredientsService.deleteAllRecipeIngredients();
    }

    // method to clean up after each scenario
    @After
    public void cleanup() {
        ingredientDatabase.clear();
    }


    // Given a recipe with the given title exists.
    // Modified: if no recipe is found, create one.
    @Given("a recipe {string} exists")
    public void a_recipe_exists(String recipeTitle) {
        List<Recipe> recipes = recipeRepository.findRecipeByTitle(recipeTitle);
        if (recipes.isEmpty()) {
            // Create a default NomNomUser if one is not provided
            NomNomUser defaultUser = new NomNomUser("defaultUser", "default@example.com", "password123");
            UserRepository.save(defaultUser);

            // Create a new Recipe instance and set required fields.
            Recipe newRecipe = new Recipe();
            newRecipe.setTitle(recipeTitle);
            newRecipe.setDescription("Default description");
            newRecipe.setCreationDate(new java.sql.Date(new Date().getTime()));
            // *** The important part: set the required NomNomUser association ***
            newRecipe.setNomNomUser(defaultUser);
            // (Set any other required fields for Recipe here, if needed.)

            recipeRepository.save(newRecipe);
            recipes = recipeRepository.findRecipeByTitle(recipeTitle);
        }
        currentRecipe = recipes.get(0);
        assertFalse(recipes.isEmpty(), "Recipe '" + recipeTitle + "' should exist");
    }


    // Given the recipe does not already include the specified ingredient.
    @Given("the recipe {string} does not already include {string}")
    public void the_recipe_does_not_already_include(String recipeTitle, String ingredientName) {
        a_recipe_exists(recipeTitle);
        List<RecipeIngredients> riList = recipeIngredientsService.getRecipeIngredientsByRecipeId(currentRecipe.getRecipeID());
        boolean found = riList.stream()
                .anyMatch(ri -> ri.getIngredient().getName().equalsIgnoreCase(ingredientName));
        assertFalse(found, "Recipe '" + recipeTitle + "' already includes '" + ingredientName + "'");
    }

    // Given the recipe already includes the specified ingredient.
    @Given("the recipe {string} already includes {string}")
    @Transactional
    public void the_recipe_already_includes(String recipeTitle, String ingredientName) {
        // Ensure the recipe exists
        a_recipe_exists(recipeTitle);

        // Check if the ingredient is already associated with the recipe
        List<RecipeIngredients> riList = recipeIngredientsService.getRecipeIngredientsByRecipeId(currentRecipe.getRecipeID());
        boolean found = riList.stream()
                .anyMatch(ri -> ri.getIngredient().getName().equalsIgnoreCase(ingredientName));

        // If not, create the association so that the duplicate test makes sense.
        if (!found) {
            Optional<Ingredient> ingredientOpt = ingredientRepository.findIngredientByName(ingredientName);
            assertTrue(ingredientOpt.isPresent(), "Ingredient '" + ingredientName + "' should exist");
            Ingredient ingredient = ingredientOpt.get();
            // You can choose a default quantity/unit or adjust as needed.
            RecipeIngredients newRi = new RecipeIngredients(1.0, "defaultUnit", currentRecipe, ingredient);
            recipeIngredientsService.createRecipeIngredient(newRi);
            found = true;
        }

        assertTrue(found, "Recipe '" + recipeTitle + "' should already include '" + ingredientName + "'");
    }



    // When I add an ingredient to the recipe with a given quantity and unit.
    @When("I add {string} to the recipe {string} with quantity {string} and unit {string}")
    @Transactional
    public void i_add_to_the_recipe_with_quantity_and_unit(String ingredientName, String recipeTitle, String quantityStr, String unit) {
        a_recipe_exists(recipeTitle);
        double quantity = Double.parseDouble(quantityStr);
        Optional<Ingredient> ingredientOpt = ingredientRepository.findIngredientByName(ingredientName);
        assertTrue(ingredientOpt.isPresent(), "Ingredient '" + ingredientName + "' should exist");
        Ingredient ingredient = ingredientOpt.get();
        RecipeIngredients newRi = new RecipeIngredients(quantity, unit, currentRecipe, ingredient);
        try {
            currentRecipeIngredient = recipeIngredientsService.createRecipeIngredient(newRi);
        } catch (Exception e) {
            exception = e;
        }
    }

    @When("I try to add {string} to the recipe {string} with quantity {string} and unit {string}")
    @Transactional
    public void i_try_to_add_to_the_recipe_with_quantity_and_unit_error(String ingredientName, String recipeTitle, String quantityStr, String unit) {
        a_recipe_exists(recipeTitle);
        double quantity = Double.parseDouble(quantityStr);
        Optional<Ingredient> ingredientOpt = ingredientRepository.findIngredientByName(ingredientName);
        assertTrue(ingredientOpt.isPresent(), "Ingredient '" + ingredientName + "' should exist");
        Ingredient ingredient = ingredientOpt.get();
        RecipeIngredients newRi = new RecipeIngredients(quantity, unit, currentRecipe, ingredient);
        try {
            recipeIngredientsService.createRecipeIngredient(newRi);
        } catch (Exception e) {
            commonSteps.setException(e);
        }
    }

    @When("I try to add a recipe ingredient with name {string} to the recipe {string} with quantity {string} and unit {string}")
    @Transactional
    public void i_try_to_add_a_recipe_ingredient_with_name_to_the_recipe_with_quantity_and_unit(String ingredientName, String recipeTitle, String quantityStr, String unit) {
        // Ensure the recipe exists (this method will create it if needed)
        a_recipe_exists(recipeTitle);

        // Parse quantity; if empty or invalid, set it to 0 so validation fails.
        double quantity;
        try {
            quantity = Double.parseDouble(quantityStr);
        } catch(NumberFormatException e) {
            quantity = 0;
        }

        // Look for the Ingredient by name. For invalid scenarios, the ingredient name might be empty.
        Ingredient ingredient;
        if (ingredientName == null || ingredientName.trim().isEmpty()) {
            // Create a new transient ingredient with an empty name.
            ingredient = new Ingredient("", "defaultType");
        } else {
            Optional<Ingredient> ingredientOpt = ingredientRepository.findIngredientByName(ingredientName);
            if (ingredientOpt.isPresent()) {
                ingredient = ingredientOpt.get();
            } else {
                // If the ingredient doesn't exist, create a new transient one.
                ingredient = new Ingredient(ingredientName, "defaultType");
            }
        }

        // Create a new RecipeIngredients instance.
        RecipeIngredients newRi = new RecipeIngredients(quantity, unit, currentRecipe, ingredient);

        // Attempt to create the recipe ingredient; catch any exception for error assertions.
        try {
            recipeIngredientsService.createRecipeIngredient(newRi);
        } catch (Exception e) {
            commonSteps.setException(e);
        }
    }






    // Then the recipe should include the ingredient with the expected quantity and unit.
    @Then("the recipe should include {string} with quantity {string} and unit {string}")
    public void the_recipe_should_include_with_quantity_and_unit(String ingredientName, String quantityStr, String unit) {
        // Re-fetch the recipe (if needed) to ensure it's up-to-date.
        a_recipe_exists(currentRecipe.getTitle());
        List<RecipeIngredients> riList = recipeIngredientsService.getRecipeIngredientsByRecipeId(currentRecipe.getRecipeID());
        Optional<RecipeIngredients> optRi = riList.stream()
                .filter(ri -> ri.getIngredient().getName().equalsIgnoreCase(ingredientName))
                .findFirst();
        assertTrue(optRi.isPresent(), "Ingredient '" + ingredientName + "' not found in recipe '" + currentRecipe.getTitle() + "'");
        RecipeIngredients ri = optRi.get();
        double expectedQuantity = Double.parseDouble(quantityStr);
        assertEquals(expectedQuantity, ri.getQuantity(), "Quantity mismatch for ingredient '" + ingredientName + "'");
        assertEquals(unit, ri.getUnit(), "Unit mismatch for ingredient '" + ingredientName + "'");
    }

    /*@Then("I should see an error message {string}")
    public void i_should_see_an_error_message(String expectedMessage) {
        assertNotNull(commonSteps.getException(), "Expected an error but none occurred.");
        assertTrue(commonSteps.getException().getMessage().contains(expectedMessage), "Expected error message to contain: " + expectedMessage);
    }*/


}
