//package com.example.nomnomapp.stepdefinitions;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//import com.example.nomnomapp.model.NomNomUser;
//import com.example.nomnomapp.service.UserService;
//import io.cucumber.java.en.Given;
//import io.cucumber.java.en.Then;
//import io.cucumber.java.en.When;
//import io.cucumber.spring.CucumberContextConfiguration;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import com.example.nomnomapp.repository.UserRepository;
//
//import java.util.Optional;
//
//@SpringBootTest
//public class CreateUserStepDefinitions {
//
//    @Autowired
//    private UserService userService;
//
//    @Autowired
//    private CommonStepDefinitions commonStepDefinitions;
//
//    @Autowired
//    private UserRepository userRepository;
//    private NomNomUser createdUser;
//
//    @Given("no user exists with username {string} or email {string}")
//    public void no_user_exists_with_username_or_email(String username, String email) {
//        Optional<NomNomUser> existingUserByUsername = userRepository.findByUsername(username);
//        assertFalse(existingUserByUsername.isPresent(), "User with username " + username + " already exists.");
//        Optional<NomNomUser> existingUserByEmail = userRepository.findByEmailAddress(email);
//        assertFalse(existingUserByEmail.isPresent(), "User with email " + email + " already exists.");
//    }
//    @Given("a user exists with username {string} and email {string}")
//    public void a_user_exists_with_username_and_email(String username, String email) {
//        Optional<NomNomUser> existingUserByUsername = userRepository.findByUsername(username);
//        assertTrue(existingUserByUsername.isPresent(), "User with username " + username + " should exist.");
//        Optional<NomNomUser> existingUserByEmail = userRepository.findByEmailAddress(email);
//        assertTrue(existingUserByEmail.isPresent(), "User with email " + email + " should exist.");
//    }
//
//
//    @When("I attempt to register with username {string}, email {string}, and password {string}")
//    public void i_attempt_to_register_with_username_email_and_password(String username, String email, String password) {
//        try {
//            createdUser = userService.createUser(username, email, password);
//        } catch (Exception e) {
//            commonStepDefinitions.setException(e);
//        }
//    }
//
//    @Then("a new user account should be created with username {string} and email {string}")
//    public void a_new_user_account_should_be_created_with_username_and_email(String username, String email) {
//        Optional<NomNomUser> user = userRepository.findByUsername(username);
//        assertTrue(user.isPresent());
//        assertEquals(username, user.get().getUsername());
//        assertEquals(email, user.get().getEmailAddress());
//    }
//
//    @Then("I should see an error message {string}")
//    public void i_should_see_an_error_message(String expectedMessage) {
//        assertNotNull(commonStepDefinitions.getException(), "Expected an error but none occurred.");
//        assertEquals(expectedMessage, commonStepDefinitions.getException().getMessage());
//    }
//}