package com.example.nomnomapp.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.nomnomapp.model.NomNomUser;
import com.example.nomnomapp.model.Recipe;
import com.example.nomnomapp.model.RecipeList;
import com.example.nomnomapp.model.Recipe.RecipeCategory;
import com.example.nomnomapp.model.RecipeList.ListCategory;
import com.example.nomnomapp.repository.RecipeListRepository;
import com.example.nomnomapp.repository.RecipeRepository;
import com.example.nomnomapp.repository.UserRepository;

public class FavoriteServiceTests {
    
    @Mock
    private RecipeListRepository recipeListRepository;
    
    @Mock
    private RecipeRepository recipeRepository;
    
    @Mock
    private UserRepository userRepository;
    
    @InjectMocks
    private FavoriteService favoriteService;
    
    private NomNomUser testUser;
    private Recipe testRecipe;
    private RecipeList testFavoritesList;
    
    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        
        // Setup test user
        testUser = new NomNomUser("testUser", "test@example.com", "password123");
        testUser.setUserId(1);
        
        // Setup test recipe
        testRecipe = new Recipe();
        testRecipe.setRecipeID(1);
        testRecipe.setTitle("Test Recipe");
        testRecipe.setDescription("Test Description");
        testRecipe.setCategory(RecipeCategory.Dinner);
        testRecipe.setCreationDate(new Date(System.currentTimeMillis()));
        testRecipe.setNomNomUser(testUser);
        
        // Setup test favorites list
        testFavoritesList = new RecipeList();
        testFavoritesList.setRecipeListID(1);
        testFavoritesList.setName("Favorites");
        testFavoritesList.setCategory(ListCategory.Favorites);
        testFavoritesList.setNomNomUser(testUser);
    }

    @Test
    public void testFindOrCreateFavoritesListWhenListExists() {
        // Setup
        List<RecipeList> userLists = new ArrayList<>();
        userLists.add(testFavoritesList);
        
        when(recipeListRepository.findByNomNomUser(testUser)).thenReturn(userLists);
        
        // Execute
        RecipeList result = favoriteService.findOrCreateFavoritesList(testUser);
        
        // Verify
        assertEquals(testFavoritesList.getRecipeListID(), result.getRecipeListID());
        assertEquals(testFavoritesList.getName(), result.getName());
        assertEquals(testFavoritesList.getCategory(), result.getCategory());
        verify(recipeListRepository, times(1)).findByNomNomUser(testUser);
        verify(recipeListRepository, never()).save(any(RecipeList.class));
    }
    
    @Test
    public void testFindOrCreateFavoritesListWhenListDoesNotExist() {
        // Setup
        List<RecipeList> emptyList = new ArrayList<>();
        RecipeList newFavoritesList = new RecipeList();
        newFavoritesList.setRecipeListID(2);
        newFavoritesList.setName("Favorites");
        newFavoritesList.setCategory(ListCategory.Favorites);
        newFavoritesList.setNomNomUser(testUser);
        
        when(recipeListRepository.findByNomNomUser(testUser)).thenReturn(emptyList);
        when(recipeListRepository.save(any(RecipeList.class))).thenReturn(newFavoritesList);
        
        // Execute
        RecipeList result = favoriteService.findOrCreateFavoritesList(testUser);
        
        // Verify
        assertEquals(newFavoritesList.getRecipeListID(), result.getRecipeListID());
        assertEquals("Favorites", result.getName());
        assertEquals(ListCategory.Favorites, result.getCategory());
        verify(recipeListRepository, times(1)).findByNomNomUser(testUser);
        verify(recipeListRepository, times(1)).save(any(RecipeList.class));
    }
    
    @Test
    public void testAddToFavorites() {
        // Setup
        int userId = 1;
        int recipeId = 1;
        List<RecipeList> userLists = new ArrayList<>();
        userLists.add(testFavoritesList);
        
        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
        when(recipeRepository.findByRecipeId(recipeId)).thenReturn(testRecipe);
        when(recipeListRepository.findByNomNomUser(testUser)).thenReturn(userLists);
        when(recipeListRepository.save(any(RecipeList.class))).thenReturn(testFavoritesList);
        
        // Execute
        RecipeList result = favoriteService.addToFavorites(userId, recipeId);
        
        // Verify
        assertEquals(testFavoritesList.getRecipeListID(), result.getRecipeListID());
        verify(userRepository, times(1)).findById(userId);
        verify(recipeRepository, times(1)).findByRecipeId(recipeId);
        verify(recipeListRepository, times(1)).findByNomNomUser(testUser);
        verify(recipeRepository, times(1)).save(testRecipe);
        verify(recipeListRepository, times(1)).save(testFavoritesList);
    }
    
    @Test
    public void testAddToFavoritesWhenRecipeAlreadyInFavorites() {
        // Setup
        int userId = 1;
        int recipeId = 1;
        List<RecipeList> userLists = new ArrayList<>();
        userLists.add(testFavoritesList);
        
        // Add recipe to favorites list
        testFavoritesList.addRecipe(testRecipe);
        testRecipe.addRecipeList(testFavoritesList);
        
        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
        when(recipeRepository.findByRecipeId(recipeId)).thenReturn(testRecipe);
        when(recipeListRepository.findByNomNomUser(testUser)).thenReturn(userLists);
        
        // Execute
        RecipeList result = favoriteService.addToFavorites(userId, recipeId);
        
        // Verify
        assertEquals(testFavoritesList.getRecipeListID(), result.getRecipeListID());
        verify(userRepository, times(1)).findById(userId);
        verify(recipeRepository, times(1)).findByRecipeId(recipeId);
        verify(recipeListRepository, times(1)).findByNomNomUser(testUser);
        verify(recipeRepository, never()).save(testRecipe);
        verify(recipeListRepository, never()).save(testFavoritesList);
    }
    
    @Test
    public void testRemoveFromFavorites() {
        // Setup
        int userId = 1;
        int recipeId = 1;
        List<RecipeList> userLists = new ArrayList<>();
        userLists.add(testFavoritesList);
        
        // Add recipe to favorites list
        testFavoritesList.addRecipe(testRecipe);
        testRecipe.addRecipeList(testFavoritesList);
        
        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
        when(recipeRepository.findByRecipeId(recipeId)).thenReturn(testRecipe);
        when(recipeListRepository.findByNomNomUser(testUser)).thenReturn(userLists);
        when(recipeListRepository.save(any(RecipeList.class))).thenReturn(testFavoritesList);
        
        // Execute
        RecipeList result = favoriteService.removeFromFavorites(userId, recipeId);
        
        // Verify
        assertEquals(testFavoritesList.getRecipeListID(), result.getRecipeListID());
        verify(userRepository, times(1)).findById(userId);
        verify(recipeRepository, times(1)).findByRecipeId(recipeId);
        verify(recipeListRepository, times(1)).findByNomNomUser(testUser);
        verify(recipeRepository, times(1)).save(testRecipe);
        verify(recipeListRepository, times(1)).save(testFavoritesList);
    }
    
    @Test
    public void testIsRecipeFavorited() {
        // Setup
        int userId = 1;
        int recipeId = 1;
        List<RecipeList> userLists = new ArrayList<>();
        userLists.add(testFavoritesList);
        
        // Add recipe to favorites list
        testFavoritesList.addRecipe(testRecipe);
        testRecipe.addRecipeList(testFavoritesList);
        
        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
        when(recipeRepository.findByRecipeId(recipeId)).thenReturn(testRecipe);
        when(recipeListRepository.findByNomNomUser(testUser)).thenReturn(userLists);
        
        // Execute
        boolean result = favoriteService.isRecipeFavorited(userId, recipeId);
        
        // Verify
        assertTrue(result);
        verify(userRepository, times(1)).findById(userId);
        verify(recipeRepository, times(1)).findByRecipeId(recipeId);
        verify(recipeListRepository, times(1)).findByNomNomUser(testUser);
    }
    
    @Test
    public void testIsRecipeNotFavorited() {
        // Setup
        int userId = 1;
        int recipeId = 1;
        List<RecipeList> userLists = new ArrayList<>();
        userLists.add(testFavoritesList);
        
        // Don't add recipe to favorites list
        
        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
        when(recipeRepository.findByRecipeId(recipeId)).thenReturn(testRecipe);
        when(recipeListRepository.findByNomNomUser(testUser)).thenReturn(userLists);
        
        // Execute
        boolean result = favoriteService.isRecipeFavorited(userId, recipeId);
        
        // Verify
        assertFalse(result);
        verify(userRepository, times(1)).findById(userId);
        verify(recipeRepository, times(1)).findByRecipeId(recipeId);
        verify(recipeListRepository, times(1)).findByNomNomUser(testUser);
    }
    
    @Test
    public void testGetFavoriteRecipes() {
        // Setup
        int userId = 1;
        List<RecipeList> userLists = new ArrayList<>();
        List<Recipe> favoriteRecipes = new ArrayList<>();
        favoriteRecipes.add(testRecipe);
        testFavoritesList.addRecipe(testRecipe);
        userLists.add(testFavoritesList);
        
        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
        when(recipeListRepository.findByNomNomUser(testUser)).thenReturn(userLists);
        
        // Execute
        List<Recipe> result = favoriteService.getFavoriteRecipes(userId);
        
        // Verify
        assertEquals(1, result.size());
        assertEquals(testRecipe.getRecipeID(), result.get(0).getRecipeID());
        verify(userRepository, times(1)).findById(userId);
        verify(recipeListRepository, times(1)).findByNomNomUser(testUser);
    }
    
    @Test
    public void testGetFavoriteRecipesByUsername() {
        // Setup
        String username = "testUser";
        List<RecipeList> userLists = new ArrayList<>();
        List<Recipe> favoriteRecipes = new ArrayList<>();
        favoriteRecipes.add(testRecipe);
        testFavoritesList.addRecipe(testRecipe);
        userLists.add(testFavoritesList);
        
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(testUser));
        when(recipeListRepository.findByNomNomUser(testUser)).thenReturn(userLists);
        
        // Execute
        List<Recipe> result = favoriteService.getFavoriteRecipesByUsername(username);
        
        // Verify
        assertEquals(1, result.size());
        assertEquals(testRecipe.getRecipeID(), result.get(0).getRecipeID());
        verify(userRepository, times(1)).findByUsername(username);
        verify(recipeListRepository, times(1)).findByNomNomUser(testUser);
    }
} 