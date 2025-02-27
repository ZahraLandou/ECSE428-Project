package com.example.nomnomapp.stepdefinitions;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.nomnomapp.model.NomNomUser;
import com.example.nomnomapp.service.UserService;
import com.example.nomnomapp.repository.UserRepository;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;
import java.util.Optional;

@SpringBootTest
public class CreateUserStepDefinitions {

    @Mock
    private UserRepository userRepository; // Mock repository

    @InjectMocks
    private UserService userService;

    @Autowired
    private CommonStepDefinitions commonStepDefinitions;

    private final Map<String, NomNomUser> userDatabase = new HashMap<>(); // Simulated user storage

    private Exception exception;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize Mocks
        userDatabase.clear();
        commonStepDefinitions.setException(null);
    }

    @After
    public void cleanUp() {
        userDatabase.clear();
    }

    @Given("no user exists with username {string} or email {string}")
    public void no_user_exists_with_username_or_email(String username, String email) {
        assertFalse(userDatabase.containsKey(username), "User with username " + username + " already exists.");
        assertFalse(userDatabase.values().stream().anyMatch(user -> user.getEmailAddress().equals(email)), "User with email " + email + " already exists.");
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
        when(userRepository.findByEmailAddress(email)).thenReturn(Optional.empty());
    }

    @Given("a user exists with username {string} and email {string}")
    public void a_user_exists_with_username_and_email(String username, String email) {
        NomNomUser user = new NomNomUser(username, email, "password123");
        userDatabase.put(username, user);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(userRepository.findByEmailAddress(email)).thenReturn(Optional.of(user));
    }

    @When("I attempt to register with username {string}, email {string}, and password {string}")
    public void i_attempt_to_register_with_username_email_and_password(String username, String email, String password) {
        try {
            NomNomUser user = new NomNomUser(username, email, password);
            userService.createUser(username, email, password);
        } catch (Exception e) {
            commonStepDefinitions.setException(e);
        }
    }

    @Then("a new user account should be created with username {string} and email {string}")
    public void a_new_user_account_should_be_created_with_username_and_email(String username, String email) {
        assertNull(commonStepDefinitions.getException(), "Exception was thrown when creation should have succeeded.");
        verify(userRepository, times(1)).save(any(NomNomUser.class)); // Ensure save was called once
    }

    @Then("no user should be created")
    public void no_user_should_be_created() {
        assertNotNull(commonStepDefinitions.getException(), "Exception was expected but not thrown.");
        verify(userRepository, never()).save(any(NomNomUser.class)); // Ensure save was never called
    }
}