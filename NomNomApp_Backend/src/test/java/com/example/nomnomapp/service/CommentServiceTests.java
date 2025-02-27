package com.example.nomnomapp.service;

import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

import java.sql.Date;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.nomnomapp.model.Comment;
import com.example.nomnomapp.model.NomNomUser;
import com.example.nomnomapp.model.Recipe;
import com.example.nomnomapp.repository.CommentRepository;

/**
 * Test class for the IngredientService class
 * 
 * @author Zahra
 */

@ExtendWith(MockitoExtension.class)
public class CommentServiceTests {
    
    @Mock
    private CommentRepository commentRepository;

    @Mock
    private CommentService commentService;

    private Recipe testRecipe;
    private NomNomUser testUser;
    private Comment testComment;


    @BeforeEach
    void setUp(){
        // TODO: username, email, passwords should be non-nullable. Is declaration with empty constructor allowable?
        testUser = new NomNomUser();
        testUser.setUsername("testUser");
        testUser.setEmailAddress("test@example.com");
        testUser.setPassword("password123");

        testRecipe = new Recipe();
        testRecipe.setTitle("French Cheese Cake");
        testRecipe.setDescription("hi my name is Thierry and I want to share this family recipe from my father Pierre");
        testRecipe.setInstructions("1-Put cheese, 2-Put cake, 3-Bake for 10 minutes");
        // Check if Date is automatically set
        testRecipe.setCategory(Recipe.RecipeCategory.Dessert);
        testRecipe.setLikes(203);
        testRecipe.setPicture("picture");
        testRecipe.setAverageRating(3.4);
        testRecipe.setNomNomUser(testUser);
    }

    @AfterEach
    void cleanup(){
        reset(commentRepository);
    }

    @Test
    public void testCreateComment_Success(){
        when(commentRepository.save(testComment)).thenReturn(testComment);

        Comment savedComment = commentService.createComment(
                                    testUser,
                                    "Dummy Comment.",
                                    5, 
                                    testRecipe
                                    );

    }
}
