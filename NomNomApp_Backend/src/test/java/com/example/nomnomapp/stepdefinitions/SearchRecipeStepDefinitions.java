package com.example.nomnomapp.stepdefinitions;

import static org.junit.jupiter.api.Assertions.*;

import com.example.nomnomapp.model.Recipe;
import com.example.nomnomapp.model.RecipeIngredients;
import com.example.nomnomapp.model.Ingredient;
import com.example.nomnomapp.model.NomNomUser;
import com.example.nomnomapp.service.RecipeService;
import com.example.nomnomapp.service.UserService;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

@SpringBootTest
public class SearchRecipeStepDefinitions {

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private UserService userService;

    @Autowired
    private CommonStepDefinitions commonSteps;

    private final Map<String, Recipe> recipeDatabase = new HashMap<>(); // Simulated recipe storage
    private Exception exception;

    @Before
    public void setUp() {
        recipeDatabase.clear();
        exception = null;
    }

    @Given("the following recipes exist in the system:")
    public void the_following_recipes_exist_in_the_system(DataTable dataTable) {
        List<Map<String, String>> recipes = dataTable.asMaps(String.class, String.class);
        for (Map<String, String> recipeData : recipes) {
            Recipe recipe = new Recipe();
            recipe.setTitle(recipeData.get("recipeName"));
            recipeDatabase.put(recipe.getTitle(), recipe);
        }
    }

    // Normal flow
    @When("I enter a valid recipe with title \"{string}\" into the search bar")
    public void i_enter_a_valid_recipe_with_title_into_the_search_bar(String recipeName) {
        searchResults = recipeService.findRecipesByName(recipeName);
    }

    @Then("I should see a list of recipes that match the specified name")
    public void i_should_see_a_list_of_recipes_that_match_the_specified_name() {
        assertFalse(searchResults.isEmpty(), "Expected recipes to be found, but none were returned.");
    }

    // Alternate flow
    @When("I enter a valid recipe with title \"{string}\" into the search bar that does not match any existing recipes")
    public void i_enter_a_valid_recipe_with_title_into_the_search_bar_that_does_not_match_any_existing_recipes(String recipeName) {
        searchResults = recipeService.findRecipesByName(recipeName);
        if (searchResults.isEmpty()) {
            String errorMessage = "Recipe does not exist";
        }
    }

    // Error flow
    @When("I enter an invalid recipe with title \"{string}\" into the search page")
    public void i_enter_an_invalid_recipe_with_title_into_the_search_page(String recipeName) {
        if (!recipeName.matches("^[a-zA-Z ]+$")) {

            //TODO commonSteps.setException(e);
        } else {
            searchResults = recipeService.findRecipesByName(recipeName);
        }
    }

    @After
    public void cleanUp() {
        recipeDatabase.clear();
    }

}
