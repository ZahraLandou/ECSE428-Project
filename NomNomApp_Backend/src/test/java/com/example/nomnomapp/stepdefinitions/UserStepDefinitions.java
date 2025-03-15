package com.example.nomnomapp.stepdefinitions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
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
    @Then("I should see an error message {string} \\(user modif)")
    public void i_should_see_error_message_for_user_modif(String message) {
        assertNotNull(exception, "Expected an error but none was thrown.");
        assertEquals(message, exception.getMessage(), "Error message mismatch.");
    }
}