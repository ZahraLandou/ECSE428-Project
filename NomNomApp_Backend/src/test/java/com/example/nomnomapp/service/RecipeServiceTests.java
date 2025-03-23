package com.example.nomnomapp.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.example.nomnomapp.model.NomNomUser;
import com.example.nomnomapp.model.Recipe;
import com.example.nomnomapp.model.Recipe.RecipeCategory;
import com.example.nomnomapp.repository.RecipeRepository;
import com.example.nomnomapp.repository.CollectionRepository;

import java.sql.Date;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

@ExtendWith(MockitoExtension.class)
public class RecipeServiceTests {

    @Mock
    private RecipeRepository recipeRepo;
    @Mock
    private CollectionRepository collectionRepo;

    @InjectMocks
    private RecipeService recipeServ;

    private Recipe testRecipe;
    private Recipe testRecipe2;
    private Map<String, List<Recipe>> testCollection;
    private NomNomUser testUser;

    @BeforeEach
    void setUp() {
        testUser = new NomNomUser("testUser", "test@example.com", "password123");
        testRecipe = new Recipe("French Cheese cake",
                                "hi my name is Thierry and I want to share this family recipe from my father Pierre",
                                "1-Put cheese, 2-Put cake, 3-Bake for 10 minutes",
                                new Date(System.currentTimeMillis()),
                                Recipe.RecipeCategory.Dessert,
                                203,
                                "picture",
                                3.4,
                                testUser);
        testRecipe2 = new Recipe("French Flan",
                                "hi my name is Thierry and I want to share this flan family recipe from my father Pierre",
                                "1-Mix the cream sugar and milk, 2-Put sugar on the bowl and burn it until caramelized, 3-Bake for 3 minutes in the oven",
                                new Date(System.currentTimeMillis()),
                                Recipe.RecipeCategory.Dessert,
                                23,
                                "picture 2",
                                4.9,
                                testUser);
        testRecipe.setRecipeID(0);
        testRecipe2.setRecipeID(1);
        testCollection = new HashMap<>();
        List<Recipe> recipeList = new ArrayList<Recipe>();
        recipeList.add(testRecipe);
        recipeList.add(testRecipe2);
        testCollection.put("Deserts from Thierry", recipeList);
    }

    //createRecipe (check that the new recipe contains the right info)
    @Test
    void testCreateRecipe_Success() {
        when(recipeRepo.save(testRecipe)).thenReturn(testRecipe);

        Recipe savedRecipe = recipeServ.createRecipe(testRecipe);

        assertNotNull(savedRecipe);
        assertEquals("French Cheese cake", savedRecipe.getTitle());
        assertEquals(203, savedRecipe.getLikes());
        assertEquals(0, savedRecipe.getRecipeID());
        assertEquals("hi my name is Thierry and I want to share this family recipe from my father Pierre", savedRecipe.getDescription());
        verify(recipeRepo, times(1)).save(testRecipe);
    }

    //Successfully create a collection of 2 recipes
    @Test
    void testCreateCollection_Success() {
        when(collectionRepo.save(testCollection.get("Deserts from Thierry"))).thenReturn(testCollection.get("Deserts from Thierry"));

        List<Recipe> collection = recipeServ.createRecipeCollection(testCollection.get("Deserts from Thierry"));

        assertNotNull(collection);
        assertNotNull(testCollection.get("Deserts from Thierry").get(0));
        assertNotNull(testCollection.get("Deserts from Thierry").get(1));
        assertEquals(testRecipe, testCollection.get("Deserts from Thierry").get(0));
        assertEquals(testRecipe2, testCollection.get("Deserts from Thierry").get(1));
        assertEquals(203, testCollection.get("Deserts from Thierry").get(0).getLikes());
        assertEquals(23, testCollection.get("Deserts from Thierry").get(1).getLikes());
        assertEquals(0, testCollection.get("Deserts from Thierry").get(0).getRecipeID());
        assertEquals(1, testCollection.get("Deserts from Thierry").get(1).getRecipeID());
        assertEquals("hi my name is Thierry and I want to share this family recipe from my father Pierre", testCollection.get("Deserts from Thierry").get(0).getDescription());
        assertEquals("hi my name is Thierry and I want to share this flan family recipe from my father Pierre", testCollection.get("Deserts from Thierry").get(1).getDescription());
        verify(collectionRepo, times(1)).save((testCollection.get("Deserts from Thierry")));
    }

    //Successfully deletes an existing recipe
    @Test
    void testDeleteRecipeById_Success() {
        when(recipeRepo.findById(0)).thenReturn(Optional.of(testRecipe));

        assertDoesNotThrow(() -> recipeServ.deleteRecipeById(0));

        verify(recipeRepo, times(1)).delete(testRecipe);
    }

    //Successfully delete an existing collection
    @Test
    void testDeleteCollectionById_Success() {
    when(collectionRepo.findById(0)).thenReturn(Optional.of(testCollection.get("Deserts from Thierry")));

    assertDoesNotThrow(() -> recipeServ.deleteCollectionByCollectionId(0));

    verify(collectionRepo, times(1)).delete(testCollection.get("Deserts from Thierry"));
    }
    //delete non-existent recipe (return exception)
    @Test
    void testDeleteRecipeById_RecipeNotFound() {
        when(recipeRepo.findById(0)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> recipeServ.deleteRecipeById(0));

        assertEquals("Recipe with ID '0' not found.", exception.getMessage());
        verify(recipeRepo, never()).delete(any());
    }
    
    //delete with a null ID
    @Test
    void testDeleteRecipeById_NullId() {
        Integer nullId = null;
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> recipeServ.deleteRecipeById(nullId));

        assertEquals("Recipe with ID 'null' not found.", exception.getMessage());
        verify(recipeRepo, never()).delete(any());
    }
    //getAllRecipes
    @Test
    void testGetAllRecipes_Success() {
        List<Recipe> recipes = Arrays.asList(testRecipe, new Recipe());
        when(recipeRepo.findAll()).thenReturn(recipes);

        List<Recipe> result = recipeServ.getAllRecipes();

        assertEquals(2, result.size());
        verify(recipeRepo, times(1)).findAll();
    }
    //getRecipesByTitle (No Results)
    @Test
    void testGetRecipesByTitle_NotFound() {
        when(recipeRepo.findRecipeByTitle("Strawberry Cake")).thenReturn(List.of());

        List<Recipe> recipes = recipeServ.getRecipesByTitle("Strawberry Cake");

        assertTrue(recipes.isEmpty());
    }

    //getRecipesByCategory (Success)
    @Test
    void testGetRecipesByCategory_Success() {
        when(recipeRepo.findRecipeByCategory(RecipeCategory.Dessert)).thenReturn(List.of(testRecipe));

        List<Recipe> recipes = recipeServ.getRecipesByCategory(RecipeCategory.Dessert);

        assertEquals(1, recipes.size());
        assertEquals(RecipeCategory.Dessert, recipes.get(0).getCategory());
    }

    //getRecipesByCategory (No Results)
    @Test
    void testGetRecipesByCategory_NotFound() {
        when(recipeRepo.findRecipeByCategory(RecipeCategory.Breakfast)).thenReturn(List.of());

        List<Recipe> recipes = recipeServ.getRecipesByCategory(RecipeCategory.Breakfast);

        assertTrue(recipes.isEmpty());
    }
    //getCollectionsByTitle test
    @Test
    void testGetCollectionsByCollectionName_Success() {
        when(collectionRepo.findCollectionByTitle("Deserts from Thierry")).thenReturn(testCollection.get("Deserts from Thierry"));

        List<Recipe> collection = recipeServ.getCollectionByName("Deserts from Thierry");

        assertEquals(2, collection.size());
        assertEquals(testRecipe, collection.get(0));
        assertEquals(testRecipe2, collection.get(1));
    }
    //getCollectionsById test
    @Test
    void testGetCollectionsByCollectionId_Success() {
        when(collectionRepo.findByCollectionId(0)).thenReturn(testCollection.get("Deserts from Thierry"));

        List<Recipe> collection = recipeServ.getCollectionById(0);

        assertEquals(2, collection.size());
        assertEquals(testRecipe, collection.get(0));
        assertEquals(testRecipe2, collection.get(1));
    }
}
