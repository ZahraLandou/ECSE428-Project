 package com.example.nomnomapp.stepdefinitions;

import static org.junit.jupiter.api.Assertions.*;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDate;
import java.util.*;

import com.example.nomnomapp.exception.NomNomException;
import com.example.nomnomapp.model.Comment;
import com.example.nomnomapp.model.Ingredient;
import com.example.nomnomapp.model.NomNomUser;
import com.example.nomnomapp.model.Recipe;
import com.example.nomnomapp.repository.CommentRepository;
import com.example.nomnomapp.repository.RecipeRepository;
import com.example.nomnomapp.repository.UserRepository;
import com.example.nomnomapp.service.CommentService;
import com.example.nomnomapp.service.RecipeService;
import com.example.nomnomapp.service.UserService;
import io.cucumber.datatable.DataTable;


import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.*;

import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Date;

@Transactional
public class CommentStepDefinitions {

    @Autowired
    private CommentService commentService;
    @Autowired
    private RecipeService recipeService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private CommentRepository commentRepo;
    @Autowired
    private RecipeRepository recipeRepo;

    private Comment createdComment;
    private String error;
    private final Map<String, Ingredient> commentDatabase = new HashMap<>();


    @Before
    public void setup() {
        commentService.deleteAllComments();
        recipeService.deleteAllRecipes();
        userService.deleteAllUsers();
 /* */
        commentDatabase.clear();
    }

    @After
    public void tearDown() {
        commentDatabase.clear();

    }

    @Given("the following users exist in the system")
    public void the_following_users_exist(DataTable dataTable) {
        List<Map<String, String>> users = dataTable.asMaps(String.class,String.class);
        for (Map<String, String> row : users) {
            NomNomUser user = userService.createUser(row.get("username"), row.get("emailAddress"), row.get("password"));
            userRepo.save(user);
            //userRepo.save(user);

        }
    }

   @Given("the following recipe exists in the system")
    public void the_following_recipe_exists(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps();
        for (Map<String, String> row : rows) {
            Recipe recipe = new Recipe();
            recipe.setTitle(row.get("title"));
            recipe.setInstructions(row.get("instructions"));
            recipe.setDescription(row.get("description"));
            recipe.setNomNomUser(userService.getNomNomUserByName(row.get("recipeNomNomUser")));
            recipe.setCategory(Recipe.RecipeCategory.valueOf(row.get("category")));
            recipe.setLikes(Integer.parseInt(row.get("likes")));
            recipe.setAverageRating(Double.parseDouble(row.get("averageRating")));
            recipe.setCreationDate(Date.valueOf(LocalDate.now()));

            recipeService.createRecipe(recipe);


   /*         Recipe recipe = new Recipe(row.get("title"),
                    row.get("description"),
                    row.get("instructions"),
                    Date.valueOf(row.get("creationDate")),
                    Recipe.RecipeCategory.valueOf(row.get("category")),
                    Integer.parseInt(row.get("likes")),
                    row.get("picture"),
                    Double.parseDouble(row.get("averageRating")),
                    userRepo.findByUsername(row.get("recipeNomNomUser")).get());
            recipeService.createRecipe(recipe);
            recipeRepo.save(recipe); // Ensure persistence*/
        }
    }

    @When("user with username {string} adds a new comment with rating {string} and content {string} to an existing recipe {string}")
    public void user_adds_comment(String username, String rating, String commentContent, String recipeTitle) {
        try {
            error = null; // Reset error before execution
            NomNomUser user = userService.getNomNomUserByName(username);
            Recipe recipe = recipeRepo.findRecipeByTitle(recipeTitle).get(0);

            createdComment = commentService.createComment(
                    commentContent,
                    Double.parseDouble(rating),
                    user,
                    recipe);
        } catch (Exception e) {
            error = e.getMessage();
        }
    }

    @Then("the rating of the recipe {string} should be {string}")
    public void the_rating_should_be_updated(String recipeTitle, String expectedRating) {
        Recipe recipe = recipeRepo.findRecipeByTitle(recipeTitle).get(0);
        assertNotNull(recipe);
        assertEquals(Double.parseDouble(expectedRating), recipe.getAverageRating(), 0.01);
    }

    @Then("the number of comments for recipe {string} in the system shall be {string}")
    public void the_number_of_comments_should_be(String recipeTitle, String expectedNumber) {
        Recipe recipe = recipeRepo.findRecipeByTitle(recipeTitle).get(0);
        assertNotNull(recipe);
        assertEquals(Integer.parseInt(expectedNumber), recipe.getComments().size());
    }


    @When("user with username {string} attempts to add a new comment with rating {string} and content {string} to an existing recipe {string}")
    //@Transactional(noRollbackFor = NomNomException.class)
    public void user_attempts_to_add_comment(String username, String ratingStr, String commentContent, String recipeTitle) {
        try {
            double rating = Double.parseDouble(ratingStr);
            NomNomUser user = userService.getNomNomUserByName(username);
            Recipe recipe = recipeRepo.findRecipeByTitle(recipeTitle).get(0);

            createdComment = commentService.createComment(commentContent, rating, user, recipe);
        } catch (Exception e) {
            error = e.getMessage();
        }
    }




    @Then("the comment created by user {string} for recipe {string},  content {string}, and rating {string} shall exist in the system ")
    public void comment_should_exist(String username, String recipeTitle, String commentContent, String rating) {
        NomNomUser user = userRepo.findByUsername(username).get();
        for (Comment c : user.getComments()) {
            if (c.getRecipe().getTitle().equals(recipeTitle)) {
                assertNotNull(c);
                assertEquals(username, c.getNomNomUser().getUsername());
                assertEquals(commentContent, c.getCommentContent());
                assertEquals(Double.parseDouble(rating), c.getRating(), 0.01);
                break;
            }
        }
    }

    @Then("the comment created by user {string} for recipe {string}, content {string}, and rating {string} shall exist in the system")
    public void the_comment_should_exist(String username, String recipeTitle, String commentContent, String ratingStr) {
        double rating = Double.parseDouble(ratingStr);
        NomNomUser user = userService.getNomNomUserByName(username);
        Recipe recipe = recipeRepo.findRecipeByTitle(recipeTitle).get(0);

        Optional<Comment> comment = commentRepo.findByCommentContentAndNomNomUserAndRecipe(commentContent, user, recipe);

        assertTrue(comment.isPresent(), "The expected comment does not exist in the system");
        assertEquals(rating, comment.get().getRating(), 0.01, "The comment's rating is incorrect");
    }

    @Then("an error should be returned with message {string}")
    public void an_error_should_be_returned_with_message(String expectedError) {
        assertNotNull(error, "Expected an error but none was thrown");
        assertEquals(expectedError, error, "The error message is incorrect");
    }


/*  
    @Then("the rating of the recipe {string} should be {string}")
    public void the_rating_of_the_recipe_should_be(String recipeTitle, String expectedRatingStr) {
        Recipe recipe = recipeRepo.findRecipeByTitle(recipeTitle).get(0);
        double expectedRating = Double.parseDouble(expectedRatingStr);
        assertEquals(expectedRating, recipe.getAverageRating(), 0.01, "The recipe's average rating is incorrect");
    }

    @Then("the number of comments for recipe {string} in the system shall be {string}")
    public void the_number_of_comments_for_recipe_should_be(String recipeTitle, String expectedNumberStr) {
        Recipe recipe = recipeRepo.findRecipeByTitle(recipeTitle).get(0);
        long expectedNumber = Long.parseLong(expectedNumberStr);
        long actualNumber = commentRepo.countByRecipe(recipe);
        assertEquals(expectedNumber, actualNumber, "The number of comments for the recipe is incorrect");
    }







    @Then("an error should be returned with message {string}")
    public void error_should_be_returned(String expectedMessage) {
        assertNotNull(error);
        assertEquals(expectedMessage, error);
    }

    @When("the user {string} updates the comment of {string} with new content {string} and rating {string}")
    public void update_comment(String username, String commentContent, String recipeTitle, String rating) {
        NomNomUser user = userRepo.findByUsername(username).get();
        try {
            error = null;
            for (Comment c : user.getComments()) {
                if (c.getRecipe().getTitle().equals(recipeTitle)) {
                    c.setCommentContent(commentContent);
                    c.setRating(Double.parseDouble(rating));
                    commentRepo.save(c);
                    break;
                }
            }

        } catch (Exception e) {
            error = e.getMessage();
        }
    }

    @Then("the comment should be updated successfully")
    public void verify_comment_update() {
        Comment updatedComment = commentRepo.findCommentByCommentId(createdComment.getCommentId());
        assertNotNull(updatedComment);
        assertEquals(createdComment.getCommentContent(), updatedComment.getCommentContent());
        assertEquals(createdComment.getRating(), updatedComment.getRating(), 0.01);
    }

    @Then("the recipe's average rating should be updated")
    public void verify_recipe_rating_update() {
        Recipe recipe = recipeRepo.findByRecipeId(createdComment.getRecipe().getRecipeID());
        assertNotNull(recipe);
        assertEquals(recipe.calculateAverageRating(), recipe.getAverageRating(), 0.01);
    }

    @When("the user deletes the comment")
    public void delete_comment() {
        try {
            error = null;
            commentService.deleteComment(createdComment.getCommentId());
        } catch (Exception e) {
            error = e.getMessage();
        }
    }

    @Then("the comment should be removed from the system")
    public void verify_comment_deleted() {
        assertNull(commentRepo.findCommentByCommentId(createdComment.getCommentId()));
    }*/
}
 