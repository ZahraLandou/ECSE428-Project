package com.example.nomnomapp.stepdefinitions;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.example.nomnomapp.model.RecipeList;
import com.example.nomnomapp.model.Recipe.RecipeCategory;
import com.example.nomnomapp.model.Recipe;
import com.example.nomnomapp.model.NomNomUser;
import com.example.nomnomapp.model.Rating;
import com.example.nomnomapp.service.RecipeListService;
import com.example.nomnomapp.service.RecipeService;
import com.example.nomnomapp.service.UserService;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.After;
import io.cucumber.java.Before;
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
    private final Map<String, Recipe> recipeDatabase = new HashMap<>(); // Simulated recipe storage
    private final Map<String, RecipeList> recipeListDatabase = new HashMap<>(); // Simulated recipe storage
    private Exception exception;

    @Before
    public void setUp() {
        recipeDatabase.clear();
        recipeListDatabase.clear();
        exception = null;
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

        List<Map<String, String>> recipeData = dataTable.asMaps(String.class, String.class);

        for (Map<String, String> recipeRow : recipeData) {
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
}