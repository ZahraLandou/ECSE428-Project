package com.example.nomnomapp.stepdefinitions;


import static org.junit.jupiter.api.Assertions.*;
import java.util.*;


import com.example.nomnomapp.model.Comment;
import com.example.nomnomapp.model.NomNomUser;
import com.example.nomnomapp.model.Recipe;
import com.example.nomnomapp.model.Recipe.RecipeCategory;
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

import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Date;

@Transactional
public class CommentStepDefinitions {


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

    @Autowired
    private RecipeService recipeService;

    private Comment createdComment;
    private List<Recipe> existingRecipes;
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


    @Given("the following users exist in the system")
    public void the_following_users_exist(io.cucumber.datatable.DataTable dataTable) {
        // System.out.println("testing");
        List<Map<String, String>> rows = dataTable.asMaps();
        for (Map<String, String> row : rows) {
            String username = row.get("username");
            if(!userService.getUserByUsername(username).isPresent()){
                userService.createUser(
                        row.get("username"), 
                        row.get("emailAddress"), 
                        row.get("password"));
            }
        }
    }

    @Given("the following recipes exist in the system")
    public void the_following_recipe_exists(io.cucumber.datatable.DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps();
        for (Map<String, String> row : rows) {
            int rowRecipeId = Integer.parseInt(row.get("recipeId"));
            Recipe recipe = recipeService.getRecipeByID(rowRecipeId);
            if(recipe == null){
                Recipe newRecipe = new Recipe();
                newRecipe.setRecipeID(rowRecipeId);
                newRecipe.setTitle(row.get("title"));
                newRecipe.setDescription(row.get("description"));
                newRecipe.setInstructions(row.get("instructions"));
                // TODO: disabled until we are able to handle cases when we get non-existing category in table
                // newRecipe.setCategory(Recipe.RecipeCategory.valueOf(row.get("category"))); 
                newRecipe.setCategory(null); // Placeholder in case line above does not work
                newRecipe.setLikes(Integer.parseInt(row.get("likes")));
                newRecipe.setPicture(row.get("picture"));
                newRecipe.setAverageRating(Double.parseDouble(row.get("averageRating")));

                NomNomUser usr = userRepo.findByUsername(row.get("nomNomUser")).orElse(null);
                newRecipe.setNomNomUser(usr);
                
                recipeRepo.save(recipe);
                existingRecipes.add(newRecipe);
            }else{
                existingRecipes.add(recipe);
            }
        }
    }

    @Given("a recipe with recipeId {string} exists")
    public void the_following_recipe_exists(String aRecipeId) {
        Recipe recipe = recipeService.getRecipeByID(Integer.parseInt(aRecipeId));
        if(recipe == null){
            Recipe newRecipe = new Recipe();
            newRecipe.setRecipeID(Integer.parseInt(aRecipeId));
            recipeRepo.save(recipe);
            existingRecipes.add(newRecipe);
        }else{
            existingRecipes.add(recipe);
        }
    }

    @When("user with username {string} attempts to add a new comment with content {string} and rating {string} to an existing recipe {string}")
    public void user_adds_comment(String username, String commentContent, String rating, String recipeId ) {
        NomNomUser usr =  userService.getUserByUsername(username).orElse(null);
        try {
            createdComment = commentService.createComment(
                                                username ,
                                                commentContent, 
                                                Double.parseDouble(rating), 
                                                Integer.parseInt(recipeId));
            commentRepo.save(createdComment);
        } catch (Exception e) {
            error = e.getMessage();
        }
    }

    @Then("the comment with content {string} and rating {string} created by user {string} for recipe with recipeId {string} should exist")
    public void comment_should_exist(String commentId, String username, String recipeId, String creationDate, String commentContent, String rating) {
        Comment comment = commentRepo.findCommentByCommentId(Integer.parseInt(commentId));
        assertNotNull(comment);
        assertEquals(username, comment.getNomNomUser().getUsername());
        assertEquals(Integer.parseInt(recipeId), comment.getRecipe().getRecipeID());
        assertEquals(Date.valueOf(creationDate), comment.getCreationDate());
        assertEquals(commentContent, comment.getCommentContent());
        assertEquals(Double.parseDouble(rating), comment.getRating());
    }

    @Then("the rating of the recipe with recipeID {string} should be {string}")
    public void the_rating_should_be_updated(String aRecipeId, String aAverageRating) {
        Recipe recipe = recipeRepo.findByRecipeId(Integer.parseInt(aRecipeId));
        assertNotNull(recipe);
        assertEquals(Double.parseDouble(aAverageRating),recipe.getAverageRating());
    }

    @Then("the number of comments for recipe with recipeID {string} in should be {string}")
    public void the_number_of_comments_should_be(String aRecipeId, String aCommentCount) {
        Recipe recipe = recipeRepo.findByRecipeId(Integer.parseInt(aRecipeId));
        assertNotNull(recipe);
        assertEquals(Integer.parseInt(aCommentCount), recipe.getComments().size());
    }

    @Then("an error should be returned with message {string}")
    public void error_should_be_returned(String expectedMessage) {
        assertEquals(expectedMessage, error);
    }
}