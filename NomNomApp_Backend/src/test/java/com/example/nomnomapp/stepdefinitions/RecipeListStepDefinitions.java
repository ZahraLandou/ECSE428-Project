package com.example.nomnomapp.stepdefinitions;

import com.example.nomnomapp.model.*;
import static org.junit.jupiter.api.Assertions.*;
import com.example.nomnomapp.service.RecipeListService;
import com.example.nomnomapp.service.RecipeService;
import com.example.nomnomapp.service.UserService;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Map;

public class RecipeListStepDefinitions {
    @Autowired
    private RecipeService recipeService;
    @Autowired
    private RecipeListService recipeListService;
    @Autowired
    private CommonStepDefinitions commonSteps;
    @Autowired
    private UserService userService;

    private Exception exception;
    private NomNomUser currentUser;

    private RecipeList myFavourites;

    @Before
    public void setUp() {
        recipeListService.deleteAllRecipeLists();
        recipeService.deleteAllRecipes();
        userService.deleteAllUsers();
        exception = null;
    }

    @Given("the following recipe lists exist in the system")
    public void the_following_recipelists_exist_in_the_system(DataTable dataTable) {
        List<Map<String, String>> recipes = dataTable.asMaps(String.class, String.class);

        for (Map<String, String> recipeListData : recipes) {
            //creating the recipelist
            RecipeList recipeList = new RecipeList();
            recipeList.setName(recipeListData.get("listName"));
            recipeList.setCategory(RecipeList.ListCategory.valueOf(recipeListData.get("category")));
            NomNomUser user = userService.getNomNomUserByName(recipeListData.get("username"));
            recipeList.setNomNomUser(user);
            assertNotNull(user, "User should not be null");
            recipeList = recipeListService.createRecipeList(recipeList);

            int id = recipeList.getRecipeListID();


            // adding recipes to the list
            String[] recipeListNames = recipeListData.get("recipes").split(", ");
            for (String name : recipeListNames) {
                List<Recipe> matchingRecipes = recipeService.getRecipesByTitle(name);
                assertFalse(matchingRecipes.isEmpty(), "Recipe " + name + " should exist in the system");
                Recipe recipe = matchingRecipes.get(0);   // we're testing with unique recipe names

                assertTrue(recipeListService.addRecipeToList(id, recipe));
            }

        }
    }

    @Given ("I am the user {string}")
    public void i_am_the_user (String username) {
        currentUser = userService.getNomNomUserByName(username);
    }

    @When("I request to view my Favourites recipe list")
    public void view_favorites_recipe_list() {
        myFavourites = recipeListService.getFavoriteRecipeListsByUser(currentUser).get(0); // we're testing with one favorite list by user
        System.out.println("Favorite Recipe List Retrieved: " + myFavourites.getName());
        System.out.println("Recipes in List: " + myFavourites.getRecipes());
    }

    @Then("I should see the recipes {string} in the list")
    public void see_recipes_in_the_list(String recipes) {
        String[] recipeNames = recipes.split(", ");
        List<Recipe> favoriteRecipes = myFavourites.getRecipes();

        assertEquals(recipeNames.length, favoriteRecipes.size(), "Mismatch in expected and actual recipe count");

        for (String name : recipeNames) {
            boolean found = favoriteRecipes.stream()
                    .anyMatch(recipe -> recipe.getTitle().equals(name.trim()));

            assertTrue(found, "Recipe not found in the list: " + name);
        }
    }


    @When("I attempt to view my Favourites recipe list")
    public void attempt_view_favorites_recipe_list() {
        try{
            myFavourites = recipeListService.getFavoriteRecipeListsByUser(currentUser).get(0); // we're testing with one favorite list by user
        } catch (IllegalArgumentException e) {
            commonSteps.setException(e);
        }
    }





}
