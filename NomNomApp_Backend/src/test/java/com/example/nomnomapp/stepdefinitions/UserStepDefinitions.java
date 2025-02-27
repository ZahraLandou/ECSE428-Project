package com.example.nomnomapp.stepdefinitions;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import com.example.nomnomapp.model.NomNomUser;
import com.example.nomnomapp.service.UserService;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.*;
import com.example.nomnomapp.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;
import java.util.Optional;

@SpringBootTest
public class UserStepDefinitions {

    @Mock
    private UserRepository userRepository;  // Use Mock instead of actual repository

    @InjectMocks
    private UserService userService;

    @Autowired
    private CommonStepDefinitions commonStepDefinitions;

    private final Map<Integer, NomNomUser> userDatabase = new HashMap<>(); // Simulated user storage

    private Exception exception;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize Mocks
        userDatabase.clear();
        exception = null;
    }

    @After
    public void cleanUp() {
        userDatabase.clear();
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


    @Then("the user should be deleted successfully")
    public void the_user_should_be_deleted_successfully() {
        assertNull(commonStepDefinitions.getException(), "Exception was thrown when deletion should have succeeded.");
        verify(userRepository, times(1)).delete(any(NomNomUser.class)); // Ensure delete was called once
    }
}