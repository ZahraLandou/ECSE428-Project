package com.example.nomnomapp.stepdefinitions;

import static org.junit.jupiter.api.Assertions.*;

import com.example.nomnomapp.model.NomNomUser;
import com.example.nomnomapp.model.Recipe;
import com.example.nomnomapp.repository.UserRepository;
import com.example.nomnomapp.service.RecipeService;
import com.example.nomnomapp.service.UserService;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

@Transactional
public class SearchRecipeStepDefinitions {

    @Autowired
    private RecipeService recipeService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private CommonStepDefinitions commonStepDefinitions;

    private final Map<String, Recipe> recipeDatabase = new HashMap<>(); // Simulated recipe storage

    private List<Recipe> searchResults;
    private String error;

    @Before
    public void setUp() {
        recipeDatabase.clear();
        recipeService.deleteAllRecipes();
        userService.deleteAllUsers();
    }

    @After
    public void cleanUp() {
        recipeDatabase.clear();
        error = null;
    }

    @Given("the following users exist in the system:")
    public void the_following_users_exist(DataTable dataTable) {
        List<Map<String, String>> users = dataTable.asMaps(String.class,String.class);
        for (Map<String, String> row : users) {
            NomNomUser user = userService.createUser(row.get("username"), row.get("emailAddress"), row.get("password"));
            userRepo.save(user);
        }
    }

    @Given("the following recipes exist in the system:")
    public void the_following_recipes_exist_in_the_system(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps();
        for (Map<String, String> row : rows) {
            Recipe recipe = new Recipe();
            recipe.setTitle(row.get("recipeName"));
            recipe.setInstructions(row.get("instructions"));
            recipe.setDescription(row.get("description"));

            NomNomUser user = userService.getNomNomUserByName(row.get("recipeNomNomUser"));
            recipe.setNomNomUser(user);

            recipe.setCategory(Recipe.RecipeCategory.valueOf(row.get("category")));
            recipe.setLikes(Integer.parseInt(row.get("likes")));
            recipe.setAverageRating(Double.parseDouble(row.get("averageRating")));
            recipe.setCreationDate(Date.valueOf(LocalDate.now()));

            recipeService.createRecipe(recipe);
        }
    }

    // Normal flow
    @When("I enter a valid recipe with title {string} into the search bar")
    public void i_enter_a_valid_recipe_with_title_into_the_search_bar(String recipeName) {
        searchResults = recipeService.getRecipesByTitle(recipeName);
    }

    @Then("I should see a list of recipes that match the specified name")
    public void i_should_see_a_list_of_recipes_that_match_the_specified_name() {
        assertFalse(searchResults.isEmpty(), "Expected recipes to be found, but none were returned.");
    }

    // Alternate flow
    @When("I enter a valid recipe with title {string} into the search bar that does not match any existing recipes")
    public void i_enter_a_valid_recipe_with_title_into_the_search_bar_that_does_not_match_any_existing_recipes(String recipeName) {
        try {
            searchResults = recipeService.getRecipesByTitle(recipeName);
        } catch (Exception e) {
            commonStepDefinitions.setException(e);
        }
    }

    // Error flow
    @When("I enter an invalid recipe with title {string} into the search page")
    public void i_enter_an_invalid_recipe_with_title_into_the_search_page(String recipeName) {
        try {
            if (!recipeName.matches("^[a-zA-Z ]+$")) {
                throw new Exception("Invalid recipe name");
            }
            searchResults = recipeService.getRecipesByTitle(recipeName);
        } catch (Exception e) {
            commonStepDefinitions.setException(e);
        }
    }

}
