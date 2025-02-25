package com.example.nomnomapp.features;

import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

import com.example.nomnomapp.model.Comment;
import com.example.nomnomapp.model.NomNomUser;
import com.example.nomnomapp.model.Recipe;
import com.example.nomnomapp.repository.CommentRepository;
import com.example.nomnomapp.repository.RecipeRepository;
import com.example.nomnomapp.repository.UserRepository;
import com.example.nomnomapp.service.CommentService;
import com.example.nomnomapp.service.RecipeService;
import com.example.nomnomapp.service.UserService;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.*;
import jakarta.transaction.Transactional;

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
    @Before
    public void setup(){
        commentRepo.deleteAll();
        recipeRepo.deleteAll();
        userRepo.deleteAll();
    }
    @After
    public void tearDown(){
        commentRepo.deleteAll();
        recipeRepo.deleteAll();
        userRepo.deleteAll();
    }

    @Given("the following Users exist in the system")
    public void the_following_users_exist(io.cucumber.datatable.DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps();
        for (Map<String, String> row : rows) {
            NomNomUser user = userService.createUser(row.get("username"), row.get("emailAddress"), row.get("password"));
            
            userRepo.save(user);
        }
    }

    @Given("the following Recipe exists in the system")
    public void the_following_recipe_exists(io.cucumber.datatable.DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps();
        for (Map<String, String> row : rows) {
            Recipe recipe = new Recipe(row.get("title"),
            row.get("description"),
            row.get("instructions"),
            Date.valueOf(row.get("creationDate")),
            Recipe.RecipeCategory.valueOf(row.get("category")),
            Integer.parseInt(row.get("likes")),
            row.get("picture"),
            Double.parseDouble(row.get("averageRating")),
            userRepo.findByUsername(row.get("recipeNomNomUser")).get());
            recipeService.createRecipe(recipe);
            recipeRepo.save(recipe); // Ensure persistence
        }
    }

    @When("user with username {string} attempts to add a new comment with rating {string} and content {string} to an existing recipe {string}")
    public void user_adds_comment(String username, String rating, String commentContent, String recipeTitle) {
        try {
            error = null; // Reset error before execution

            createdComment = commentService.createComment(
                commentContent,
                Double.valueOf(rating),
                userRepo.findByUsername(username),
                recipeRepo.findRecipeByTitle(recipeTitle).get(0)
            );
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

    @Then("the comment created by user {string} for recipe {string},  content {string}, and rating {string} shall exist in the system ")
    public void comment_should_exist( String username, String recipeTitle, String commentContent, String rating) {
        NomNomUser user = userRepo.findByUsername(username).get();
        for (Comment c: user.getComments()){
            if (c.getRecipe().getTitle().equals(recipeTitle)){
                assertNotNull(c);
        assertEquals(username, c.getNomNomUser().getUsername());
        assertEquals(commentContent, c.getCommentContent());
        assertEquals(Double.parseDouble(rating), c.getRating(), 0.01);
        break;
            }
        }
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
            for (Comment c: user.getComments()){
                if (c.getRecipe().getTitle().equals(recipeTitle)){
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
    }
}
