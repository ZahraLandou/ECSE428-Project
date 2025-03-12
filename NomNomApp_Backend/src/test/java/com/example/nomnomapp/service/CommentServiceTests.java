package com.example.nomnomapp.service;

import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

import java.sql.Date;
import java.util.Optional;

import org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.nomnomapp.model.Comment;
import com.example.nomnomapp.model.NomNomUser;
import com.example.nomnomapp.model.Recipe;
import com.example.nomnomapp.repository.CommentRepository;
import com.example.nomnomapp.repository.RecipeRepository;
import com.example.nomnomapp.repository.UserRepository;

import ch.qos.logback.classic.joran.ReconfigureOnChangeTask;

import com.example.nomnomapp.exception.NomNomException;

/**
 * Test class for the IngredientService class
 * 
 * @author Kazuto
 */

@ExtendWith(MockitoExtension.class)
public class CommentServiceTests {
    
    @Mock
    private CommentRepository commentRepository;

    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CommentService commentService;

    @InjectMocks
    private RecipeService recipeService;

    private Recipe testRecipe;
    private NomNomUser testUser;
    private Comment testComment;


    @BeforeEach
    void setUp(){
        testUser = new NomNomUser(
                                "testUser", 
                                "test@example.com", 
                                "password123"
                                );
        testRecipe = new Recipe(
                                "French Cheese cake",
                                "hi my name is Thierry and I want to share this family recipe from my father Pierre",
                                "1-Put cheese, 2-Put cake, 3-Bake for 10 minutes",
                                Recipe.RecipeCategory.Dessert,
                                203,
                                "picture",
                                3.4,
                                testUser);
        testComment = new Comment("Dummy Comment.", 5.0, testUser, testRecipe);
    }

    @AfterEach
    void cleanup(){
        reset(commentRepository);
    }

    @Test
    public void testCreateComment_Success(){
        when(commentRepository.save(any(Comment.class))).thenReturn(testComment);
        when(recipeRepository.findByRecipeId(testRecipe.getRecipeID())).thenReturn(testRecipe);
        when(userRepository.findByUsername(testUser.getUsername())).thenReturn(Optional.of(testUser));

        Comment savedComment = commentService.createComment(
                                    testUser.getUsername(),
                                    "Dummy Comment.",
                                    (double)5,
                                    testRecipe.getRecipeID()
                                    );

        assertNotNull(savedComment);
        assertEquals("testUser", savedComment.getNomNomUser().getUsername());
        assertEquals("Dummy Comment.", savedComment.getCommentContent());
        assertEquals(5.0, savedComment.getRating(), 0.01);
        assertEquals("French Cheese cake", savedComment.getRecipe().getTitle());
    }

    @Test
    public void testCreateComment_Fail_EmptyContent(){
        when(commentRepository.save(any(Comment.class))).thenReturn(testComment);
        when(userRepository.findByUsername(testUser.getUsername())).thenReturn(Optional.of(testUser));
        
        Exception e = assertThrows(NomNomException.class, ()->{
            commentService.createComment(
                                    testUser.getUsername(),
                                    "",
                                    (double)5,
                                    testRecipe.getRecipeID()
                                    );
        } );

        assertEquals("Comment cannot be empty.", e.getMessage());
    }

    @Test
    public void testCreateComment_Fail_NegativeRating(){
        when(userRepository.findByUsername(testUser.getUsername())).thenReturn(Optional.of(testUser));
        
        Exception e = assertThrows(NomNomException.class, ()->{
            commentService.createComment(
                                    testUser.getUsername(),
                                    "Dummy Comment.",
                                    -1.,
                                    testRecipe.getRecipeID()
                                    );
        } );

        assertEquals("Rating must be between 0 and 5.", e.getMessage());
    }

    @Test
    public void testCreateComment_Fail_RatingTooBig(){
        when(commentRepository.save(any(Comment.class))).thenReturn(testComment);
        when(userRepository.findByUsername(testUser.getUsername())).thenReturn(Optional.of(testUser));
        
        Exception e = assertThrows(NomNomException.class, ()->{
            commentService.createComment(
                                    testUser.getUsername(),
                                    "Dummy Comment.",
                                    100.,
                                    testRecipe.getRecipeID()
                                    );
        } );

        assertEquals("Rating must be between 0 and 5.", e.getMessage());
    }

    @Test
    public void testCreateComment_Fail_RecipeDNE(){
        when(commentRepository.save(any(Comment.class))).thenReturn(testComment);
        
        Exception e = assertThrows(NomNomException.class, ()->{
            commentService.createComment(
                                    testUser.getUsername(),
                                    "Dummy Comment.",
                                    5.,
                                    testRecipe.getRecipeID()
                                    );
        } );

        assertEquals("Recipe does not exist.", e.getMessage());
    }

    @Test
    public void testCreateComment_Fail_UserDNE(){
        when(commentRepository.save(any(Comment.class))).thenReturn(testComment);
        when(recipeRepository.findByRecipeId(testRecipe.getRecipeID())).thenReturn(testRecipe);
        when(userRepository.findByUsername(testUser.getUsername())).thenReturn(Optional.empty());
        
        Exception e = assertThrows(NomNomException.class, ()->{
            commentService.createComment(
                                    testUser.getUsername(),
                                    "Dummy Comment.",
                                    5.,
                                    testRecipe.getRecipeID()
                                    );
        } );

        assertEquals("User does not exist.", e.getMessage());
    }
    
    @Test
    public void testUpdateComment_Success_ContentOnly(){
        when(commentRepository.findCommentByCommentId(1)).thenReturn(testComment);
        when(commentRepository.save(testComment)).thenReturn(testComment);
        
        int testCommentId = 1;
        String newCommentContent = "ABCDEFG";

        Comment updatedComment = commentService.updateComment(
            testCommentId,
            newCommentContent
        );
        
        assertNotNull(updatedComment);
        assertEquals(newCommentContent, updatedComment.getCommentContent());
    }

    @Test
    public void testUpdateComment_Success_RatingOnly(){
        when(commentRepository.findCommentByCommentId(1)).thenReturn(testComment);
        when(commentRepository.save(testComment)).thenReturn(testComment);
        
        int testCommentId = 1;
        double newRating = 4.;

        Comment updatedComment = commentService.updateComment(
            testCommentId,
            newRating
        );
        
        assertNotNull(updatedComment);
        assertEquals(newRating, updatedComment.getRating(), 0.01);
    }

    @Test
    public void testUpdateComment_Success_RatingAndContent(){
        when(commentRepository.findCommentByCommentId(1)).thenReturn(testComment);
        when(commentRepository.save(testComment)).thenReturn(testComment);
        when(recipeRepository.save(any(Recipe.class))).thenReturn(testRecipe);
        
        int testCommentId = 1;
        String newCommentContent = "foobar";
        double newRating = 4.;

        Comment updatedComment = commentService.updateComment(
            testCommentId,
            newCommentContent,
            newRating
        );
        
        assertNotNull(updatedComment);
        assertEquals(newCommentContent, updatedComment.getCommentContent());
        assertEquals(newRating, updatedComment.getRating(), 0.01);
    }

    @Test
    public void testUpdateComment_Fail_ContentOnly_InvalidCommentId(){
        when(commentRepository.findCommentByCommentId(1)).thenReturn(testComment);
        when(commentRepository.save(testComment)).thenReturn(testComment);
        
        int testCommentId = -1;
        String newCommentContent = "foobarbuzz";
        Exception e = assertThrows(NomNomException.class, ()->{
            commentService.updateComment(
                testCommentId,
                newCommentContent
            );
        });

        assertEquals("Comment ID is not valid.", e.getMessage());
    }
    @Test
    public void testUpdateComment_Fail_ContentOnly_NoContent(){
        when(commentRepository.findCommentByCommentId(1)).thenReturn(testComment);
        when(commentRepository.save(testComment)).thenReturn(testComment);
        
        int testCommentId = 1;
        String newCommentContent = "";
        Exception e = assertThrows(NomNomException.class, ()->{
            commentService.updateComment(
                testCommentId,
                newCommentContent
            );
        });

        assertEquals("Comment cannot be empty.", e.getMessage());
    }

    @Test
    public void testUpdateComment_Fail_RatingOnly_InvalidCommentId(){
        when(commentRepository.findCommentByCommentId(1)).thenReturn(testComment);
        when(commentRepository.save(testComment)).thenReturn(testComment);
        
        int testCommentId = -1;
        double newRating = 4.;

        Exception e = assertThrows(NomNomException.class, ()->{
            commentService.updateComment(
                testCommentId,
                newRating
            );
        });
        assertEquals("Comment ID is not valid.", e.getMessage());
    }

    @Test
    public void testUpdateComment_Fail_RatingOnly_NegativeRating(){
        when(commentRepository.findCommentByCommentId(1)).thenReturn(testComment);
        when(commentRepository.save(testComment)).thenReturn(testComment);
        
        int testCommentId = 1;
        double newRating = -4.;

        Exception e = assertThrows(NomNomException.class, ()->{
            commentService.updateComment(
                testCommentId,
                newRating
            );
        });
        
        assertEquals("Rating must be between 0 and 5.", e.getMessage());
    }

    @Test
    public void testUpdateComment_Fail_RatingOnly_RatingTooLarge(){
        when(commentRepository.findCommentByCommentId(1)).thenReturn(testComment);
        when(commentRepository.save(testComment)).thenReturn(testComment);
        
        int testCommentId = 1;
        double newRating = 100.;

        Exception e = assertThrows(NomNomException.class, ()->{
            commentService.updateComment(
                testCommentId,
                newRating
            );
        });
        
        assertEquals("Rating must be between 0 and 5.", e.getMessage());
    }

    @Test
    public void testUpdateComment_Fail_RatingAndContent_InvalidCommentId(){
        when(commentRepository.findCommentByCommentId(1)).thenReturn(testComment);
        when(commentRepository.save(testComment)).thenReturn(testComment);
        
        int testCommentId = -1;
        String newCommentContent = "foobar";
        double newRating = 4.;

        Exception e = assertThrows(NomNomException.class, ()->{
            commentService.updateComment(
                testCommentId,
                newCommentContent,
                newRating
            );
        });
        assertEquals("Comment ID is not valid.", e.getMessage());
    }

    @Test
    public void testUpdateComment_Fail_RatingAndContent_EmptyCommentContent(){
        when(commentRepository.findCommentByCommentId(1)).thenReturn(testComment);
        when(commentRepository.save(testComment)).thenReturn(testComment);
        
        int testCommentId = 1;
        String newCommentContent = "";
        double newRating = 4.;

        Exception e = assertThrows(NomNomException.class, ()->{
            commentService.updateComment(
                testCommentId,
                newCommentContent,
                newRating
            );
        });

    }

    @Test
    public void testUpdateComment_Fail_RatingAndContent_NegativeRating(){
        when(commentRepository.findCommentByCommentId(1)).thenReturn(testComment);
        when(commentRepository.save(testComment)).thenReturn(testComment);
        
        int testCommentId = 1;
        String newCommentContent = "foobarbaz";
        double newRating = -4.;

        Exception e = assertThrows(NomNomException.class, ()->{
            commentService.updateComment(
                testCommentId,
                newCommentContent,
                newRating
            );
        });
        assertEquals("Rating must be between 0 and 5.", e.getMessage());
    }

    @Test
    public void testUpdateComment_Fail_RatingAndContent_RatingTooLarge(){
        when(commentRepository.findCommentByCommentId(1)).thenReturn(testComment);
        when(commentRepository.save(testComment)).thenReturn(testComment);
        
        int testCommentId = 1;
        String newCommentContent = "foobarbaz";
        double newRating = 5.1;

        Exception e = assertThrows(NomNomException.class, ()->{
            commentService.updateComment(
                testCommentId,
                newCommentContent,
                newRating
            );
        });
        assertEquals("Rating must be between 0 and 5.", e.getMessage());
    }

    
    
}
