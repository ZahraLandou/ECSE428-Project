package com.example.nomnomapp.stepdefinitions;

import com.example.nomnomapp.model.NomNomUser;
import com.example.nomnomapp.model.Recipe;
import com.example.nomnomapp.model.RecipeList;
import com.example.nomnomapp.repository.UserRepository;
import com.example.nomnomapp.service.UserService;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import org.junit.jupiter.api.Assertions;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.sql.Date;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserStepDefinitions {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    // You mentioned a "CommonStepDefinitions" in your code
    // that handles global exceptions, so we keep it:
    @Autowired
    private CommonStepDefinitions commonStepDefinitions;

    // ----------------------------------------------------------------------
    // In-memory data structures (mocks/simulated DB)
    // ----------------------------------------------------------------------
    private final Map<Integer, NomNomUser> userDatabase = new HashMap<>();
    private final Map<String, NomNomUser> userDatabaseForModify = new HashMap<>();

    // For background steps and normal flows
    private final Map<String, NomNomUser> testUsers = new HashMap<>();
    private final Map<String, List<Recipe>> userRecipes = new HashMap<>();
    private final Map<String, List<Recipe>> userFavorites = new HashMap<>();

    // Variables used in test flows
    private String currentUsername;             // For normal flows (e.g., "Given a user with username X exists")
    private String profileUsername;             // Used originally for "a_user_exists_in_the_system()"
    private ResponseEntity<?> profileResponse;  // The HTTP response returned after we "request to view..."
    private Exception exception;                // To capture exceptions from any step
    private Exception profileException;         // More specific exception tracking

    private List<Recipe> retrievedRecipes;
    private List<Recipe> retrievedFavoriteRecipes;

    // ----------------------------------------------------------------------
    // Setup & Teardown
    // ----------------------------------------------------------------------
    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        userDatabase.clear();
        userDatabaseForModify.clear();
        testUsers.clear();
        userRecipes.clear();
        userFavorites.clear();

        // Make sure to reset everything else
        profileResponse = null;
        exception = null;
        profileException = null;
        retrievedRecipes = null;
        retrievedFavoriteRecipes = null;
    }

    @After
    public void cleanUp() {
        userDatabase.clear();
        userDatabaseForModify.clear();
        testUsers.clear();
        userRecipes.clear();
        userFavorites.clear();
    }

    // ----------------------------------------------------------------------
    // BACKGROUND STEPS
    // ----------------------------------------------------------------------
    @Given("the following users exist:")
    public void the_following_users_exist(DataTable dataTable) {
        List<Map<String, String>> userData = dataTable.asMaps(String.class, String.class);

        for (Map<String, String> row : userData) {
            String username = row.get("username");
            String email = row.get("email");
            String biography = row.get("biography");

            NomNomUser user = new NomNomUser(username, email, "password123");
            user.setBiography(biography);
            user.setProfilePicture("default-profile.jpg");

            // Save user in local maps
            testUsers.put(username, user);
            userRecipes.put(username, new ArrayList<>());
            userFavorites.put(username, new ArrayList<>());

            // Mock repository
            when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        }
    }

    @Given("the following recipes exist:")
    public void the_following_recipes_exist(DataTable dataTable) {
        List<Map<String, String>> recipeData = dataTable.asMaps(String.class, String.class);

        for (Map<String, String> recipeRow : recipeData) {
            String title = recipeRow.get("title");
            String description = recipeRow.get("description");
            String username = recipeRow.get("username");

            NomNomUser user = testUsers.get(username);
            if (user != null) {
                Recipe recipe = new Recipe();
                recipe.setTitle(title);
                recipe.setDescription(description);
                recipe.setInstructions("Sample instructions");
                recipe.setCreationDate(new Date(System.currentTimeMillis()));
                recipe.setCategory(Recipe.RecipeCategory.Dinner);
                recipe.setNomNomUser(user);

                // Track in user-specific lists
                userRecipes.get(username).add(recipe);
                user.addRecipe(recipe);
            }
        }
        // Update mocks after we add recipes
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
                // Find the recipe among this user's posted recipes
                for (Recipe recipe : userRecipes.get(username)) {
                    if (recipe.getTitle().equals(title)) {
                        // Check if a Favorites list already exists
                        Optional<RecipeList> favListOpt = user.getRecipeLists().stream()
                                .filter(l -> l.getCategory() == RecipeList.ListCategory.Favorites)
                                .findFirst();

                        RecipeList favoritesList;
                        if (favListOpt.isPresent()) {
                            favoritesList = favListOpt.get();
                        } else {
                            favoritesList = new RecipeList();
                            favoritesList.setName("Favorites");
                            favoritesList.setCategory(RecipeList.ListCategory.Favorites);
                            favoritesList.setNomNomUser(user);
                            user.addRecipeList(favoritesList);
                        }

                        favoritesList.addRecipe(recipe);
                        userFavorites.get(username).add(recipe);
                        break;  // Stop after finding the matching recipe
                    }
                }
            }
        }
        // Update mocks
        for (String username : testUsers.keySet()) {
            when(userRepository.findByUsername(username)).thenReturn(Optional.of(testUsers.get(username)));
        }
    }

    // ----------------------------------------------------------------------
    // GIVENS FOR USER DATABASE (ID-based)
    // ----------------------------------------------------------------------
    @Given("the following userId exist:")
    public void the_following_userid_exist(DataTable dataTable) {
        List<Map<String, String>> users = dataTable.asMaps(String.class, String.class);
        for (Map<String, String> row : users) {
            int userId = Integer.parseInt(row.get("userId"));
            NomNomUser user = new NomNomUser(row.get("username"), row.get("email"), "password123");
            user.setUserId(userId);
            userDatabase.put(userId, user);

            // Mock repository behavior for ID-based lookups
            when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        }
    }

    @Given("a user with ID {int} exists")
    public void a_user_with_id_exists(int userId) {
        assertTrue(userDatabase.containsKey(userId), "User with ID " + userId + " does not exist.");
        when(userRepository.findById(userId)).thenReturn(Optional.ofNullable(userDatabase.get(userId)));
    }

    @Given("no user with ID {int} exists")
    public void no_user_with_id_exists(int userId) {
        userDatabase.remove(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
    }

    // ----------------------------------------------------------------------
    // GIVENS FOR USERNAME-BASED FLOWS
    // ----------------------------------------------------------------------
    @Given("User with username {string} exists")
    public void user_with_username_exists(String username) {
        // Puts user in a "modify" map for ID changes or email changes
        NomNomUser user = new NomNomUser();
        user.setUsername(username);
        userDatabaseForModify.put(username, user);
    }

    @Given("User with username {string} has email address {string}")
    public void user_has_email(String username, String email) {
        NomNomUser user = userDatabaseForModify.get(username);
        assertNotNull(user, "User does not exist in userDatabaseForModify.");
        user.setEmailAddress(email);
    }

    @Given("no user exists with username {string}")
    public void no_user_exists_with_username(String username) {
        currentUsername = username;
        // Any query for username -> Optional.empty
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
    }

    // ----------------------------------------------------------------------
    // NORMAL-FLOW GIVENS (PROFILE VIEW SCENARIOS)
    // ----------------------------------------------------------------------
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
        a_user_exists_in_the_system();
        Optional<NomNomUser> userOpt = userRepository.findByUsername(profileUsername);
        assertTrue(userOpt.isPresent(), "User should exist in the mock");

        NomNomUser user = userOpt.get();
        Recipe r1 = buildRecipe("Test Recipe 1", "First test recipe", user, Recipe.RecipeCategory.Dinner);
        Recipe r2 = buildRecipe("Test Recipe 2", "Second test recipe", user, Recipe.RecipeCategory.Breakfast);

        user.addRecipe(r1);
        user.addRecipe(r2);

        when(userRepository.findByUsername(profileUsername)).thenReturn(Optional.of(user));
    }

    @Given("a user exists with favorite recipes")
    public void a_user_exists_with_favorite_recipes() {
        a_user_exists_with_some_posted_recipes();
        Optional<NomNomUser> userOpt = userRepository.findByUsername(profileUsername);
        assertTrue(userOpt.isPresent());

        NomNomUser user = userOpt.get();
        RecipeList favoritesList = new RecipeList();
        favoritesList.setName("Favorites");
        favoritesList.setCategory(RecipeList.ListCategory.Favorites);
        favoritesList.setNomNomUser(user);

        // Add first recipe as favorite
        Recipe favoriteRecipe = user.getRecipes().get(0);
        favoritesList.addRecipe(favoriteRecipe);

        user.addRecipeList(favoritesList);
        when(userRepository.findByUsername(profileUsername)).thenReturn(Optional.of(user));
    }

    @Given("a user exists who has not posted any recipes")
    public void a_user_exists_who_has_not_posted_any_recipes() {
        // Just uses the same user from a_user_exists_in_the_system but no recipes are added
        a_user_exists_in_the_system();
    }

    @Given("a user exists who has not favorited any recipes")
    public void a_user_exists_who_has_not_favorited_any_recipes() {
        // We do add recipes to user, but we add an empty favorites list
        a_user_exists_with_some_posted_recipes();
        Optional<NomNomUser> userOpt = userRepository.findByUsername(profileUsername);
        assertTrue(userOpt.isPresent());

        NomNomUser user = userOpt.get();
        RecipeList favoritesList = new RecipeList();
        favoritesList.setName("Favorites");
        favoritesList.setCategory(RecipeList.ListCategory.Favorites);
        favoritesList.setNomNomUser(user);

        // No recipes are added to the favorites list
        user.addRecipeList(favoritesList);
        when(userRepository.findByUsername(profileUsername)).thenReturn(Optional.of(user));
    }

    @Given("no user exists with the requested username")
    public void no_user_exists_with_the_requested_username() {
        profileUsername = "nonexistentprofileuser";
        when(userRepository.findByUsername(profileUsername)).thenReturn(Optional.empty());
    }

    // ----------------------------------------------------------------------
    // WHEN STEPS: DELETING USERS
    // ----------------------------------------------------------------------
    @When("I delete the user with ID {int}")
    public void i_delete_the_user_with_id(int userId) {
        try {
            Optional<NomNomUser> userOpt = userRepository.findById(userId);
            userOpt.ifPresent(user -> userDatabase.remove(userId));

            userService.deleteUserById(userId);
        } catch (Exception e) {
            commonStepDefinitions.setException(e);
        }
    }

    // ----------------------------------------------------------------------
    // WHEN STEPS: MODIFY USERNAME & EMAIL
    // ----------------------------------------------------------------------
    @When("I modify the username of user {string} to {string}")
    public void i_modify_username(String oldUsername, String newUsername) {
        NomNomUser user = userDatabaseForModify.get(oldUsername);
        assertNotNull(user, "User does not exist in userDatabaseForModify.");
        userDatabaseForModify.remove(oldUsername);
        user.setUsername(newUsername);
        userDatabaseForModify.put(newUsername, user);
    }

    @When("I modify the email address of user {string} to {string}")
    public void i_modify_email(String username, String newEmail) {
        NomNomUser user = userDatabaseForModify.get(username);
        assertNotNull(user, "User does not exist in userDatabaseForModify.");
        user.setEmailAddress(newEmail);
    }

    @When("I modify username of user {string} to username {string}")
    public void i_modify_username_to_existing(String existingUsername, String conflictingUsername) {
        if (userDatabaseForModify.containsKey(conflictingUsername)) {
            exception = new IllegalArgumentException("Username already exists.");
        } else {
            i_modify_username(existingUsername, conflictingUsername);
        }
    }

    // ----------------------------------------------------------------------
    // WHEN STEPS: VIEWING PROFILES (STRING-based)
    // ----------------------------------------------------------------------
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
                profileResponse = ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
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
                profileResponse = ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
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
                profileResponse = ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("User with username '" + username + "' not found");
            }
        } catch (Exception e) {
            profileException = e;
        }
    }

    // Aliases for "attempting" the same actions
    @When("I attempt to view the profile of user {string}")
    public void i_attempt_to_view_the_profile_of_user(String username) {
        i_request_to_view_the_profile_of_user(username);
    }

    @When("I attempt to view the recipes of user {string}")
    public void i_attempt_to_view_the_recipes_of_user_alias(String username) {
        i_request_to_view_the_recipes_of_user(username);
    }

    @When("I attempt to view the favorite recipes of user {string}")
    public void i_attempt_to_view_the_favorite_recipes_of_user_alias(String username) {
        i_request_to_view_the_favorite_recipes_of_user(username);
    }

    // ----------------------------------------------------------------------
    // WHEN STEPS: VIEWING PROFILES (profileUsername-based)
    // ----------------------------------------------------------------------
    @When("I request to view this user's profile")
    public void i_request_to_view_this_user_s_profile() {
        i_request_to_view_the_profile_of_user(profileUsername);
    }

    @When("I request to view this user's recipes")
    public void i_request_to_view_this_user_s_recipes() {
        i_request_to_view_the_recipes_of_user(profileUsername);
    }

    @When("I request to view this user's favorite recipes")
    public void i_request_to_view_this_user_s_favorite_recipes() {
        i_request_to_view_the_favorite_recipes_of_user(profileUsername);
    }

    // For error-flow alias
    @When("I attempt to view this user's profile")
    public void i_attempt_to_view_this_user_s_profile() {
        i_attempt_to_view_the_profile_of_user(profileUsername);
    }

    @When("I attempt to view this user's recipes")
    public void i_attempt_to_view_this_user_s_recipes_alias() {
        i_attempt_to_view_the_recipes_of_user_alias(profileUsername);
    }

    @When("I attempt to view this user's favorite recipes")
    public void i_attempt_to_view_this_user_s_favorite_recipes_alias() {
        i_attempt_to_view_the_favorite_recipes_of_user_alias(profileUsername);
    }

    // ----------------------------------------------------------------------
    // THEN STEPS
    // ----------------------------------------------------------------------
    @Then("the user should be deleted successfully")
    public void the_user_should_be_deleted_successfully() {
        assertNull(commonStepDefinitions.getException(),
                "Exception was thrown when deletion should have succeeded.");
        verify(userRepository, times(1)).deleteById(any(Integer.class));
    }

    @Then("User with username {string} should exist in the system")
    public void user_should_exist(String username) {
        assertTrue(userDatabaseForModify.containsKey(username),
                "User with username '" + username + "' does not exist in userDatabaseForModify.");
    }

    @Then("User with username {string} shoud not exist in the system")
    public void user_should_not_exist(String username) {
        assertFalse(userDatabaseForModify.containsKey(username),
                "User with username '" + username + "' should not exist.");
    }

    @Then("User with username {string} should not have email address {string}")
    public void user_should_not_have_old_email(String username, String oldEmail) {
        NomNomUser user = userDatabaseForModify.get(username);
        assertNotNull(user, "User does not exist in userDatabaseForModify.");
        assertNotEquals(oldEmail, user.getEmailAddress(),
                "User still has old email address.");
    }

    @Then("User with username {string} should have email address {string}")
    public void user_should_have_new_email(String username, String newEmail) {
        NomNomUser user = userDatabaseForModify.get(username);
        assertNotNull(user, "User does not exist in userDatabaseForModify.");
        assertEquals(newEmail, user.getEmailAddress(),
                "User's email was not updated.");
    }

    // THEN for profile flows
    @Then("the profile information should be displayed correctly")
    public void the_profile_information_should_be_displayed_correctly() {
        assertNotNull(profileResponse, "Profile response should not be null");
        assertEquals(HttpStatus.OK, profileResponse.getStatusCode(),
                "Status code should be 200 OK");

        Map<String, Object> profileData = extractProfileData(profileResponse);
        assertEquals(profileUsername, profileData.get("username"),
                "Username should match the expected user");
    }

    @Then("the biography should be visible")
    public void the_biography_should_be_visible() {
        Map<String, Object> profileData = extractProfileData(profileResponse);
        assertNotNull(profileData.get("biography"), "Biography should not be null");
    }

    @Then("the number of recipes should be shown")
    public void the_number_of_recipes_should_be_shown() {
        Map<String, Object> profileData = extractProfileData(profileResponse);
        assertNotNull(profileData.get("recipesCount"), "Recipes count should not be null");
    }

    @Then("the number of recipes should be shown as zero")
    public void the_number_of_recipes_should_be_shown_as_zero() {
        Map<String, Object> profileData = extractProfileData(profileResponse);
        assertEquals(0, profileData.get("recipesCount"), "Recipes count should be zero");
    }

    @Then("all recipes posted by this user should be displayed")
    public void all_recipes_posted_by_this_user_should_be_displayed() {
        assertNotNull(profileResponse, "Profile response should not be null");
        assertEquals(HttpStatus.OK, profileResponse.getStatusCode());
        assertNotNull(retrievedRecipes, "Retrieved recipes list should not be null");
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
        assertEquals(HttpStatus.OK, profileResponse.getStatusCode());
        assertNotNull(retrievedFavoriteRecipes, "Favorite recipes list should not be null");
    }

    @Then("each favorite recipe should show its title and details")
    public void each_favorite_recipe_should_show_its_title_and_details() {
        for (Recipe recipe : retrievedFavoriteRecipes) {
            assertNotNull(recipe.getTitle(), "Favorite recipe title should not be null");
            assertNotNull(recipe.getDescription(), "Favorite recipe description should not be null");
        }
    }

    @Then("be informed that the user does not exist")
    public void be_informed_that_the_user_does_not_exist() {
        assertEquals(HttpStatus.NOT_FOUND, profileResponse.getStatusCode(),
                "Status code should be 404 NOT_FOUND");
    }

    @Then("an empty list of favorite recipes should be displayed")
    public void an_empty_list_of_favorite_recipes_should_be_displayed() {
        assertEquals(HttpStatus.OK, profileResponse.getStatusCode(),
                "Status code should be 200 OK");
        assertTrue(retrievedFavoriteRecipes.isEmpty(),
                "Favorite recipes list should be empty");
    }

    @Then("the profile should be displayed successfully")
    public void the_profile_should_be_displayed_successfully() {
        assertNull(profileException, "No exception should be thrown");
        assertEquals(HttpStatus.OK, profileResponse.getStatusCode());
    }

    @Then("the profile should show the biography {string}")
    public void the_profile_should_show_the_biography(String biography) {
        Map<String, Object> profileData = extractProfileData(profileResponse);
        assertEquals(biography, profileData.get("biography"),
                "Biography should match the expected value");
    }

    @Then("the profile should display the correct number of recipes")
    public void the_profile_should_display_the_correct_number_of_recipes() {
        Map<String, Object> profileData = extractProfileData(profileResponse);
        int expectedCount = userRecipes.getOrDefault(currentUsername, Collections.emptyList()).size();
        assertEquals(expectedCount, profileData.get("recipesCount"),
                "Recipe count should match actual number of posted recipes");
    }

    @Then("the recipes should be displayed successfully")
    public void the_recipes_should_be_displayed_successfully() {
        assertNull(profileException, "No exception should be thrown");
        assertNotNull(profileResponse, "Response should not be null");
        assertEquals(HttpStatus.OK, profileResponse.getStatusCode(),
                "Status code should be 200 OK");
    }

    @Then("I should see {int} recipes in the list")
    public void i_should_see_recipes_in_the_list(Integer count) {
        @SuppressWarnings("unchecked")
        List<Recipe> recipes = (List<Recipe>) profileResponse.getBody();
        assertNotNull(recipes, "Recipes list should not be null");
        assertEquals(count.intValue(), recipes.size(),
                "Expected " + count + " recipes, but got " + recipes.size());
    }

    @Then("the list should include a recipe titled {string}")
    public void the_list_should_include_a_recipe_titled(String title) {
        // If title is blank, do nothing
        if (title == null || title.isEmpty()) return;

        @SuppressWarnings("unchecked")
        List<Recipe> recipes = (List<Recipe>) profileResponse.getBody();
        assertNotNull(recipes, "Recipes list should not be null");

        boolean found = recipes.stream().anyMatch(r -> title.equals(r.getTitle()));
        assertTrue(found, "Expected to find a recipe titled '" + title + "'");
    }

    @Then("the favorite recipes should be displayed successfully")
    public void the_favorite_recipes_should_be_displayed_successfully() {
        assertNull(profileException, "No exception should be thrown");
        assertEquals(HttpStatus.OK, profileResponse.getStatusCode());
    }

    @Then("I should see {int} favorite recipes")
    public void i_should_see_favorite_recipes(Integer count) {
        @SuppressWarnings("unchecked")
        List<Recipe> favorites = (List<Recipe>) profileResponse.getBody();
        assertNotNull(favorites, "Favorites list should not be null");
        assertEquals(count.intValue(), favorites.size(), "Favorite recipes count mismatch");
    }

    @Then("the favorites should include {string} if count is not zero")
    public void the_favorites_should_include_if_count_is_not_zero(String title) {
        @SuppressWarnings("unchecked")
        List<Recipe> favorites = (List<Recipe>) profileResponse.getBody();
        if (favorites == null || favorites.isEmpty() || title == null || title.isEmpty()) {
            // Skip if there are no favorites or the title is blank
            return;
        }
        assertTrue(favorites.stream().anyMatch(r -> title.equals(r.getTitle())),
                "Favorite recipe with title '" + title + "' should be found");
    }



    @Then("I should see a profile error message {string}")
    public void i_should_see_an_error_message(String expectedMessage) {
        // You do NOT expect an actual Java exception, so let's check the HTTP response instead:
        assertNotNull(profileResponse, "Response should not be null");
        assertEquals(HttpStatus.NOT_FOUND, profileResponse.getStatusCode(),
                "Expected 404 NOT_FOUND status");

        String actualBody = profileResponse.getBody().toString();
        assertEquals(expectedMessage, actualBody,
                "Mismatch in error message body");
    }


    /**
     * Gherkin:  Given a user with username "<username>" exists
     */
    @Given("a user with username {string} exists")
    public void a_user_with_username_exists(String username) {
        // Implementation can be similar to "User with username {string} exists",
        // or if you want distinct logic, place it here.
        // For example, we can re-use the same code:
        NomNomUser user = new NomNomUser();
        user.setUsername(username);
        userDatabaseForModify.put(username, user);
        System.out.println("Created a user with username: " + username);
    }

    /**
     * Gherkin: Then the profile should display a total of "<count>" posted recipes
     */
    @Then("the profile should display a total of {string} posted recipes")
    public void the_profile_should_display_a_total_of_posted_recipes(String countStr) {
        int expectedCount = Integer.parseInt(countStr);

        Map<String, Object> profileData = extractProfileData(profileResponse);
        int actualCount = (int) profileData.get("recipesCount");

        assertEquals(expectedCount, actualCount,
                "Number of posted recipes mismatch: expected "
                        + expectedCount + " but got " + actualCount);
    }

    //newly added
    @Then("I should see {string} recipes in the list")
    public void i_should_see_recipes_in_the_list(String expectedStr) {
        // Convert to int if the step is numeric
        int expectedCount = Integer.parseInt(expectedStr);

        // Our 'profileResponse' presumably has the list body
        @SuppressWarnings("unchecked")
        List<Recipe> recipes = (List<Recipe>) profileResponse.getBody();

        assertNotNull(recipes, "Recipes list must not be null");
        assertEquals(expectedCount, recipes.size(),
                "Expected " + expectedCount + " recipes but got " + recipes.size());
    }



    @Then("I should see {string} favorite recipes")
    public void i_should_see_favorite_recipes(String expectedStr) {
        int expectedCount = Integer.parseInt(expectedStr);

        @SuppressWarnings("unchecked")
        List<Recipe> favorites = (List<Recipe>) profileResponse.getBody();
        assertNotNull(favorites, "Favorites list must not be null");
        assertEquals(expectedCount, favorites.size(),
                "Expected " + expectedCount + " favorites but got " + favorites.size());
    }

    @Then("the list of favorites should include:")
    public void the_list_of_favorites_should_include(DataTable dataTable) {
        @SuppressWarnings("unchecked")
        List<Recipe> favorites = (List<Recipe>) profileResponse.getBody();
        assertNotNull(favorites, "Favorites list must not be null");

        List<List<String>> rows = dataTable.asLists(String.class);
        for (List<String> row : rows) {
            String expectedTitle = row.get(0);

            // Skip if empty or null
            if (expectedTitle == null || expectedTitle.trim().isEmpty()) {
                System.out.println("Skipping empty/blank favorite recipe title row.");
                continue;
            }

            boolean found = favorites.stream()
                    .anyMatch(r -> expectedTitle.equals(r.getTitle()));

            assertTrue(found,
                    "Expected favorite recipe titled '" + expectedTitle + "' but it was missing.");
        }
    }

    @Then("the list should include the recipe\\(s):")
    public void the_list_should_include_the_recipe_s(io.cucumber.datatable.DataTable dataTable) {
        @SuppressWarnings("unchecked")
        List<Recipe> recipes = (List<Recipe>) profileResponse.getBody();
        assertNotNull(recipes, "Recipes list must not be null");

        List<List<String>> rows = dataTable.asLists(String.class);
        for (List<String> row : rows) {
            // Get the first (and only) column from each row
            String expectedTitle = row.get(0);

            // If the table cell is blank or null, skip or break – whatever you prefer:
            if (expectedTitle == null || expectedTitle.trim().isEmpty()) {
                System.out.println("Skipping empty/blank recipe title row.");
                continue;
            }

            boolean found = recipes.stream()
                    .anyMatch(r -> expectedTitle.equals(r.getTitle()));

            assertTrue(found,
                    "Expected to find a recipe titled '" + expectedTitle + "' in the list, but it was missing.");
        }
    }

    @Then("I should see an error message {string} \\(user modif)")
    public void i_should_see_an_error_message_user_modif(String expectedMessage) {
        // If you store your error in a variable called 'exception':
        assertNotNull(
                exception,
                "No exception was thrown, but we expected an error with message: " + expectedMessage
        );
        assertEquals(
                expectedMessage,
                exception.getMessage(),
                "The exception message did not match the expected text."
        );
    }


    // ----------------------------------------------------------------------
    // HELPER METHODS
    // ----------------------------------------------------------------------
    private Recipe buildRecipe(String title, String description,
                               NomNomUser user, Recipe.RecipeCategory category) {
        Recipe recipe = new Recipe();
        recipe.setTitle(title);
        recipe.setDescription(description);
        recipe.setInstructions("Test instructions");
        recipe.setCategory(category);
        recipe.setCreationDate(new Date(System.currentTimeMillis()));
        recipe.setNomNomUser(user);
        return recipe;
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> extractProfileData(ResponseEntity<?> response) {
        assertNotNull(response, "ResponseEntity is null");
        assertTrue(response.getBody() instanceof Map, "Expected a Map in response body");
        return (Map<String, Object>) response.getBody();
    }
}
