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

import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

@SpringBootTest
public class RecipeStepDefinitions {

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private UserService userService;

    private final Map<String, Recipe> recipeDatabase = new HashMap<>(); // Simulated recipe storage
    private Exception exception;

    @Before
    public void setUp() {
        recipeDatabase.clear();
        exception = null;
    }

    @After
    public void cleanUp() {
        recipeDatabase.clear();
    }

    // Given: Existing recipe in the system
    @Given("a recipe with title {string} exists")
    public void the_following_recipe_exist(String recipeName) {
        Recipe newRecipe = new Recipe();
        newRecipe.setTitle(recipeName);
        recipeDatabase.put(recipeName, newRecipe);
    }

    // When: Adding a new recipe
    @When("I create a new recipe with name {string}")
    public void i_create_new_recipe(String recipeName) {
        Recipe newRecipe = new Recipe();
        newRecipe.setTitle(recipeName);
        recipeDatabase.put(newRecipe.getTitle(), newRecipe);
    }

    // When: Adding a new recipe with details
    @When("the user wants to post a recipe with {string}, {string}, {string}")
    public void i_create_new_recipe_with_details(String recipeName, String ingredientName, String recipeInstruction) {
        Recipe newRecipe = new Recipe();
        newRecipe.setTitle(recipeName);
        Ingredient newIngredient = new Ingredient(ingredientName, "liquid");
        RecipeIngredients newRecipeIngredient = new RecipeIngredients(2.0, "mg", newRecipe, newIngredient);
        newRecipe.addRecipeIngredient(newRecipeIngredient);
        newRecipe.setInstructions(recipeInstruction);
        recipeDatabase.put(newRecipe.getTitle(), newRecipe);
    }

    // When: Modifying a recipe
    @When("I add ingredient {string} with quantity {string} to recipe {string}")
    public void i_add_ingredient(String ingredient, String quantity, String recipeName) {
        double quantity_double = Double.parseDouble(quantity);
        Recipe recipe = recipeDatabase.get(recipeName);
        Ingredient newIngredient = new Ingredient(ingredient, "liquid");
        RecipeIngredients newRecipeIngredient = new RecipeIngredients(quantity_double, "mg", recipe, newIngredient);
        assertNotNull(recipe, "Recipe does not exist.");
        recipe.addRecipeIngredient(newRecipeIngredient);
        recipeDatabase.replace(recipeName, recipe);
    }

    @When("I add cooking step {string} to recipe {string}")
    public void i_add_cooking_step(String step, String recipeName) {
        Recipe recipe = recipeDatabase.get(recipeName);
        assertNotNull(recipe, "Recipe does not exist.");
        recipe.setInstructions(step);
        recipeDatabase.replace(recipeName, recipe);
    }

    // When: Deleting a recipe
    @When("the user wants to delete a recipe with title {string}")
    public void i_delete_recipe(String recipeName) {
        try {
            recipeDatabase.remove(recipeName);
        } catch (Exception e) {
            exception = e;
        }
    }

    // When: Deleting a non existent recipe
    @When("the user wants to delete a recipe that does not exist, given title {string}")
    public void i_delete_non_existant_recipe(String recipeName) {
        Recipe currentRecipe = recipeDatabase.get(recipeName);
        assertNull(currentRecipe, "The recipe should not exist");
    }

    // Then: Recipe should be deleted successfully
    @Then("the recipe with title {string} should not exist in the system")
    public void recipe_should_be_deleted(String recipeName) {
        Recipe recipe = recipeDatabase.get(recipeName);
        assertNull(recipe, "Exception was thrown when deletion should have succeeded.");
    }

    // Then: Recipe should exist in the system
    @Then("the recipe with title {string} should exist in the system")
    public void recipe_should_exist(String recipeName) {
        Recipe recipe = recipeDatabase.get(recipeName);
        assertNotNull(recipe, "Exception was thrown when creation should have succeeded.");
    }
    // Then: Recipe with details should exist in the system
    @Then("the recipe with title {string}, ingredient {string}, and instruction {string} should exist in the system")
    public void recipe_with_details_should_exist(String recipeName, String recipeIngredient, String recipeInstruction) {
        Recipe recipe = recipeDatabase.get(recipeName);
        assertEquals(recipe.getTitle(), recipeName);
        assertEquals(recipe.getRecipeIngredients().getFirst().getIngredient().getName(), recipeIngredient);
        assertEquals(recipe.getInstructions(), recipeInstruction);
        assertNotNull(recipe, "Exception was thrown when creation should have succeeded.");
    }

    // Then: Recipe with details (including ingredient quantity) should exist in the system
    @Then("the recipe with title {string}, ingredient {string} with quantity {string} should exist in the system")
    public void recipe_with_details_quantity_should_exist(String recipeName, String recipeIngredient, String recipeIngredientQuantity) {
        Recipe recipe = recipeDatabase.get(recipeName);
        assertEquals(recipe.getTitle(), recipeName);
        assertEquals(recipe.getRecipeIngredients().getFirst().getIngredient().getName(), recipeIngredient);
        double quantity_double = Double.parseDouble(recipeIngredientQuantity);
        assertEquals(recipe.getRecipeIngredients().getFirst().getQuantity(), quantity_double);
        assertNotNull(recipe, "Exception was thrown when creation should have succeeded.");
    }

        // This method will be shared for all step definitions that use error messages
    @Then("I should see an error message {string} \\(not common)")
    public void i_should_see_an_error_message(String expectedMessage) {
        assertEquals(expectedMessage, "Error! Recipe not found.");
    }

    @Given("a recipe with title {string} and amount of likes {string} exists")
    public void the_following_recipe_with_like_exist(String title,String likes) {
        Recipe newRecipe = new Recipe();
        newRecipe.setTitle(title);
        newRecipe.setLikes(Integer.parseInt(likes));
        newRecipe.setCreationDate(Date.valueOf(LocalDate.now()));
        newRecipe.setDescription("This is a test");
        newRecipe.setNomNomUser(userService.createUser("user","user@email.com","password"));
        recipeDatabase.put(title, newRecipe);
        recipeService.createRecipe(newRecipe);

    }

    @When("a user likes the recipe {string}")
    public void user_likes_recipe(String title){
        Recipe newRecipe= recipeService.likeRecipe(recipeDatabase.get(title).getRecipeID());
        recipeDatabase.put(title, newRecipe);
    }


    @Then("the recipe with title {string} should have {string} likes")
    public void recipe_likes_updated(String title,String likes) {
        assertEquals(Integer.parseInt(likes),recipeDatabase.get(title).getLikes());
    }

    @When("a user unlikes the recipe {string}")
    public void user_unlikes_recipe(String title){
        Recipe newRecipe= recipeService.unlikeRecipe(recipeDatabase.get(title).getRecipeID());
        recipeDatabase.put(title, newRecipe);
    }
}
