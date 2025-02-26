package com.example.nomnomapp.stepdefinitions;

import static org.junit.jupiter.api.Assertions.*;

import io.cucumber.java.en.Then;

public class CommonStepDefinitions {

    private Exception exception;

    public CommonStepDefinitions() {}

    // This method will be shared for all step definitions that use error messages
    @Then("I should see an error message {string}")
    public void i_should_see_an_error_message(String expectedMessage) {
        assertNotNull(exception, "Expected an error but none occurred.");
        assertEquals(expectedMessage, exception.getMessage());
    }

    public void setException(Exception e) {
        this.exception = e;
    }
}
