package com.example.nomnomapp.stepdefinitions;

import static org.junit.jupiter.api.Assertions.*;

import com.example.nomnomapp.model.NomNomUser;
import com.example.nomnomapp.repository.UserRepository;
import com.example.nomnomapp.service.UserService;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import io.cucumber.java.en.Then;

import java.util.List;
import java.util.Map;

//@Component
public class CommonStepDefinitions {

    private Exception exception;
    private UserService userService;
    private UserRepository userRepo;

    public CommonStepDefinitions() {}

    // This method will be shared for all step definitions that use error messages
    @Then("I should see an error message {string}")
    public void i_should_see_an_error_message(String expectedMessage) {
        assertNotNull(this.exception, "Expected an error but none occurred.");
        assertEquals(expectedMessage, this.exception.getMessage());

    }

    public void setException(Exception e) {
        this.exception = e;
    }

    public Exception getException() {
        return this.exception;
    }

}
