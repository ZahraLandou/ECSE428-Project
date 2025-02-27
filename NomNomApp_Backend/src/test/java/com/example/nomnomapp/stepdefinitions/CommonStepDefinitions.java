package com.example.nomnomapp.stepdefinitions;

import static org.junit.jupiter.api.Assertions.*;
import org.springframework.stereotype.Component;
import io.cucumber.java.en.Then;

//@Component
public class CommonStepDefinitions {

    private Exception exception;

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
