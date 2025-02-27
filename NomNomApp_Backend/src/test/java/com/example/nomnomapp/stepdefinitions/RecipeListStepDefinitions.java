package com.example.nomnomapp.stepdefinitions;

import static org.junit.jupiter.api.Assertions.*;

import com.example.nomnomapp.model.NomNomUser;
import com.example.nomnomapp.model.Recipe;
import com.example.nomnomapp.model.RecipeList;
import io.cucumber.java.en.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

@SpringBootTest
public class RecipeListStepDefinitions {
    private RecipeList recipeList;
    private NomNomUser mch;
    private final Map<String, RecipeList> recipeLists = new HashMap<>();
    private final Map<String, Recipe> recipes = new HashMap<>();

    @Given("a user exists")
    public void aUserExists() {
        mch = new NomNomUser("mch64", "mch64@gmail.com", "nonnom");
    }

    @When("the user creates a recipe list with name {string} and category {string}")
    public void theUserCreatesARecipeListWithNameAndCategory(String name, String category) {
        recipeList = new RecipeList(1, name, RecipeList.ListCategory.valueOf(category), mch);
        recipeLists.put(name, recipeList);
    }

    @Then("the recipe list should be created successfully")
    public void theRecipeListShouldBeCreatedSuccessfully() {
        assertNotNull(recipeList);
    }

    @Given("a user has a recipe list named {string}")
    public void aUserHasARecipeListNamed(String name) {
        recipeList = new RecipeList(1, name, RecipeList.ListCategory.Favorites, mch);
        recipeLists.put(name, recipeList);
    }

    @Given("a recipe {string} exists")
    public void aRecipeExists(String recipeName) {
        Recipe recipe = new Recipe();
        recipes.put(recipeName, recipe);
    }

    @When("the user adds the recipe {string} to {string}")
    public void theUserAddsTheRecipeTo(String recipeName, String listName) {
        RecipeList list = recipeLists.get(listName);
        list.addRecipe(recipes.get(recipeName));
    }

    @Then("the recipe list {string} should contain {string}")
    public void theRecipeListShouldContain(String listName, String recipeName) {
        RecipeList list = recipeLists.get(listName);
        assertTrue(list.getRecipes().contains(recipes.get(recipeName)));
    }

    @Given("a user has a recipe list named {string} containing {string}")
    public void aUserHasARecipeListNamedContaining(String listName, String recipeName) {
        aUserHasARecipeListNamed(listName);
        aRecipeExists(recipeName);
        theUserAddsTheRecipeTo(recipeName, listName);
    }

    @When("the user removes {string} from recipe list {string}")
    public void theUserRemovesFrom(String recipeName, String listName) {
        RecipeList list = recipeLists.get(listName);
        list.removeRecipe(recipes.get(recipeName));
    }

    @Then("the recipe list {string} should not contain {string}")
    public void theRecipeListShouldNotContain(String listName, String recipeName) {
        RecipeList list = recipeLists.get(listName);
        assertFalse(list.getRecipes().contains(recipes.get(recipeName)));
    }
}

