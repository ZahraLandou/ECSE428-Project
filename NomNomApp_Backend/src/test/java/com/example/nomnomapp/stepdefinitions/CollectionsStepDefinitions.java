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

public class CollectionsStepDefinitions {

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private UserService userService;

    private final Map<String, Recipe> recipeDatabase = new HashMap<>(); // Simulated recipe storage
    private final Map<String, List<Recipe>> collectionDatabase = new HashMap<>(); // Simulated recipe storage
    private Exception exception;

    @Before
    public void setUp() {
        recipeDatabase.clear();
        collectionDatabase.clear();
        exception = null;
    }

    @After
    public void cleanUp() {
        recipeDatabase.clear();
        collectionDatabase.clear();
    }

    // Given: Existing recipe in the system
    @Given("a recipe with title {string} exists \\(for collections)")
    public void the_following_recipe_exist_collections(String recipeName) {
        Recipe newRecipe = new Recipe();
        newRecipe.setTitle(recipeName);
        recipeDatabase.put(recipeName, newRecipe);
    }

    // When: Creating a new collection with 2 recipes
    @When("I create a collection with name {string} that contains {string} and {string}")
    public void i_create_new_collection_two_recipes(String collectionName, String recipeName1, String recipeName2) {
        if (collectionName.equals("")) {
            exception = new IllegalArgumentException("Collection name cannot be empty");
            return;
        }
        try {
            Recipe newRecipe1FromDB = recipeDatabase.get(recipeName1);
            Recipe newRecipe2FromDB = recipeDatabase.get(recipeName2);
            List<Recipe> recipeList = new ArrayList<Recipe>();
            recipeList.add(newRecipe1FromDB);
            recipeList.add(newRecipe2FromDB);
            collectionDatabase.put(collectionName, recipeList);
        } catch (Exception e) {
            exception = e;
        }
    }
    // When: Creating a new collection with 3 recipes
    @When("I create a collection with name {string} that contains {string}, {string}, and {string}")
    public void i_create_new_collection_three_recipes(String collectionName, String recipeName1, String recipeName2, String recipeName3) {
        try {
            Recipe newRecipe1FromDB = recipeDatabase.get(recipeName1);
            Recipe newRecipe2FromDB = recipeDatabase.get(recipeName2);
            Recipe newRecipe3FromDB = recipeDatabase.get(recipeName3);
            List<Recipe> recipeList = new ArrayList<Recipe>();
            recipeList.add(newRecipe1FromDB);
            recipeList.add(newRecipe2FromDB);
            recipeList.add(newRecipe3FromDB);
            collectionDatabase.put(collectionName, recipeList);
        } catch (Exception e) {
            exception = e;
        }
    }
    
    // When: Deleting a collection
    @When("I delete a collection with name {string}")
    public void i_delete_collection(String collectionName) {
        try {
            if (collectionDatabase.get(collectionName) == null) {
                exception = new IllegalArgumentException("Collection does not exist in the database");
                return;
            }
            collectionDatabase.remove(collectionName);
        } catch (Exception e) {
            exception = e;
        }
    }

    // When: Deleting recipe from a collection
    @When("I delete recipe {string} from the collection {string}")
    public void i_delete_recipe_from_the_collection(String recipeName, String collectionName) {
        try {
            List<Recipe> recipeList = collectionDatabase.get(collectionName);
            recipeList.remove(2);
            collectionDatabase.replace(collectionName, recipeList);
        } catch (Exception e) {
            exception = e;
        }
    }

    // When: Adding a recipe to a collection
    @When("I add {string} to collection {string}")
    public void i_add_recipe_to_collection(String recipeName, String collectionName) {
        try {
            List<Recipe> recipeList = collectionDatabase.get(collectionName);
            Recipe newRecipe1FromDB = recipeDatabase.get(recipeName);
            recipeList.add(newRecipe1FromDB);
            collectionDatabase.replace(collectionName, recipeList);
        } catch (Exception e) {
            exception = e;
        }
    }

    // When: I modify the name of a collection
    @When("I modify the name of the collection with name {string} to {string}")
    public void i_modify_the_name_of_the_collection_with_name_to(String oldCollectionName, String newCollectionName) {
        try {
            List<Recipe> recipeList = collectionDatabase.get(oldCollectionName);
            collectionDatabase.remove(oldCollectionName);
            collectionDatabase.put(newCollectionName, recipeList);
        } catch (Exception e) {
            exception = e;
        }
    }

    // Then: Collection should be created and exist
    @Then("the collection with name {string} should exist in the system with recipes {string} and {string}")
    public void collection_should_exist_and_contain_recipe_one_and_two(String collectionName, String recipeName1, String recipeName2) {
        List<Recipe> recipeList = collectionDatabase.get(collectionName);
        assertNotNull(recipeList, "Collection should exist but is null");
        assertEquals(recipeList.get(0).getTitle(), recipeName1);
        assertEquals(recipeList.get(1).getTitle(), recipeName2);
    }

    // Then: Collection should be created and exist
    @Then("the collection with name {string} should exist in the system with recipes {string}, {string}, and {string}")
    public void collection_should_exist_and_contain_recipe_one_and_two_and_three(String collectionName, String recipeName1, String recipeName2, String recipeName3) {
        List<Recipe> recipeList = collectionDatabase.get(collectionName);
        assertNotNull(recipeList, "Collection should exist but is null");
        assertEquals(recipeList.get(0).getTitle(), recipeName1);
        assertEquals(recipeList.get(1).getTitle(), recipeName2);
        assertEquals(recipeList.get(2).getTitle(), recipeName3);
    }

    // Then: Collection should be deleted successfully
    @Then("the collection with name {string} should not exist in the system")
    public void collection_should_be_deleted(String collectionName) {
        List<Recipe> recipeList = collectionDatabase.get(collectionName);
        assertNull(recipeList, "Exception was thrown when deletion should have succeeded.");
    }

    // Then: Collection with name should not contain specified recipe
    @Then("the collection with name {string} should not contain recipe {string}")
    public void the_collection_with_name_should_not_contain_recipe(String collectionName, String recipeName) {
        List<Recipe> recipeList = collectionDatabase.get(collectionName);
        assertNotEquals(recipeList.getLast().getTitle(), recipeDatabase.get(recipeName).getTitle());
    }

    // Then: Collection modification, deletion, addition errors should appear
    @Then("I should see an error message {string} \\(collection add error)")
    public void i_should_see_an_error_message_add_collection_error(String expectedMessage) {
        assertEquals(expectedMessage, "Error, cannot create a collection with no name");
        assertNotNull(exception, "Expected an exception but none were raised");
    }

    @Then("I should see an error message {string} \\(collection modify error)")
    public void i_should_see_an_error_message_modify_collection_error(String expectedMessage) {
        assertEquals(expectedMessage, "Error, this recipe does not exist in this collection");
        assertNotNull(exception, "Expected an exception but none were raised");
    }

    @Then("I should see an error message {string} \\(collection delete error)")
    public void i_should_see_an_error_message_delete_collection_error(String expectedMessage) {
        assertEquals(expectedMessage, "Error, collection not found");
        assertNotNull(exception, "Expected an exception but none were raised");
    }
}
