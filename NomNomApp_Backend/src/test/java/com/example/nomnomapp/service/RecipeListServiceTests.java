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
import com.example.nomnomapp.model.RecipeList;
import com.example.nomnomapp.repository.CommentRepository;
import com.example.nomnomapp.repository.RecipeListRepository;
import com.example.nomnomapp.repository.RecipeRepository;
import com.example.nomnomapp.repository.UserRepository;

import ch.qos.logback.classic.joran.ReconfigureOnChangeTask;

import com.example.nomnomapp.exception.NomNomException;

/**
 * Test class for the RecipeListService class
 * 
 * @author Kazuto
 */

@ExtendWith(MockitoExtension.class)
public class RecipeListServiceTests {
    
    @Mock
    private RecipeListRepository recipeListRepository;

    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private RecipeListService recipeListService;

    @InjectMocks
    private RecipeService recipeService;

    private Recipe testRecipe;
    private NomNomUser testUser;
    private RecipeList testRecipeList;


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
        testRecipeList = new RecipeList(
                                "lunchIdeas", 
                                RecipeList.ListCategory.Regular, 
                                testUser);
    }

    @AfterEach
    void cleanup(){
        reset(recipeListRepository);
        reset(recipeRepository);
        reset(userRepository);
    }


    //============== Add Recipe to List Tests ================
    @Test
    public void testAddRecipeToList_Success(){
        when(recipeListRepository.save(any(RecipeList.class))).thenReturn(testRecipeList);

        boolean result = recipeListService.addRecipeToList(testUser.getUserId(), testRecipeList.getRecipeListID(), testRecipe.getRecipeID());

        assertEquals(result, true);
        assertEquals(testRecipe, testRecipeList.getRecipes().get(0));
        assertEquals(testRecipeList, testRecipe.getRecipeList(0));
    }

    @Test
    public void testAddRecipeToList_Fail_InvalidRecipeId(){
        //TODO

    }

    @Test
    public void testAddRecipeToList_Fail_RecipeListIdDNE(){
        //TODO

    }

    @Test
    public void testAddRecipeToList_Fail_RecipeListIdAlreadyInList(){
        //TODO

    }
    
    @Test
    public void testAddRecipeToList_Fail_UserIdDNE(){
        //TODO

    }

    @Test
    public void testAddRecipeToList_Fail_UserIdNotOwner(){
        //TODO

    }

    //============== Remove Recipe from List Tests ================
    @Test
    public void testRemoveRecipeFromList_Success(){
        //TODO

    }

    @Test
    public void testRemoveRecipeFromList_Fail_RecipeIdDNE(){
        //TODO

    }

    @Test
    public void testRemoveRecipeFromList_Fail_RecipeIdNotInList(){
        //TODO

    }

    @Test
    public void testRemoveRecipeFromList_Fail_InvalidRecipeListId(){
        //TODO

    }

    @Test
    public void testRemoveRecipeFromList_Fail_UserIdDNE(){
        //TODO

    }
    
    @Test
    public void testRemoveRecipeFromList_Fail_UserIdNotOwner(){
        //TODO

    }
}

