package com.example.nomnomapp.stepdefinitions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import io.cucumber.datatable.DataTable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;

import com.example.nomnomapp.model.Recipe;
import com.example.nomnomapp.model.RecipeList;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.nomnomapp.model.NomNomUser;
import com.example.nomnomapp.repository.UserRepository;
import com.example.nomnomapp.service.UserService;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;



@SpringBootTest
public class UserStepDefinitions {

    @Mock
    private UserRepository userRepository;  // Use Mock instead of actual repository

    @InjectMocks
    private UserService userService;

    @Autowired
    private CommonStepDefinitions commonStepDefinitions;

    private final Map<Integer, NomNomUser> userDatabase = new HashMap<>(); // Simulated user storage
    private final Map<String, NomNomUser> userDatabaseForModify = new HashMap<>(); // Simulated user storage

    private ResponseEntity<?> profileResponse;
    private List<Recipe> retrievedRecipes;
    private List<Recipe> retrievedFavoriteRecipes;
    private String profileUsername;
    private Map<String, NomNomUser> testUsers = new HashMap<>();
    private Map<String, List<Recipe>> userRecipes = new HashMap<>();
    private Map<String, List<Recipe>> userFavorites = new HashMap<>();
    private String currentUsername;
    private Exception profileException;
    private Exception exception;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize Mocks
        userDatabase.clear();
        userDatabaseForModify.clear();
        exception = null;
    }

    @After
    public void cleanUp() {
        userDatabase.clear();
        userDatabaseForModify.clear();
    }

    @Given("the following userId exist:")
    public void the_following_users_exist(DataTable dataTable) {
        List<Map<String, String>> users = dataTable.asMaps(String.class, String.class);
        for (Map<String, String> row : users) {
            int userId = Integer.parseInt(row.get("userId"));
            NomNomUser user = new NomNomUser(row.get("username"), row.get("email"), "password123");
            user.setUserId(userId);
            userDatabase.put(userId, user);

            // Mock repository behavior
            when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        }
    }

    @Given("a user with ID {int} exists")
    public void a_user_with_id_exists(int userId) {
        assertTrue(userDatabase.containsKey(userId), "User with ID " + userId + " does not exist.");
        when(userRepository.findById(userId)).thenReturn(Optional.ofNullable(userDatabase.get(userId)));
    }

    // Given: User exists with username
    @Given("User with username {string} exists")
    public void user_with_username_exists(String username) {
        NomNomUser user = new NomNomUser();
        user.setUsername(username);
        userDatabaseForModify.put(username, user);
    }

    // Given: User exists with username and has a specific email address
    @Given("User with username {string} has email address {string}")
    public void user_has_email(String username, String email) {
        NomNomUser user = userDatabaseForModify.get(username);
        assertNotNull(user, "User does not exist.");
        user.setEmailAddress(email);
        userDatabaseForModify.replace(username, user);
    }

    @Given("no user with ID {int} exists")
    public void no_user_with_id_exists(int userId) {
        userDatabase.remove(userId); // Explicitly remove the user
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
    }

    // Profile View - Given steps
    @Given("a user exists in the system")
    public void a_user_exists_in_the_system() {
        profileUsername = "profiletestuser";
        NomNomUser user = new NomNomUser(profileUsername, "profiletest@example.com", "password123");
        user.setBiography("Test biography for profile");
        user.setProfilePicture("profile-pic.jpg");

        when(userRepository.findByUsername(profileUsername)).thenReturn(Optional.of(user));
    }

    @Given("a user exists with some posted recipes")
    public void a_user_exists_with_some_posted_recipes() {
        // First set up basic user
        a_user_exists_in_the_system();

        // Get the user from mock
        Optional<NomNomUser> userOpt = userRepository.findByUsername(profileUsername);
        assertTrue(userOpt.isPresent(), "User should exist in the mock");
        NomNomUser user = userOpt.get();

        // Create recipes
        Recipe recipe1 = new Recipe();
        recipe1.setTitle("Test Recipe 1");
        recipe1.setDescription("First test recipe");
        recipe1.setInstructions("Test instructions");
        recipe1.setCategory(Recipe.RecipeCategory.Dinner);
        recipe1.setCreationDate(new Date(System.currentTimeMillis()));
        recipe1.setNomNomUser(user);

        Recipe recipe2 = new Recipe();
        recipe2.setTitle("Test Recipe 2");
        recipe2.setDescription("Second test recipe");
        recipe2.setInstructions("More test instructions");
        recipe2.setCategory(Recipe.RecipeCategory.Breakfast);
        recipe2.setCreationDate(new Date(System.currentTimeMillis()));
        recipe2.setNomNomUser(user);

        // Add recipes to user
        user.addRecipe(recipe1);
        user.addRecipe(recipe2);

        // Mock the repository
        when(userRepository.findByUsername(profileUsername)).thenReturn(Optional.of(user));
    }

    @Given("a user exists with favorite recipes")
    public void a_user_exists_with_favorite_recipes() {
        // Set up user with recipes
        a_user_exists_with_some_posted_recipes();

        // Get the user from mock
        Optional<NomNomUser> userOpt = userRepository.findByUsername(profileUsername);
        assertTrue(userOpt.isPresent(), "User should exist in the mock");
        NomNomUser user = userOpt.get();

        // Create favorites list
        RecipeList favoritesList = new RecipeList();
        favoritesList.setName("Favorites");
        favoritesList.setCategory(RecipeList.ListCategory.Favorites);
        favoritesList.setNomNomUser(user);

        // Add first recipe to favorites
        Recipe favoriteRecipe = user.getRecipes().get(0);
        favoritesList.addRecipe(favoriteRecipe);

        // Add list to user
        user.addRecipeList(favoritesList);

        // Update the mock
        when(userRepository.findByUsername(profileUsername)).thenReturn(Optional.of(user));
    }

    @Given("a user exists who has not posted any recipes")
    public void a_user_exists_who_has_not_posted_any_recipes() {
        // Just set up a basic user without recipes
        a_user_exists_in_the_system();
    }

    @Given("a user exists who has not favorited any recipes")
    public void a_user_exists_who_has_not_favorited_any_recipes() {
        // First set up user with recipes
        a_user_exists_with_some_posted_recipes();

        // Get the user from mock
        Optional<NomNomUser> userOpt = userRepository.findByUsername(profileUsername);
        assertTrue(userOpt.isPresent(), "User should exist in the mock");
        NomNomUser user = userOpt.get();

        // Create empty favorites list
        RecipeList favoritesList = new RecipeList();
        favoritesList.setName("Favorites");
        favoritesList.setCategory(RecipeList.ListCategory.Favorites);
        favoritesList.setNomNomUser(user);

        // Add empty list to user
        user.addRecipeList(favoritesList);

        // Update the mock
        when(userRepository.findByUsername(profileUsername)).thenReturn(Optional.of(user));
    }

    @Given("no user exists with the requested username")
    public void no_user_exists_with_the_requested_username() {
        profileUsername = "nonexistentprofileuser";
        when(userRepository.findByUsername(profileUsername)).thenReturn(Optional.empty());
    }

    // Background step definitions
    @Given("the following users exist:")
    public void the_following_users_exist_1(DataTable dataTable) {
        List<Map<String, String>> userData = dataTable.asMaps(String.class, String.class);

        for (Map<String, String> user : userData) {
            String username = user.get("username");
            String email = user.get("email");
            String biography = user.get("biography");

            NomNomUser nomNomUser = new NomNomUser(username, email, "password123");
            nomNomUser.setBiography(biography);
            nomNomUser.setProfilePicture("default-profile.jpg");

            testUsers.put(username, nomNomUser);
            userRecipes.put(username, new ArrayList<>());
            userFavorites.put(username, new ArrayList<>());

            // Mock the repository
            when(userRepository.findByUsername(username)).thenReturn(Optional.of(nomNomUser));
        }
    }

    @Given("the following recipes exist:")
    public void the_following_recipes_exist(DataTable dataTable) {
        List<Map<String, String>> recipeData = dataTable.asMaps(String.class, String.class);

        for (Map<String, String> recipe : recipeData) {
            String title = recipe.get("title");
            String description = recipe.get("description");
            String username = recipe.get("username");

            NomNomUser user = testUsers.get(username);
            if (user != null) {
                Recipe newRecipe = new Recipe();
                newRecipe.setTitle(title);
                newRecipe.setDescription(description);
                newRecipe.setInstructions("Sample instructions");
                newRecipe.setCreationDate(new Date(System.currentTimeMillis()));
                newRecipe.setCategory(Recipe.RecipeCategory.Dinner);
                newRecipe.setNomNomUser(user);

                // Add recipe to user's recipes list
                userRecipes.get(username).add(newRecipe);
                user.addRecipe(newRecipe);
            }
        }

        // Update mocks after adding recipes
        for (String username : testUsers.keySet()) {
            when(userRepository.findByUsername(username)).thenReturn(Optional.of(testUsers.get(username)));
        }
    }

    @Given("the following favorite recipes exist:")
    public void the_following_favorite_recipes_exist(DataTable dataTable) {
        List<Map<String, String>> favoriteData = dataTable.asMaps(String.class, String.class);

        for (Map<String, String> favorite : favoriteData) {
            String username = favorite.get("username");
            String title = favorite.get("title");

            NomNomUser user = testUsers.get(username);
            if (user != null) {
                // Find the recipe by title
                for (Recipe recipe : userRecipes.get(username)) {
                    if (recipe.getTitle().equals(title)) {
                        // Create favorites list if it doesn't exist
                        boolean hasFavoritesList = false;
                        RecipeList favoritesList = null;

                        for (RecipeList list : user.getRecipeLists()) {
                            if (list.getCategory() == RecipeList.ListCategory.Favorites) {
                                favoritesList = list;
                                hasFavoritesList = true;
                                break;
                            }
                        }

                        if (!hasFavoritesList) {
                            favoritesList = new RecipeList();
                            favoritesList.setName("Favorites");
                            favoritesList.setCategory(RecipeList.ListCategory.Favorites);
                            favoritesList.setNomNomUser(user);
                            user.addRecipeList(favoritesList);
                        }

                        // Add recipe to favorites
                        favoritesList.addRecipe(recipe);
                        userFavorites.get(username).add(recipe);
                        break;
                    }
                }
            }
        }

        // Update mocks after adding favorites
        for (String username : testUsers.keySet()) {
            when(userRepository.findByUsername(username)).thenReturn(Optional.of(testUsers.get(username)));
        }
    }

    // Normal flow step definitions
    @Given("a user with username {string} exists")
    public void a_user_with_username_exists(String username) {
        currentUsername = username;
        assertTrue(testUsers.containsKey(username), "Test user should exist: " + username);
    }







    @When("I delete the user with ID {int}")
    public void i_delete_the_user_with_id(int userId) {
        try {
            Optional<NomNomUser> userToDelete = userRepository.findById(userId);
            if (userToDelete.isPresent()){
                userDatabase.remove(userId);
            }
            userService.deleteUserById(userId);
        } catch (Exception e) {
            commonStepDefinitions.setException(e);
        }
    }

    // When: Modifying username
    @When("I modify the username of user {string} to {string}")
    public void i_modify_username(String currentUsername, String newUsername) {
        NomNomUser user = userDatabaseForModify.get(currentUsername);
        assertNotNull(user, "User does not exist.");
        userDatabaseForModify.remove(currentUsername);
        user.setUsername(newUsername);
        userDatabaseForModify.put(newUsername, user);
    }

    // When: Modifying email
    @When("I modify the email address of user {string} to {string}")
    public void i_modify_email(String username, String newEmail) {
        NomNomUser user = userDatabaseForModify.get(username);
        assertNotNull(user, "User does not exist.");
        user.setEmailAddress(newEmail);
        userDatabaseForModify.replace(username, user);
    }

    // When: Attempting to modify to an existing username
    @When("I modify username of user {string} to username {string}")
    public void i_modify_username_to_existing(String userName2, String userName1) {
        if (userDatabaseForModify.containsKey(userName1)) {
            exception = new IllegalArgumentException("Username already exists.");
        } else {
            i_modify_username(userName2, userName1);
        }
    }

    // Profile View - When steps
    @When("I request to view this user's profile")
    public void i_request_to_view_this_user_s_profile() {
        try {
            Optional<NomNomUser> userOpt = userRepository.findByUsername(profileUsername);
            if (userOpt.isPresent()) {
                NomNomUser user = userOpt.get();
                Map<String, Object> profileData = new HashMap<>();
                profileData.put("username", user.getUsername());
                profileData.put("biography", user.getBiography());
                profileData.put("profilePicture", user.getProfilePicture());
                profileData.put("recipesCount", user.numberOfRecipes());
                profileData.put("followersCount", user.numberOfFollowers());
                profileData.put("followingCount", user.numberOfFollowing());
                profileResponse = ResponseEntity.ok(profileData);
            } else {
                profileResponse = ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            exception = e;
        }
    }

    @When("I request to view this user's recipes")
    public void i_request_to_view_this_user_s_recipes() {
        try {
            Optional<NomNomUser> userOpt = userRepository.findByUsername(profileUsername);
            if (userOpt.isPresent()) {
                NomNomUser user = userOpt.get();
                retrievedRecipes = new ArrayList<>(user.getRecipes());
                profileResponse = ResponseEntity.ok(retrievedRecipes);
            } else {
                profileResponse = ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            exception = e;
        }
    }

    @When("I request to view this user's favorite recipes")
    public void i_request_to_view_this_user_s_favorite_recipes() {
        try {
            Optional<NomNomUser> userOpt = userRepository.findByUsername(profileUsername);
            if (userOpt.isPresent()) {
                NomNomUser user = userOpt.get();
                retrievedFavoriteRecipes = new ArrayList<>();

                // Find favorites list
                for (RecipeList list : user.getRecipeLists()) {
                    if (list.getCategory() == RecipeList.ListCategory.Favorites) {
                        retrievedFavoriteRecipes.addAll(list.getRecipes());
                        break;
                    }
                }

                profileResponse = ResponseEntity.ok(retrievedFavoriteRecipes);
            } else {
                profileResponse = ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            exception = e;
        }
    }

    // These are aliases for the error flow scenarios
    @When("I attempt to view this user's profile")
    public void i_attempt_to_view_this_user_s_profile() {
        i_request_to_view_this_user_s_profile();
    }

    @When("I attempt to view this user's recipes")
    public void i_attempt_to_view_this_user_s_recipes() {
        i_request_to_view_this_user_s_recipes();
    }

    @When("I attempt to view this user's favorite recipes")
    public void i_attempt_to_view_this_user_s_favorite_recipes() {
        i_request_to_view_this_user_s_favorite_recipes();
    }

    @When("I request to view the profile of user {string}")
    public void i_request_to_view_the_profile_of_user(String username) {
        try {
            Optional<NomNomUser> userOpt = userRepository.findByUsername(username);
            if (userOpt.isPresent()) {
                NomNomUser user = userOpt.get();
                Map<String, Object> profileData = new HashMap<>();
                profileData.put("username", user.getUsername());
                profileData.put("biography", user.getBiography());
                profileData.put("profilePicture", user.getProfilePicture());
                profileData.put("recipesCount", user.numberOfRecipes());
                profileData.put("followersCount", user.numberOfFollowers());
                profileData.put("followingCount", user.numberOfFollowing());
                profileResponse = ResponseEntity.ok(profileData);
            } else {
                profileResponse = ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("User with username '" + username + "' not found");
            }
        } catch (Exception e) {
            profileException = e;
        }
    }

    @When("I request to view the recipes of user {string}")
    public void i_request_to_view_the_recipes_of_user(String username) {
        try {
            Optional<NomNomUser> userOpt = userRepository.findByUsername(username);
            if (userOpt.isPresent()) {
                NomNomUser user = userOpt.get();
                List<Recipe> recipes = new ArrayList<>(user.getRecipes());
                profileResponse = ResponseEntity.ok(recipes);
            } else {
                profileResponse = ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("User with username '" + username + "' not found");
            }
        } catch (Exception e) {
            profileException = e;
        }
    }

    @When("I request to view the favorite recipes of user {string}")
    public void i_request_to_view_the_favorite_recipes_of_user(String username) {
        try {
            Optional<NomNomUser> userOpt = userRepository.findByUsername(username);
            if (userOpt.isPresent()) {
                NomNomUser user = userOpt.get();
                List<Recipe> favorites = new ArrayList<>();

                for (RecipeList list : user.getRecipeLists()) {
                    if (list.getCategory() == RecipeList.ListCategory.Favorites) {
                        favorites.addAll(list.getRecipes());
                        break;
                    }
                }

                profileResponse = ResponseEntity.ok(favorites);
            } else {
                profileResponse = ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("User with username '" + username + "' not found");
            }
        } catch (Exception e) {
            profileException = e;
        }
    }




    @Then("the user should be deleted successfully")
    public void the_user_should_be_deleted_successfully() {
        assertNull(commonStepDefinitions.getException(), "Exception was thrown when deletion should have succeeded.");
        verify(userRepository, times(1)).delete(any(NomNomUser.class)); // Ensure delete was called once
    }

    // Then: User with new username should exist
    @Then("User with username {string} should exist in the system")
    public void user_should_exist(String username) {
        assertTrue(userDatabaseForModify.containsKey(username), "User does not exist.");
    }

    // Then: User with old username should not exist
    @Then("User with username {string} shoud not exist in the system")
    public void user_should_not_exist(String username) {
        assertFalse(userDatabaseForModify.containsKey(username), "User should not exist.");
    }

    // Then: User should not have old email
    @Then("User with username {string} should not have email address {string}")
    public void user_should_not_have_old_email(String username, String oldEmail) {
        NomNomUser user = userDatabaseForModify.get(username);
        assertNotNull(user, "User does not exist.");
        assertNotEquals(oldEmail, user.getEmailAddress(), "User still has old email.");
    }

    // Then: User should have new email
    @Then("User with username {string} should have email address {string}")
    public void user_should_have_new_email(String username, String newEmail) {
        NomNomUser user = userDatabaseForModify.get(username);
        assertNotNull(user, "User does not exist.");
        assertEquals(newEmail, user.getEmailAddress(), "User email was not updated.");
    }

    // Then: Error message for existing username
    /*@Then("I should see an error message {string} \\(user modif)")
    public void i_should_see_error_message_for_user_modif(String message) {
        assertNotNull(exception, "Expected an error but none was thrown.");
        assertEquals(message, exception.getMessage(), "Error message mismatch.");
    }*/

    // Profile View - Then steps
    @Then("the profile information should be displayed correctly")
    public void the_profile_information_should_be_displayed_correctly() {
        assertNotNull(profileResponse, "Profile response should not be null");
        assertEquals(HttpStatus.OK, profileResponse.getStatusCode(), "Status code should be OK");

        @SuppressWarnings("unchecked")
        Map<String, Object> profileData = (Map<String, Object>) profileResponse.getBody();
        assertNotNull(profileData, "Profile data should not be null");
        assertEquals(profileUsername, profileData.get("username"), "Username should match");
    }

    @Then("the biography should be visible")
    public void the_biography_should_be_visible() {
        assertNotNull(profileResponse, "Profile response should not be null");

        @SuppressWarnings("unchecked")
        Map<String, Object> profileData = (Map<String, Object>) profileResponse.getBody();
        assertNotNull(profileData, "Profile data should not be null");
        assertNotNull(profileData.get("biography"), "Biography should not be null");
    }

    @Then("the number of recipes should be shown")
    public void the_number_of_recipes_should_be_shown() {
        assertNotNull(profileResponse, "Profile response should not be null");

        @SuppressWarnings("unchecked")
        Map<String, Object> profileData = (Map<String, Object>) profileResponse.getBody();
        assertNotNull(profileData, "Profile data should not be null");
        assertNotNull(profileData.get("recipesCount"), "Recipe count should not be null");
    }

    @Then("the number of recipes should be shown as zero")
    public void the_number_of_recipes_should_be_shown_as_zero() {
        assertNotNull(profileResponse, "Profile response should not be null");

        @SuppressWarnings("unchecked")
        Map<String, Object> profileData = (Map<String, Object>) profileResponse.getBody();
        assertNotNull(profileData, "Profile data should not be null");
        assertEquals(0, profileData.get("recipesCount"), "Recipe count should be zero");
    }

    @Then("all recipes posted by this user should be displayed")
    public void all_recipes_posted_by_this_user_should_be_displayed() {
        assertNotNull(profileResponse, "Profile response should not be null");
        assertEquals(HttpStatus.OK, profileResponse.getStatusCode(), "Status code should be OK");
        assertNotNull(retrievedRecipes, "Retrieved recipes should not be null");
    }

    @Then("each recipe should show its title and details")
    public void each_recipe_should_show_its_title_and_details() {
        for (Recipe recipe : retrievedRecipes) {
            assertNotNull(recipe.getTitle(), "Recipe title should not be null");
            assertNotNull(recipe.getDescription(), "Recipe description should not be null");
        }
    }

    @Then("all recipes marked as favorite by this user should be displayed")
    public void all_recipes_marked_as_favorite_by_this_user_should_be_displayed() {
        assertNotNull(profileResponse, "Profile response should not be null");
        assertEquals(HttpStatus.OK, profileResponse.getStatusCode(), "Status code should be OK");
        assertNotNull(retrievedFavoriteRecipes, "Retrieved favorite recipes should not be null");
    }

    @Then("each favorite recipe should show its title and details")
    public void each_favorite_recipe_should_show_its_title_and_details() {
        for (Recipe recipe : retrievedFavoriteRecipes) {
            assertNotNull(recipe.getTitle(), "Recipe title should not be null");
        }
    }

    @Then("be informed that the user does not exist")
    public void be_informed_that_the_user_does_not_exist() {
        assertEquals(HttpStatus.NOT_FOUND, profileResponse.getStatusCode(), "Status code should be NOT_FOUND");
    }

    @Then("an empty list of favorite recipes should be displayed")
    public void an_empty_list_of_favorite_recipes_should_be_displayed() {
        assertNotNull(profileResponse, "Profile response should not be null");
        assertEquals(HttpStatus.OK, profileResponse.getStatusCode(), "Status code should be OK");
        assertNotNull(retrievedFavoriteRecipes, "Retrieved favorite recipes should not be null");
        assertTrue(retrievedFavoriteRecipes.isEmpty(), "Favorite recipes list should be empty");
    }

    @Then("the profile should be displayed successfully")
    public void the_profile_should_be_displayed_successfully() {
        assertNull(profileException, "No exception should be thrown");
        assertNotNull(profileResponse, "Response should not be null");
        assertEquals(HttpStatus.OK, profileResponse.getStatusCode(), "Status code should be OK");
    }

    @Then("the profile should show the biography {string}")
    public void the_profile_should_show_the_biography(String biography) {
        assertNotNull(profileResponse, "Response should not be null");
        assertEquals(HttpStatus.OK, profileResponse.getStatusCode(), "Status code should be OK");

        @SuppressWarnings("unchecked")
        Map<String, Object> profileData = (Map<String, Object>) profileResponse.getBody();
        assertNotNull(profileData, "Profile data should not be null");
        assertEquals(biography, profileData.get("biography"), "Biography should match");
    }

    @Then("the profile should display the correct number of recipes")
    public void the_profile_should_display_the_correct_number_of_recipes() {
        assertNotNull(profileResponse, "Response should not be null");

        @SuppressWarnings("unchecked")
        Map<String, Object> profileData = (Map<String, Object>) profileResponse.getBody();
        assertNotNull(profileData, "Profile data should not be null");

        int expectedCount = userRecipes.get(currentUsername).size();
        assertEquals(expectedCount, profileData.get("recipesCount"), "Recipe count should match");
    }

    @Then("the recipes should be displayed successfully")
    public void the_recipes_should_be_displayed_successfully() {
        assertNull(profileException, "No exception should be thrown");
        assertNotNull(profileResponse, "Response should not be null");
        assertEquals(HttpStatus.OK, profileResponse.getStatusCode(), "Status code should be OK");
    }

    @Then("I should see {int} recipes in the list")
    public void i_should_see_recipes_in_the_list(Integer count) {
        assertNotNull(profileResponse, "Response should not be null");

        @SuppressWarnings("unchecked")
        List<Recipe> recipes = (List<Recipe>) profileResponse.getBody();
        assertNotNull(recipes, "Recipes list should not be null");
        assertEquals(count.intValue(), recipes.size(), "Recipe count should match");
    }

    @Then("the list should include a recipe titled {string}")
    public void the_list_should_include_a_recipe_titled(String title) {
        if (title == null || title.isEmpty()) {
            return; // Skip check for empty titles
        }

        assertNotNull(profileResponse, "Response should not be null");

        @SuppressWarnings("unchecked")
        List<Recipe> recipes = (List<Recipe>) profileResponse.getBody();
        assertNotNull(recipes, "Recipes list should not be null");

        boolean found = false;
        for (Recipe recipe : recipes) {
            if (recipe.getTitle().equals(title)) {
                found = true;
                break;
            }
        }

        assertTrue(found, "Recipe with title '" + title + "' should be found");
    }

    @Then("the favorite recipes should be displayed successfully")
    public void the_favorite_recipes_should_be_displayed_successfully() {
        assertNull(profileException, "No exception should be thrown");
        assertNotNull(profileResponse, "Response should not be null");
        assertEquals(HttpStatus.OK, profileResponse.getStatusCode(), "Status code should be OK");
    }

    @Then("I should see {int} favorite recipes")
    public void i_should_see_favorite_recipes(Integer count) {
        assertNotNull(profileResponse, "Response should not be null");

        @SuppressWarnings("unchecked")
        List<Recipe> favorites = (List<Recipe>) profileResponse.getBody();
        assertNotNull(favorites, "Favorites list should not be null");
        assertEquals(count.intValue(), favorites.size(), "Favorite recipes count should match");
    }

    @Then("the favorites should include {string} if count is not zero")
    public void the_favorites_should_include_if_count_is_not_zero(String title) {
        @SuppressWarnings("unchecked")
        List<Recipe> favorites = (List<Recipe>) profileResponse.getBody();

        if (favorites.isEmpty() || title == null || title.isEmpty()) {
            return; // Skip check for empty lists or titles
        }

        boolean found = false;
        for (Recipe recipe : favorites) {
            if (recipe.getTitle().equals(title)) {
                found = true;
                break;
            }
        }

        assertTrue(found, "Favorite recipe with title '" + title + "' should be found");
    }

    @Then("I should see an error message {string}")
    public void i_should_see_an_error_message(String message) {
        assertNotNull(profileResponse, "Response should not be null");
        assertEquals(HttpStatus.NOT_FOUND, profileResponse.getStatusCode(), "Status code should be NOT_FOUND");

        String responseBody = profileResponse.getBody().toString();
        assertEquals(message, responseBody, "Error message should match");
    }

    // Error flow step definitions
    @Given("no user exists with username {string}")
    public void no_user_exists_with_username(String username) {
        currentUsername = username;
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
    }

    @When("I attempt to view the profile of user {string}")
    public void i_attempt_to_view_the_profile_of_user(String username) {
        i_request_to_view_the_profile_of_user(username);
    }

    @When("I attempt to view the recipes of user {string}")
    public void i_attempt_to_view_the_recipes_of_user(String username) {
        i_request_to_view_the_recipes_of_user(username);
    }

    @When("I attempt to view the favorite recipes of user {string}")
    public void i_attempt_to_view_the_favorite_recipes_of_user(String username) {
        i_request_to_view_the_favorite_recipes_of_user(username);
    }




}