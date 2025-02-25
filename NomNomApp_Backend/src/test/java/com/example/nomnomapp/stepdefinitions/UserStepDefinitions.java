package com.example.nomnomapp.stepdefinitions;

import static org.junit.jupiter.api.Assertions.*;

import com.example.nomnomapp.model.NomNomUser;
import com.example.nomnomapp.service.UserService;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.*;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

@SpringBootTest
public class UserStepDefinitions {

    @Autowired
    private UserService userService;

    private final Map<Integer, NomNomUser> userDatabase = new HashMap<>(); // Simulated user storage

    private Exception exception;

    @Before
    public void setUp() {
        userDatabase.clear();
        exception = null;
    }

    @After
    public void cleanUp() {
        userDatabase.clear();
    }

    // Given: Existing users in the system
    @Given("the following users exist:")
    public void the_following_users_exist(DataTable dataTable) {
        List<Map<String, String>> users = dataTable.asMaps(String.class, String.class);
        for (Map<String, String> row : users) {
            NomNomUser user = new NomNomUser(
                    row.get("username"),
                    row.get("email"),
                    "password123" // Assuming default password
            );
            int userId = Integer.parseInt(row.get("userId"));
            user.setUserId(userId);
            userDatabase.put(userId, user);
        }
    }

    // Given: A user exists with a specific ID
    @Given("a user with ID {int} exists")
    public void a_user_with_id_exists(int userId) {
        assertTrue(userDatabase.containsKey(userId), "User with ID " + userId + " does not exist.");
    }

    // Given: No user exists with a specific ID
    @Given("no user with ID {int} exists")
    public void no_user_with_id_exists(int userId) {
        assertFalse(userDatabase.containsKey(userId), "User with ID " + userId + " should not exist.");
    }

    // When: Attempt to delete a user
    @When("I delete the user with ID {int}")
    public void i_delete_the_user_with_id(int userId) {
        try {
            userService.deleteUserById(userId);
            userDatabase.remove(userId);
        } catch (Exception e) {
            this.exception = e;
        }
    }

    // Then: User should be deleted successfully
    @Then("the user should be deleted successfully")
    public void the_user_should_be_deleted_successfully() {
        assertNull(exception, "Exception was thrown when deletion should have succeeded.");
    }

    // Then: Attempting to delete a non-existent user should return an error
    @Then("I should see an error message {string}")
    public void i_should_see_an_error_message(String expectedMessage) {
        assertNotNull(exception, "Expected an error but none occurred.");
        assertEquals(expectedMessage, exception.getMessage());
    }
}
