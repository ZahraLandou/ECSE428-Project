package com.example.nomnomapp.stepdefinitions;

import static org.junit.jupiter.api.Assertions.*;

import com.example.nomnomapp.model.RecipeList;
import com.example.nomnomapp.model.Recipe;
import com.example.nomnomapp.model.NomNomUser;
import com.example.nomnomapp.service.RecipeListService;
import com.example.nomnomapp.service.RecipeService;
import com.example.nomnomapp.service.UserService;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.ParameterType;
import io.cucumber.java.en.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Date;
import java.util.*;

public class ViewCollectionsStepDefinitions {

    @Autowired
    private RecipeListService recipeListService;

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private UserService userService;

    private NomNomUser testUser;
    private RecipeList currentlyViewedRecipeList;
    private final Map<String, Recipe> recipeDatabase = new HashMap<>(); // Simulated recipe storage
    private final Map<String, RecipeList> recipeListDatabase = new HashMap<>(); // Simulated recipe storage

    // parameter types
    @ParameterType(".*")
    public String name(String name) {
        return name;
    }

    @ParameterType(".*")
    public String description(String description) {
        return description;
    }

    @Before
    public void setUp() {
        recipeDatabase.clear();
        recipeListDatabase.clear();
    }

    @After
    public void cleanUp() {
        recipeDatabase.clear();
        recipeListDatabase.clear();
    }

    // ----------------------------------------------------------------------
    // GIVEN
    // ----------------------------------------------------------------------

    @Given("the user is logged into the NomNom application")
    public void the_user_is_logged_into_the_NomNom_application() {

        testUser = new NomNomUser("testUser", "test@example.com", "password");
        testUser.setUserId(1);
    }

    @Given("the user is not logged into the NomNom application")
    public void the_user_is_not_logged_into_the_NomNom_application() {

        testUser = null;
    }

    @Given("the user has created the {name} list, of category \"Regular\"")
    public void the_user_has_created_the_list_of_category_regular(String name) {

        RecipeList recipeList = new RecipeList();

        recipeList.setName(name);
        recipeList.setCategory(RecipeList.ListCategory.Regular);
        recipeList.setNomNomUser(testUser);

        recipeListDatabase.put(name, recipeList);
    }

    @Given("the user has saved the following recipes in the {name} list")
    public void the_user_has_saved_the_following_recipes_in_the_list(String name, DataTable dataTable) {

        RecipeList recipeList = recipeListDatabase.get(name);
        if (recipeList == null) {
            the_user_has_created_the_list_of_category_regular(name);
        }

        List<Map<String, String>> recipeListData = dataTable.asMaps(String.class, String.class);

        for (Map<String, String> recipeRow : recipeListData) {
            // get table data
            String title = recipeRow.get("name");
            String description = recipeRow.get("description");

            // create new recipe
            Recipe recipe = new Recipe();
            recipe.setTitle(title);
            recipe.setDescription(description);
            recipe.setCreationDate(new Date(System.currentTimeMillis()));

            // link recipe to user
            recipe.setNomNomUser(testUser);
            testUser.addRecipe(recipe);

            // link recipe to list
            recipeList.addRecipe(recipe);

            // update database
            recipeDatabase.put(title, recipe);
            recipeListDatabase.put(name, recipeList);
        }
    }

    @Given("the user has no recipes saved in their {name} list")
    public void the_user_has_no_recipes_saved_in_their_list(String name) {

        // remove old recipe list
        RecipeList recipeList = recipeListDatabase.get(name);
        if (recipeList != null) {
            for (Recipe recipe : recipeList.getRecipes()) {
                recipeDatabase.remove(recipe.getTitle());
            }
            recipeListDatabase.remove(name);
        }

        // create new recipe list with no recipes in it
        the_user_has_created_the_list_of_category_regular(name);
    }

    // ----------------------------------------------------------------------
    // WHEN
    // ----------------------------------------------------------------------

    @When("the user navigates to the {name} list")
    public void the_user_navigates_to_the_list(String name) {

        if (testUser != null)
            currentlyViewedRecipeList = recipeListDatabase.get(name);
        else
            currentlyViewedRecipeList = null;
    }

    // ----------------------------------------------------------------------
    // THEN
    // ----------------------------------------------------------------------

    @Then("the NomNom application should display the following recipes, their description, and their other attributes")
    public void the_NomNom_application_should_display_the_following_recipes(DataTable dataTable) {

        // assert currently viewed recipe list exists
        assertNotNull(currentlyViewedRecipeList);

        // expected list and actual list
        List<Map<String, String>> recipeListExpected = dataTable.asMaps(String.class, String.class);
        List<Recipe> recipeListActual = currentlyViewedRecipeList.getRecipes();

        // assert expected and actual lists are same size
        assertEquals(recipeListExpected.size(), recipeListActual.size(),
                String.format("Expected recipe list size: %d; Actual recipe list size: %d",
                        recipeListExpected.size(), recipeListActual.size()));

        // count is num of matching recipe records (rows)
        int count = 0;
        int expectedCount = recipeListExpected.size();

        for (Map<String, String> recipeRow : recipeListExpected) {

            boolean isInRecipeListActual = false;

            // get table data
            String title = recipeRow.get("name");
            String description = recipeRow.get("description");

            // check if exists in actual currently viewed recipe list
            for (Recipe recipe : recipeListActual) {
                if (recipe.getTitle().equals(title) && recipe.getDescription().equals(description)) {
                    count++;
                    isInRecipeListActual = true;
                }
            }

            // assert expected recipe is in the actual list
            assertTrue(isInRecipeListActual, String.format("Expected recipe %s not in actual recipe list", title));
        }

        // assert all expected recipes are in the actual list
        assertEquals(expectedCount, count,
                String.format("Expected %d recipes matching but got %d recipes matching", expectedCount, count));
    }

    @Then("the NomNom application should display the empty {name} list with no recipes")
    public void the_NomNom_application_should_display_the_empty_list_with_no_recipes(String name) {

        // assert currently viewed recipe list exists
        assertNotNull(currentlyViewedRecipeList);

        // expected list and actual list
        RecipeList recipeListExpected = recipeListDatabase.get(name);
        RecipeList recipeListActual = currentlyViewedRecipeList;

        // assert expected and actual lists are same list
        assertEquals(recipeListExpected.getName(), recipeListActual.getName(),
                String.format("Expected recipe list: %s; Actual recipe list: %s",
                        recipeListExpected.getName(), recipeListActual.getName()));

        // assert actual list is empty
        assertEquals(0, recipeListActual.getRecipes().size(),
                String.format("Expected recipe list size: 0; Actual recipe list size: %d",
                        recipeListActual.getRecipes().size()));
    }

    @Then("the NomNom application should inform the user they are not logged in")
    public void the_NomNom_application_should_inform_the_user_they_are_not_logged_in() {
        assertNull(testUser);
    }
}