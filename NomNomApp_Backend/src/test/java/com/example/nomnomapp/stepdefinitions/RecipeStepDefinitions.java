package com.example.nomnomapp.stepdefinitions;

import static org.junit.jupiter.api.Assertions.*;

import com.example.nomnomapp.model.Recipe;
import com.example.nomnomapp.model.RecipeIngredients;
import com.example.nomnomapp.model.Ingredient;
import com.example.nomnomapp.model.NomNomUser;
import com.example.nomnomapp.repository.UserRepository;
import com.example.nomnomapp.service.IngredientService;
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
    private CommonStepDefinitions commonSteps;
    @Autowired
    private UserService userService;

    private UserRepository userRepository;

    @Autowired
    private IngredientService ingredientService;

    private final Map<String, Recipe> recipeDatabase = new HashMap<>(); // Simulated recipe storage
    private Exception exception;
    private NomNomUser testUser;

    private List<Recipe> matchingRecipes;

    @Before
    public void setUp() {
        recipeDatabase.clear();
        recipeService.deleteAllRecipes();
        userService.deleteAllUsers();
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
    public void the_following_recipe_with_like_exist(String title, String likes) {
        Recipe newRecipe = new Recipe();
        newRecipe.setTitle(title);
        newRecipe.setLikes(Integer.parseInt(likes));
        newRecipe.setCreationDate(Date.valueOf(LocalDate.now()));
        newRecipe.setDescription("This is a test");
        newRecipe.setNomNomUser(userService.createUser("user", "user@email.com", "password"));
        recipeDatabase.put(title, newRecipe);
        recipeService.createRecipe(newRecipe);

    }

    @When("a user likes the recipe {string}")
    public void user_likes_recipe(String title) {
        Recipe newRecipe = recipeService.likeRecipe(recipeDatabase.get(title).getRecipeID());
        recipeDatabase.put(title, newRecipe);
    }


    @Then("the recipe with title {string} should have {string} likes")
    public void recipe_likes_updated(String title, String likes) {
        assertEquals(Integer.parseInt(likes), recipeDatabase.get(title).getLikes());
    }

    @When("a user unlikes the recipe {string}")
    public void user_unlikes_recipe(String title) {
        Recipe newRecipe = recipeService.unlikeRecipe(recipeDatabase.get(title).getRecipeID());
        recipeDatabase.put(title, newRecipe);
    }

    @Given("the following recipes exist in the system")
    public void the_following_recipes_exist_in_the_system(DataTable dataTable) {
        if (testUser == null) {
            testUser = userService.createUser("testUser", "test@example.com", "password");
        }
        List<Map<String, String>> recipes = dataTable.asMaps(String.class, String.class);

        for (Map<String, String> recipeData : recipes) {
            Recipe newRecipe = new Recipe();
            newRecipe.setTitle(recipeData.get("title"));
            newRecipe.setDescription(recipeData.get("description"));
            newRecipe.setCategory(Recipe.RecipeCategory.valueOf(recipeData.get("category")));

            // adding ingredients to the recipe
            String[] ingredientNames = recipeData.get("ingredients").split(", ");
            for (String ingredientName : ingredientNames) {
                //creating ingredients
                Ingredient ingredient = ingredient = ingredientService.createIngredient(ingredientName, "solid");
                RecipeIngredients recipeIngredient = new RecipeIngredients(1.0, "unit", newRecipe, ingredient);
                newRecipe.addRecipeIngredient(recipeIngredient);
            }

            newRecipe.setCreationDate(new Date(System.currentTimeMillis()));
            newRecipe.setNomNomUser(testUser);
            recipeDatabase.put(newRecipe.getTitle(), newRecipe);
            recipeService.createRecipe(newRecipe);
        }
    }

    @When("I request to view all recipes")
    public void i_request_to_view_all_recipes() {
        List<Recipe> allRecipes = recipeService.getAllRecipes();
        assertNotNull(allRecipes, "The recipe list should not be null.");
    }

    @Then("I should get a list of {string} recipes")
    public void i_should_get_list_of_recipes(String totalRecipes) {
        List<Recipe> allRecipes = recipeService.getAllRecipes();
        assertEquals(Integer.parseInt(totalRecipes), allRecipes.size(), "The total number of recipes does not match.");
    }

    @Then("the list should contain {string}, {string}, and {string}")
    public void the_list_should_contain(String recipe1, String recipe2, String recipe3) {
        List<Recipe> allRecipes = recipeService.getAllRecipes();
        List<String> recipeTitles = allRecipes.stream().map(Recipe::getTitle).toList();

        assertTrue(recipeTitles.contains(recipe1), "The recipe list should contain " + recipe1);
        assertTrue(recipeTitles.contains(recipe2), "The recipe list should contain " + recipe2);
        assertTrue(recipeTitles.contains(recipe3), "The recipe list should contain " + recipe3);
    }

    @When("I request to view a recipe that contains {string}")
    public void i_request_to_view_recipe_containing_ingredient(String ingredient) {
        matchingRecipes = recipeService.getRecipesByIngredients(ingredient);
        assertFalse(matchingRecipes.isEmpty(), "No recipes found with ingredient: " + ingredient);
    }

    @Then("I should receive the recipe {string}")
    public void i_should_receive_recipe(String title) {
        assertEquals(title, matchingRecipes.get(0).getTitle(), "Recipe title does not match.");
    }

    @Then("the recipe description should be {string}")
    public void the_recipe_description_should_be(String description) {
        assertEquals(description, matchingRecipes.get(0).getDescription(), "Recipe description does not match.");
    }

    @Then("the category should be {string}")
    public void the_category_should_be(String category) {
        assertEquals(category, matchingRecipes.get(0).getCategory().toString(), "Recipe category does not match.");
    }

    @When("I request to view a recipe with the title {string}")
    public void i_request_to_view_recipe_with_invalid_title(String invalidTitle) {
        try {
            recipeService.getRecipesByTitle(invalidTitle);
        } catch (IllegalArgumentException e) {
            commonSteps.setException(e);
        }
    }

    @When("I attempt to view a recipe that contains {string}")
    public void i_attempt_to_view_a_recipe_that_contains(String ingredientName) {
        try {
            recipeService.getRecipesByIngredients(ingredientName);
        } catch (IllegalArgumentException e) {
            commonSteps.setException(e);
        }
    }
}
