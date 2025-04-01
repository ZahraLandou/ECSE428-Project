package com.example.nomnomapp.stepdefinitions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import com.example.nomnomapp.controller.RatingController;
import com.example.nomnomapp.model.NomNomUser;
import com.example.nomnomapp.model.Rating;
import com.example.nomnomapp.model.Recipe;
import com.example.nomnomapp.repository.UserRepository;
import com.example.nomnomapp.service.RatingService;
import io.cucumber.java.en.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public class RatingStepDefinitions {

    private RatingController ratingController;
    private UserRepository userRepository;
    private RatingService ratingService;
    private Recipe testRecipe;
    private NomNomUser testUser;
    private ResponseEntity<?> response;
    private Rating existingRating;
    private Rating rating;
    public RatingStepDefinitions() {
        this.ratingService = mock(RatingService.class); // Initialize mock service
        userRepository = mock(UserRepository.class);
        this.ratingController = new RatingController(ratingService, userRepository); //Inject into controller
    }

    @Given("I am logged in and on a recipe's page")
    public void iAmLoggedInAndOnARecipePage() {
        ratingService = mock(RatingService.class);
        userRepository = mock(UserRepository.class);
        ratingController = new RatingController(ratingService, userRepository);
        testRecipe = new Recipe();
        testRecipe.setRecipeID(1);
        testUser = new NomNomUser("testUser", "test@example.com", "password");
        testUser.setUserId(123);
        when(userRepository.existsById(testUser.getUserId())).thenReturn(true);
    }

    @When("I select a rating between {int} and {int} stars and I submit my rating")
    public void iSubmitRating(int min, int max) {
        int ratingValue = (min + max) / 2;
        when(ratingService.rateRecipe(testUser.getUserId(), testRecipe.getRecipeId(), ratingValue))
                .thenReturn(new Rating(testUser, testRecipe, ratingValue));

        response = ratingController.rateRecipe(testUser.getUserId(), testRecipe.getRecipeId(), ratingValue);
    }

    @Then("the rating should be saved")
    public void theRatingShouldBeSaved() {
        //assertEquals(200, response.getStatusCodeValue());
        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
    }

    @Then("the average rating and number of ratings for the recipe should be updated and displayed accordingly on the page")
    public void averageRatingShouldBeUpdated() {
        double expectedAverage = 5.0;
        testRecipe.getRatings().add(new Rating(testUser, testRecipe, 5));
        double actualAverage = testRecipe.calculateAverageRatingForRating();

        // Debugging
        System.out.println("Ratings count: " + testRecipe.getRatings().size());
        System.out.println("Calculated average: " + actualAverage);

        assertEquals(expectedAverage, actualAverage);
    }

    @Given("I am logged in and have already rated a recipe")
    public void iHaveAlreadyRatedARecipe() {
        testUser = new NomNomUser("testUser", "test@example.com", "password");
        testUser.setUserId(1);
        testRecipe = new Recipe();
        testRecipe.setRecipeID(1);
        existingRating = new Rating(testUser, testRecipe, 3);
        when(ratingService.findUserRating(testUser.getUserId(), testRecipe.getRecipeId()))
                .thenReturn(Optional.of(existingRating));
        System.out.println("Existing rating setup complete.");
    }

    @When("I change my rating to a new value and submit my new rating")
    public void iChangeMyRatingAndSubmit() {
        int newRatingValue = 4;  // User updates rating to 4 stars
        when(ratingService.updateRating(testUser.getUserId(), testRecipe.getRecipeId(), newRatingValue))
                .thenReturn(new Rating(testUser, testRecipe, newRatingValue));

        response = ratingController.updateRating(testUser.getUserId(), testRecipe.getRecipeId(), newRatingValue);
    }

    @Then("my previous rating should be replaced with the new one")
    public void previousRatingReplaced() {
        assertEquals(200, response.getStatusCodeValue());
    }

    @Given("I am not logged in and on a recipe's page")
    public void iAmNotLoggedIn() {
        testUser = null;
        testRecipe = new Recipe();
        testRecipe.setRecipeID(1);
    }

    @When("I select a rating and try to submit my rating for the recipe")
    public void iTryToSubmitRating() {
        response = ratingController.rateRecipe(0, testRecipe.getRecipeId(), 5);
    }

    @Then("the rating should not be saved")
    public void ratingNotSaved() {
        assertEquals(401, response.getStatusCodeValue());
        assertEquals("Unauthorized: You must be logged in to rate recipes.", response.getBody());
    }

    @Then("the error message {string} should be displayed")
    public void errorMessageDisplayed(String expectedMessage) {
        String actualMessage = response.getBody().toString();
        assertEquals("Unauthorized: You must be logged in to rate recipes.", actualMessage);
    }

}
