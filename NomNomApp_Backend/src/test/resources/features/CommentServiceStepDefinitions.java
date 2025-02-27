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
import com.example.nomnomapp.service.UserService;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.*;
import jakarta.transaction.Transactional;

import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Date;

@Transactional
public class CommentServiceStepDefinitions {


    @Autowired
    private CommentService commentService;

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
    public void setUp() {
        userRepo.deleteAll();
        commentRepo.deleteAll();
        recipeRepo.deleteAll();

    }

    @After
    public void tearDown() {
        userRepo.deleteAll();
        commentRepo.deleteAll();
        recipeRepo.deleteAll();
    }


    @Given("the following Users exist in the system")
    public void the_following_users_exist(io.cucumber.datatable.DataTable dataTable) {
        System.out.println("testing");
        List<Map<String, String>> rows = dataTable.asMaps();
        for (Map<String, String> row : rows) {
            userService.createUser(row.get("username"), row.get("emailAddress"), row.get("password"));
        }

    }

    @Given("the following Recipe exists in the system ")
    public void the_following_recipe_exists(io.cucumber.datatable.DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps();
        for (Map<String, String> row : rows) {
            new Recipe(Integer.parseInt(row.get("recipeId")) , row.get("title"), row.get("description"),
                    row.get("instructions"), Date.valueOf(row.get("creationDate")), Recipe.RecipeCategory.valueOf(row.get("category")),
                    Integer.parseInt(row.get("likes")), row.get("picture"), Double.parseDouble(row.get("averageRating")),
                    userRepo.findByUserId(Integer.parseInt(row.get("recipeNomNomUser"))));
        }
    }

    @When("user with userId {string} attempts to add a new comment with {string}, date {string} and content {string} to an existing recipe {string}")
    public void user_adds_comment(String commentId, String commentContent, String rating, String userId, String recipeId ) {
        try {
            createdComment = commentService.createComment(Integer.parseInt(commentId), commentContent, Double.parseDouble(rating), userRepo.findByUserId(Integer.parseInt(userId)),recipeRepo.findByRecipeId(Integer.parseInt(recipeId)));
        } catch (Exception e) {
            error = e.getMessage();
        }
    }

    @Then("the rating of the recipe {string} should be {string}")
    public void the_rating_should_be_updated(String recipeId, String expectedRating) {
        Recipe recipe = recipeRepo.findByRecipeId(Integer.parseInt(recipeId));
        assertNotNull(recipe);
        assertEquals(Double.parseDouble(expectedRating),recipe.getAverageRating());
    }

    @Then("the number of comments for recipe {string} in the system shall be {string}")
    public void the_number_of_comments_should_be(String recipeId, String expectedNumber) {
        Recipe recipe = recipeRepo.findByRecipeId(Integer.parseInt(recipeId));
        assertNotNull(recipe);
        assertEquals(Integer.parseInt(expectedNumber), recipe.getComments().size());
    }

    @Then("the comment {string} created by user {string} for recipe {string}, date {string}, content {string}, and rating {string} shall exist in the system ")
    public void comment_should_exist(String commentId, String username, String recipeId, String creationDate, String commentContent, String rating) {
        Comment comment = commentRepo.findCommentByCommentId(Integer.parseInt(commentId));
        assertNotNull(comment);
        assertEquals(username, comment.getNomNomUser().getUsername());
        assertEquals(Integer.parseInt(recipeId), comment.getRecipe().getRecipeID());
        assertEquals(Date.valueOf(creationDate), comment.getCreationDate());
        assertEquals(commentContent, comment.getCommentContent());
        assertEquals(Double.parseDouble(rating), comment.getRating());
    }

    @Then("an error should be returned with message {string}")
    public void error_should_be_returned(String expectedMessage) {
        assertEquals(expectedMessage, error);
    }
}