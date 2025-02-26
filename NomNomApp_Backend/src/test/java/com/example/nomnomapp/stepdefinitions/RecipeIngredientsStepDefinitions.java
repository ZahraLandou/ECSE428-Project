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
import java.util.Map;
import io.cucumber.datatable.DataTable;

@Transactional
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

    private RecipeIngredients foundRecipeIngredient;

    private Recipe currentRecipe;
    private RecipeIngredients currentRecipeIngredient;
    private Exception exception;

    private final Map<String, Ingredient> ingredientDatabase = new HashMap<>(); // In-memory map to simulate ingredient
    private RecipeIngredients resultIngredient;

    private List<RecipeIngredients> allRecipeIngredients;

    // storage by name
    @Before
    public void setUp() {
        ingredientDatabase.clear();
        resultIngredient = null;
        recipeIngredientsService.deleteAllRecipeIngredients();
        recipeRepository.deleteAll();
    }


    // method to clean up after each scenario
    @After
    public void cleanup() {
        ingredientDatabase.clear();
        recipeRepository.deleteAll();
    }


    // Given a recipe with the given title exists.
    // Modified: if no recipe is found, create one.
    @Given("a recipe {string} exists")
    public void a_recipe_exists(String recipeTitle) {
        List<Recipe> recipes = recipeRepository.findRecipeByTitle(recipeTitle);

        if (recipes.isEmpty()) {
            // Create user if needed
            NomNomUser defaultUser = UserRepository.findByUsername("defaultUser")
                    .orElseGet(() -> UserRepository.save(
                            new NomNomUser("defaultUser", "default@example.com", "password123")));

            // Create the new Recipe
            Recipe newRecipe = new Recipe();
            newRecipe.setTitle(recipeTitle);
            newRecipe.setNomNomUser(defaultUser);
            newRecipe.setDescription("Default description");
            newRecipe.setCreationDate(new java.sql.Date(System.currentTimeMillis()));

            recipeRepository.save(newRecipe);

            // Just set currentRecipe to that new object
            currentRecipe = newRecipe;
        } else {
            // If it already existed, just use the first one
            currentRecipe = recipes.get(0);
        }

        // Confirm we have a valid recipe
        assertNotNull(currentRecipe, "Recipe '" + recipeTitle + "' not created/found as expected.");
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
        a_recipe_exists(recipeTitle);

        // Check if the ingredient is already in the recipe
        List<RecipeIngredients> riList = recipeIngredientsService
                .getRecipeIngredientsByRecipeId(currentRecipe.getRecipeID());
        boolean found = riList.stream()
                .anyMatch(ri -> ri.getIngredient().getName().equalsIgnoreCase(ingredientName));

        if (!found) {
            // Try to find the ingredient in the DB
            Optional<Ingredient> ingredientOpt = ingredientRepository.findIngredientByName(ingredientName);
            // If it does not exist, create it here
            Ingredient ingredient = ingredientOpt.orElseGet(() -> {
                Ingredient newIng = new Ingredient(ingredientName, "defaultType");
                return ingredientRepository.save(newIng);
            });

            // Now associate it with the recipe
            RecipeIngredients newRi = new RecipeIngredients(1.0, "defaultUnit", currentRecipe, ingredient);
            recipeIngredientsService.createRecipeIngredient(newRi);
        }

        // Double-check it's in the recipe now
        List<RecipeIngredients> updatedRiList = recipeIngredientsService
                .getRecipeIngredientsByRecipeId(currentRecipe.getRecipeID());
        boolean updatedFound = updatedRiList.stream()
                .anyMatch(ri -> ri.getIngredient().getName().equalsIgnoreCase(ingredientName));
        assertTrue(updatedFound,
                String.format("Recipe '%s' should already include '%s'", recipeTitle, ingredientName));
    }

    @Transactional
    @Given("the following recipe ingredients exist:")
    public void the_following_recipe_ingredients_exist(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        // Possibly create or fetch a test recipe to attach them to:
        Recipe testRecipe = fetchOrCreateTestRecipe("Test Recipe");

        for (Map<String, String> row : rows) {
            String name = row.get("name");
            String quantityStr = row.get("quantity");
            String unit = row.get("unit");

            // 1) Find or create the base Ingredient
            Optional<Ingredient> optIng = ingredientRepository.findIngredientByName(name);
            Ingredient ingredient = optIng.orElseGet(() -> {
                Ingredient newIng = new Ingredient(name, "unknown");
                return ingredientRepository.save(newIng);
            });

            // 2) Create the new RecipeIngredients
            RecipeIngredients ri = new RecipeIngredients();
            ri.setIngredient(ingredient);
            ri.setQuantity(Double.parseDouble(quantityStr));
            ri.setUnit(unit);

            // 3) *** The key line ***
            // Because your schema says 'recipe' cannot be null, attach a *valid* Recipe
            ri.setRecipe(testRecipe);

            // 4) Persist
            recipeIngredientsService.createRecipeIngredient(ri);
        }
    }

    private Recipe fetchOrCreateTestRecipe(String title) {
        List<Recipe> existingRecipes = recipeRepository.findRecipeByTitle(title);
        if (!existingRecipes.isEmpty()) {
            return existingRecipes.get(0);
        }

        // Ensure there's a default user
        NomNomUser defaultUser = UserRepository.findByUsername("defaultUser")
                .orElseGet(() -> {
                    NomNomUser newUser = new NomNomUser("defaultUser", "default@example.com", "password123");
                    return UserRepository.save(newUser);
                });

        // Create the new recipe
        Recipe newRecipe = new Recipe();
        newRecipe.setTitle(title);
        newRecipe.setNomNomUser(defaultUser);

        // The critical lines: set all NOT NULL fields
        newRecipe.setCreationDate(new java.sql.Date(System.currentTimeMillis()));
        newRecipe.setDescription("Some default description");    // If NOT NULL
        newRecipe.setInstructions("Some default instructions");  // If NOT NULL
        // newRecipe.setCategory("Dinner");                     // If NOT NULL, etc.

        // Save
        recipeRepository.save(newRecipe);
        return newRecipe;
    }




    @When("I search for the recipe ingredient {string}")
    public void i_search_for_the_recipe_ingredient(String ingredientName) {
        // We'll assume recipe ingredients are stored in a manner that each "ingredientName" is unique,
        // or you'll adapt your logic to handle duplicates.

        // For example, we might have a service method that returns
        //   recipeIngredientsService.findByIngredientName(ingredientName);
        // If that doesn't exist, you'd do a more custom search:

        List<RecipeIngredients> allIngredients = recipeIngredientsService.getAllRecipeIngredients();

        Optional<RecipeIngredients> matchOpt = allIngredients.stream()
                .filter(ri -> ri.getIngredient().getName().equalsIgnoreCase(ingredientName))
                .findFirst();

        foundRecipeIngredient = matchOpt.orElse(null);
    }

    @Then("I should see the recipe ingredient details with name {string}, quantity {string}, and unit {string}")
    public void i_should_see_the_recipe_ingredient_details_with_name_quantity_and_unit(
            String expectedName, String expectedQuantityStr, String expectedUnit) {

        assertNotNull(foundRecipeIngredient, "No recipe ingredient was found by the previous search.");

        // Compare the name
        String actualName = foundRecipeIngredient.getIngredient().getName();
        assertEquals(expectedName, actualName, "Ingredient name mismatch.");

        // Compare the quantity
        double expectedQuantity = Double.parseDouble(expectedQuantityStr);
        assertEquals(expectedQuantity, foundRecipeIngredient.getQuantity(), "Ingredient quantity mismatch.");

        // Compare the unit
        assertEquals(expectedUnit, foundRecipeIngredient.getUnit(), "Ingredient unit mismatch.");
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

    @When("I remove {string} from the recipe {string}")
    @Transactional
    public void i_remove_from_the_recipe(String ingredientName, String recipeTitle) {
        // Ensure the recipe exists (this sets currentRecipe)
        a_recipe_exists(recipeTitle);

        // Retrieve all RecipeIngredients for the current recipe
        List<RecipeIngredients> riList = recipeIngredientsService
                .getRecipeIngredientsByRecipeId(currentRecipe.getRecipeID());

        // Find the RecipeIngredients entry that matches the desired ingredient name
        Optional<RecipeIngredients> toRemoveOpt = riList.stream()
                .filter(ri -> ri.getIngredient().getName().equalsIgnoreCase(ingredientName))
                .findFirst();

        // If found, remove it; otherwise decide if this should fail
        if (toRemoveOpt.isPresent()) {
            // Call deleteRecipeIngredient from your service
            recipeIngredientsService.deleteRecipeIngredient(toRemoveOpt.get().getRecipeIngredientID());
        } else {
            // If the ingredient isn't found, you can decide whether to throw an error or do nothing
            throw new IllegalArgumentException(
                    "Ingredient '" + ingredientName + "' not found in recipe '" + recipeTitle + "'.");
        }
    }

    @Then("the recipe should not include {string}")
    public void the_recipe_should_not_include(String ingredientName) {
        // Retrieve all RecipeIngredients again to verify the ingredient is gone
        List<RecipeIngredients> riList = recipeIngredientsService
                .getRecipeIngredientsByRecipeId(currentRecipe.getRecipeID());

        // Check if any recipe ingredient still matches the one that was removed
        boolean stillPresent = riList.stream()
                .anyMatch(ri -> ri.getIngredient().getName().equalsIgnoreCase(ingredientName));

        assertFalse(stillPresent,
                "Recipe '" + currentRecipe.getTitle()
                        + "' still includes ingredient '" + ingredientName + "' after removal.");
    }

    @Given("the recipe {string} does not include {string}")
    public void the_recipe_does_not_include(String recipeTitle, String ingredientName) {
        // Ensure the recipe exists; this method sets currentRecipe
        a_recipe_exists(recipeTitle);

        // Retrieve the list of RecipeIngredients for this recipe
        List<RecipeIngredients> riList = recipeIngredientsService
                .getRecipeIngredientsByRecipeId(currentRecipe.getRecipeID());

        // Check if any entry matches the specified ingredient
        boolean found = riList.stream()
                .anyMatch(ri -> ri.getIngredient().getName().equalsIgnoreCase(ingredientName));

        // Fail if it's found, since this step expects the recipe not to have that ingredient
        assertFalse(found,
                String.format("Recipe '%s' already includes '%s'.", recipeTitle, ingredientName));
    }

    @When("I try to remove {string} from the recipe {string}")
    public void i_try_to_remove_from_the_recipe(String ingredientName, String recipeTitle) {
        // Ensure the recipe exists; this method sets currentRecipe
        a_recipe_exists(recipeTitle);

        // Get the relevant recipe-ingredient association if it exists
        List<RecipeIngredients> riList = recipeIngredientsService
                .getRecipeIngredientsByRecipeId(currentRecipe.getRecipeID());

        Optional<RecipeIngredients> toRemoveOpt = riList.stream()
                .filter(ri -> ri.getIngredient().getName().equalsIgnoreCase(ingredientName))
                .findFirst();

        // Try removing it. If it doesn't exist, you can decide whether to throw or do nothing.
        try {
            if (toRemoveOpt.isPresent()) {
                recipeIngredientsService.deleteRecipeIngredient(toRemoveOpt.get().getRecipeIngredientID());
            } else {
                // If you want to simulate a failure, throw an exception and catch it
                throw new IllegalArgumentException(
                        String.format("Ingredient '%s' not found in recipe '%s'.", ingredientName, recipeTitle));
            }
        } catch (Exception e) {
            // If you're capturing exceptions for later verification in your tests:
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

    @When("I request a list of all recipe ingredients")
    public void i_request_a_list_of_all_recipe_ingredients() {
        // Fetch all recipe ingredients from the database/service
        allRecipeIngredients = recipeIngredientsService.getAllRecipeIngredients();
        // In a real test, you might do further filtering, or store them differently.
    }
    @Then("I should see a recipe ingredient with name {string}, quantity {string}, and unit {string}")
    public void i_should_see_a_recipe_ingredient_with_name_quantity_and_unit(
            String expectedName, String expectedQuantityStr, String expectedUnit) {

        assertNotNull(allRecipeIngredients, "No recipe ingredients were retrieved. Did you call the When step?");
        double expectedQuantity = Double.parseDouble(expectedQuantityStr);

        // Check if the list contains an ingredient matching all three properties
        boolean matchFound = allRecipeIngredients.stream().anyMatch(ri ->
                ri.getIngredient().getName().equalsIgnoreCase(expectedName)
                        && Double.compare(ri.getQuantity(), expectedQuantity) == 0
                        && ri.getUnit().equalsIgnoreCase(expectedUnit)
        );

        assertTrue(matchFound,
                String.format(
                        "Expected an ingredient with name='%s', quantity='%s', unit='%s' but none was found.",
                        expectedName, expectedQuantityStr, expectedUnit
                )
        );
    }

    @Given("the recipe ingredient {string} does not exist")
    public void the_recipe_ingredient_does_not_exist(String ingredientName) {
        // 1) Find the ingredient by name, if it exists
        Optional<Ingredient> existing = ingredientRepository.findIngredientByName(ingredientName);
        // 2) If found, remove it to ensure it no longer exists
        existing.ifPresent(ingredientRepository::delete);
        // 3) Verify it’s really gone
        Optional<Ingredient> check = ingredientRepository.findIngredientByName(ingredientName);
        assertTrue(check.isEmpty(),
                String.format("Ingredient '%s' still exists in the database but should not.", ingredientName));
    }

    @When("I try to search for the recipe ingredient {string}")
    public void i_try_to_search_for_the_recipe_ingredient(String ingredientName) {
        try {
            Optional<Ingredient> found = ingredientRepository.findIngredientByName(ingredientName);

            // If it's not found, throw an exception:
            if (found.isEmpty()) {
                throw new NoSuchElementException("Recipe ingredient with name '" + ingredientName + "' not found");
            }

            // Otherwise, you might store the found ingredient somewhere:
            // commonSteps.setFoundIngredient(found.get());

        } catch (Exception e) {
            // Capture the exception so your @Then step can check it
            commonSteps.setException(e);
        }
    }
    @When("I update the recipe ingredient with name {string} in recipe {string} to have quantity {string} and unit {string}")
    @Transactional
    public void i_update_the_recipe_ingredient_in_recipe(String ingredientName,
                                                         String recipeTitle,
                                                         String newQuantityStr,
                                                         String newUnit) {
        try {
            // 1) Fetch the recipe by title.
            Recipe recipe = fetchOrCreateTestRecipe(recipeTitle);

            // 2) Look up the existing RecipeIngredients entry.
            Optional<RecipeIngredients> existingRi = recipeIngredientsService
                    .getRecipeIngredientsByRecipeId(recipe.getRecipeID())
                    .stream()
                    .filter(ri -> ri.getIngredient().getName().equalsIgnoreCase(ingredientName))
                    .findFirst();

            if (existingRi.isEmpty()) {
                // If the ingredient doesn't exist in the recipe, throw an error (or handle as you wish).
                throw new NoSuchElementException(
                        "Recipe ingredient with name '" + ingredientName + "' not found"
                );
            }

            // 3) Convert the quantity input
            double quantity = Double.parseDouble(newQuantityStr);

            // 4) Update fields on the existing recipe ingredient
            RecipeIngredients riToUpdate = existingRi.get();
            riToUpdate.setQuantity(quantity);
            riToUpdate.setUnit(newUnit);

            if (newUnit != null && !newUnit.isBlank()) {
                riToUpdate.setUnit(newUnit);
            }

            // 5) Save changes via your service
            //    Importantly, capture (return) the updated object in currentRecipeIngredient
            currentRecipeIngredient = recipeIngredientsService.updateRecipeIngredient(riToUpdate);

        } catch (Exception e) {
            // If this was a scenario expecting no error, you could skip capturing,
            // but typically we do so if something might go wrong.
            commonSteps.setException(e);
        }
    }

    @When("I try to update the recipe ingredient with name {string} in recipe {string} to have quantity {string} and unit {string}")
    @Transactional
    public void i_try_to_update_the_recipe_ingredient_in_recipe(String ingredientName,
                                                                String recipeTitle,
                                                                String newQuantityStr,
                                                                String newUnit) {
        try {
            // The same logic as above, but we always wrap in a try-catch to capture exceptions
            i_update_the_recipe_ingredient_in_recipe(ingredientName, recipeTitle, newQuantityStr, newUnit);
        } catch (Exception e) {
            // Since i_update_the_recipe_ingredient_in_recipe might also throw, let's capture that
            commonSteps.setException(e);
        }
    }

    @Then("the recipe ingredient should be updated successfully")
    public void the_recipe_ingredient_should_be_updated_successfully() {
        // 1) Confirm no exception occurred
        assertNull(commonSteps.getException(),
                "Expected no error, but found: " + commonSteps.getException());

        // 2) Confirm we actually set currentRecipeIngredient
        assertNotNull(currentRecipeIngredient,
                "No updated recipe ingredient was stored in the test context.");

        // 3) Optionally re-fetch from DB to ensure changes persisted.
        RecipeIngredients reloadedRi = recipeIngredientsService
                .getRecipeIngredientById(currentRecipeIngredient.getRecipeIngredientID());
        assertNotNull(reloadedRi,
                "Updated recipe ingredient not found in the database after save.");

        // 4) If you know exactly what the quantity and unit *should* be,
        //    you can also assert those fields here:
        //      assertEquals(2.0, reloadedRi.getQuantity());
        //      assertEquals("cups", reloadedRi.getUnit());
    }
    @Then("the updated recipe ingredient should have name {string}, quantity {string}, and unit {string}")
    public void the_updated_recipe_ingredient_should_have_name_quantity_and_unit(
            String expectedName, String expectedQuantityStr, String expectedUnit) {

        // 1) Confirm no error occurred
        assertNull(commonSteps.getException(),
                "Expected no error, but found: " + commonSteps.getException());

        // 2) Ensure we actually have an updated recipe ingredient
        assertNotNull(currentRecipeIngredient,
                "No updated recipe ingredient was stored in the test context.");

        // 3) Re-fetch from DB (optional but recommended to confirm the changes persisted)
        RecipeIngredients reloadedRi = recipeIngredientsService
                .getRecipeIngredientById(currentRecipeIngredient.getRecipeIngredientID());

        assertNotNull(reloadedRi,
                "Updated recipe ingredient not found in the database after save.");

        // 4) Parse the quantity
        double expectedQuantity = Double.parseDouble(expectedQuantityStr);

        // 5) Check each field
        //    - The "name" is in the Ingredient entity
        //    - "quantity" and "unit" are in the RecipeIngredients entity
        assertEquals(expectedName, reloadedRi.getIngredient().getName(),
                "Ingredient name mismatch after update.");
        assertEquals(expectedQuantity, reloadedRi.getQuantity(),
                "Quantity mismatch after update.");
        assertEquals(expectedUnit, reloadedRi.getUnit(),
                "Unit mismatch after update.");
    }





}
