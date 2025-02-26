package com.example.nomnomapp.stepdefinitions;

import static org.junit.jupiter.api.Assertions.*;

import com.example.nomnomapp.model.Ingredient;
import com.example.nomnomapp.service.IngredientService;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

@SpringBootTest
public class IngredientStepDefinitions {

    @Autowired
    private IngredientService ingredientService;

    @Autowired
    private CommonStepDefinitions commonSteps;

    private final Map<String, Ingredient> ingredientDatabase = new HashMap<>(); // In-memory map to simulate ingredient
                                                                                // storage by name

    private Exception exception;
    private Ingredient resultIngredient;

    // Setup method to initialize necessary resources before each scenario
    @Before
    public void setUp() {
        ingredientDatabase.clear();
        resultIngredient = null;
        ingredientService.deleteAllIngredients();
    }

    // method to clean up after each scenario
    @After
    public void cleanup() {
        ingredientDatabase.clear();
    }

    // Given step to ensure ingredients exist in the system
    @Given("the following ingredients exist:")
    public void the_following_ingredients_exist(DataTable dataTable) {
        List<Map<String, String>> ingredients = dataTable.asMaps(String.class, String.class);
        for (Map<String, String> row : ingredients) {
            Ingredient ingredient = ingredientService.createIngredient(row.get("name"), row.get("type"));
            ingredientDatabase.put(row.get("name").toLowerCase(), ingredient);
        }
    }

    // Given step to ensure the ingredient does not exist in the system
    @Given("the ingredient {string} does not exist")
    public void the_ingredient_does_not_exist(String name) {
        assertFalse(ingredientDatabase.containsKey(name.toLowerCase()));
    }

    // Given step to ensure the ingredient exists in the system
    @Given("the ingredient {string} already exists")
    public void the_ingredient_already_exists(String name) {
        assertTrue(ingredientDatabase.containsKey(name.toLowerCase()));
    }

    // When step to add a new ingredient with a given name and type
    @When("I add an ingredient with name {string} and type {string}")
    public void i_add_an_ingredient_with_name_and_type(String name, String type) {
        try {
            resultIngredient = ingredientService.createIngredient(name, type); // create ingredient
            ingredientDatabase.put(name.toLowerCase(), resultIngredient); // add to ingredient list by name
        } catch (Exception e) {
            this.exception = e;
        }
    }

    // Then step to check if the ingredient was created successfully
    @Then("the ingredient should be created successfully")
    public void the_ingredient_should_be_created_successfully() {
        assertNotNull(resultIngredient, "The ingredient was not created successfully");
    }

    // Then step to verify that the ingredient exists in the list of ingredients
    @Then("I should see {string} in the list of ingredients")
    public void i_should_see_in_the_list_of_ingredients(String name) {
        List<Ingredient> allIngredients = ingredientService.getAllIngredients();
        boolean found = allIngredients.stream().anyMatch(ingredient -> ingredient.getName().equalsIgnoreCase(name));
        assertTrue(found, "Ingredient " + name + " should be in the list of ingredients");
    }

    // When step to attempt adding an ingredient (error flow), capturing any
    // exceptions
    @When("I try to add an ingredient with name {string} and type {string}")
    public void i_try_to_add_an_ingredient_with_name_and_type(String name, String type) {
        try {
            ingredientService.createIngredient(name, type);
        } catch (Exception e) {
            commonSteps.setException(e);
        }
    }

    // When step to request the list of all ingredients
    @When("I request the list of all ingredients")
    public void i_request_the_list_of_all_ingredients() {
        try {
            resultIngredient = null;
            List<Ingredient> allIngredients = ingredientService.getAllIngredients(); // // fetch all ingredients
            this.resultIngredient = allIngredients.size() > 0 ? allIngredients.get(0) : null;
        } catch (Exception e) {
            this.exception = e;
        }
    }

    // Then step to verify the ingredient details (name and type)
    @Then("I should see an ingredient with name {string} and type {string}")
    public void i_should_see_an_ingredient_with_name_and_type(String name, String type) {
        List<Ingredient> allIngredients = ingredientService.getAllIngredients();

        boolean found = allIngredients.stream()
                .anyMatch(ingredient -> ingredient.getName().equalsIgnoreCase(name) &&
                        ingredient.getType().equalsIgnoreCase(type));

        assertTrue(found, "Ingredient with name '" + name + "' and type '" + type + "' not found in the list");
    }

    // When step to search for an ingredient by name
    @When("I search for {string}")
    public void i_search_for(String name) {
        try {
            resultIngredient = ingredientService.getIngredientByName(name);
        } catch (Exception e) {
            this.exception = e;
        }
    }

    // Then step to verify the ingredient details (name and type) after searching
    @Then("I should see the ingredient details with name {string} and type {string}")
    public void i_should_see_the_ingredient_details_with_name_and_type(String name, String type) {
        assertNotNull(resultIngredient, "Ingredient not found");
        assertEquals(name, resultIngredient.getName(), "Ingredient name does not match");
        assertEquals(type, resultIngredient.getType(), "Ingredient type does not match");
    }

    // When step to update an ingredient's name and type by ID
    @When("I update the ingredient with name {string} to have name {string} and type {string}")
    public void i_update_the_ingredient_with_name_to_have_name_and_type(String oldName, String newName,
            String newType) {
        try {

            Ingredient updatedIngredient = ingredientService.updateIngredientByName(oldName, newName, newType);
            resultIngredient = updatedIngredient; // store the updated ingredient
        } catch (Exception e) {
            this.exception = e;
        }
    }

    // Then step to verify that the ingredient was successfully updated
    @Then("the ingredient should be updated successfully")
    public void the_ingredient_should_be_updated_successfully() {
        assertNotNull(resultIngredient, "The ingredient was not updated successfully");
    }

    // Then step to check if the updated ingredient has the expected name and type
    @Then("the updated ingredient should have name {string} and type {string}")
    public void the_updated_ingredient_should_have_name_and_type(String expectedName, String expectedType) {
        assertEquals(expectedName, resultIngredient.getName(), "Updated ingredient name does not match");
        assertEquals(expectedType, resultIngredient.getType(), "Updated ingredient type does not match");
    }

    // When step to try updating an ingredient(error flow)
    @When("I try to update the ingredient with name {string} to have name {string} and type {string}")
    public void i_try_to_update_the_ingredient_with_name_to_have_name_and_type(String oldName, String newName,
            String newType) {
        try {
            ingredientService.updateIngredientByName(oldName, newName, newType);
        } catch (Exception e) {
            commonSteps.setException(e);
        }
    }

    // When step to try searching for an ingredient(error flow)
    @When("I try to search for {string}")
    public void i_try_to_search_for_name(String name) {
        try {
            resultIngredient = ingredientService.getIngredientByName(name);
        } catch (Exception e) {
            commonSteps.setException(e);
        }
    }
}
