package com.example.nomnomapp.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import com.example.nomnomapp.exception.NomNomException;
import com.example.nomnomapp.model.NomNomUser;
import com.example.nomnomapp.model.Recipe;
import com.example.nomnomapp.model.Recipe.RecipeCategory;
import com.example.nomnomapp.model.ShortFormVideo;
import com.example.nomnomapp.repository.RecipeRepository;
import com.example.nomnomapp.repository.ShortFormVideoRepository;

@ExtendWith(MockitoExtension.class)
public class ShortFormVideoServiceTests {

    @Mock
    private ShortFormVideoRepository shortFormVideoRepository;

    @Mock
    private RecipeRepository recipeRepository;

    @InjectMocks
    private ShortFormVideoService shortFormVideoService;

    private ShortFormVideo testVideo;
    private Recipe testRecipe;
    private NomNomUser testUser;

    @BeforeEach
    void setUp() {
        testUser = new NomNomUser("testUser", "test@example.com", "password123");
        testRecipe = new Recipe("Test Recipe",
                              "A test recipe description",
                              "1. Step one. 2. Step two.",
                              new Date(System.currentTimeMillis()),
                              Recipe.RecipeCategory.Dinner,
                              0,
                              "recipe-image.jpg",
                              4.5,
                              testUser);
        testRecipe.setRecipeID(1);
        
        testVideo = new ShortFormVideo(1, "Test Video", "A test video description", "video-content.mp4", testRecipe);
    }

    @Test
    void testCreateShortFormVideo_Success() {
        // Setup
        when(recipeRepository.findByRecipeId(testRecipe.getRecipeID())).thenReturn(testRecipe);
        when(shortFormVideoRepository.findByRecipe(testRecipe)).thenReturn(null);
        when(shortFormVideoRepository.save(any(ShortFormVideo.class))).thenReturn(testVideo);

        // Execute
        ShortFormVideo savedVideo = shortFormVideoService.createShortFormVideo(testVideo);

        // Verify
        assertNotNull(savedVideo);
        assertEquals("Test Video", savedVideo.getVideoTitle());
        assertEquals(1, savedVideo.getVideoId());
        assertEquals(testRecipe, savedVideo.getRecipe());
        verify(shortFormVideoRepository, times(1)).save(testVideo);
    }

    @Test
    void testCreateShortFormVideo_NullVideo() {
        // Execute & Verify
        NomNomException exception = assertThrows(NomNomException.class, 
                () -> shortFormVideoService.createShortFormVideo(null));
        
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Short form video cannot be null", exception.getMessage());
        verify(shortFormVideoRepository, never()).save(any());
    }

    @Test
    void testCreateShortFormVideo_NullRecipe() {
        // Setup
        testVideo = new ShortFormVideo(1, "Test Video", "A test video description", "video-content.mp4", null);
        
        // Execute & Verify
        NomNomException exception = assertThrows(NomNomException.class, 
                () -> shortFormVideoService.createShortFormVideo(testVideo));
        
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Short form video must be associated with a recipe", exception.getMessage());
        verify(shortFormVideoRepository, never()).save(any());
    }

    @Test
    void testCreateShortFormVideo_RecipeNotFound() {
        // Setup
        when(recipeRepository.findByRecipeId(testRecipe.getRecipeID())).thenReturn(null);
        
        // Execute & Verify
        NomNomException exception = assertThrows(NomNomException.class, 
                () -> shortFormVideoService.createShortFormVideo(testVideo));
        
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("Recipe with ID: " + testRecipe.getRecipeID() + " not found", exception.getMessage());
        verify(shortFormVideoRepository, never()).save(any());
    }

    @Test
    void testCreateShortFormVideo_RecipeAlreadyHasVideo() {
        // Create a fresh recipe and videos for this test to avoid any interference
        Recipe recipe = new Recipe("Test Recipe",
                              "A test recipe description",
                              "1. Step one. 2. Step two.",
                              new Date(System.currentTimeMillis()),
                              Recipe.RecipeCategory.Dinner,
                              0,
                              "recipe-image.jpg",
                              4.5,
                              testUser);
        recipe.setRecipeID(101);  // Use a unique ID for this test
        
        // Important - capture the mock behavior FIRST before creating the video
        when(recipeRepository.findByRecipeId(recipe.getRecipeID())).thenReturn(recipe);
        
        ShortFormVideo existingVideo = new ShortFormVideo(202, "Existing Video", "Already exists", "existing-video.mp4", recipe);
        when(shortFormVideoRepository.findByRecipe(recipe)).thenReturn(existingVideo);
        
        // Create video AFTER setting up mocks to ensure the references stay consistent
        final ShortFormVideo newVideo = new ShortFormVideo(201, "Test Video", "Test Description", "test-video.mp4", recipe);
        
        // Make sure the video actually has a recipe
        assertNotNull(newVideo.getRecipe(), "New video's recipe must not be null");
        assertEquals(101, newVideo.getRecipe().getRecipeID(), "Recipe ID should be 101");
        
        // Execute the service method
        NomNomException exception = assertThrows(NomNomException.class, () -> {
            ShortFormVideo capturedVideo = newVideo; // Ensure we're using the exact same object
            assertNotNull(capturedVideo.getRecipe(), "Recipe still not null before service call");
            shortFormVideoService.createShortFormVideo(capturedVideo);
        });
        
        // Verify the exception
        assertEquals(HttpStatus.CONFLICT, exception.getStatus());
        assertEquals("Recipe already has an associated short form video", exception.getMessage());
        verify(shortFormVideoRepository, never()).save(any());
    }

    @Test
    void testGetShortFormVideoById_Success() {
        // Setup
        when(shortFormVideoRepository.findByVideoId(1)).thenReturn(testVideo);
        
        // Execute
        ShortFormVideo retrievedVideo = shortFormVideoService.getShortFormVideoById(1);
        
        // Verify
        assertNotNull(retrievedVideo);
        assertEquals(testVideo, retrievedVideo);
        verify(shortFormVideoRepository, times(1)).findByVideoId(1);
    }

    @Test
    void testGetShortFormVideoById_NotFound() {
        // Setup
        when(shortFormVideoRepository.findByVideoId(999)).thenReturn(null);
        
        // Execute & Verify
        NomNomException exception = assertThrows(NomNomException.class, 
                () -> shortFormVideoService.getShortFormVideoById(999));
        
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("Short form video with ID: 999 not found", exception.getMessage());
    }

    @Test
    void testGetShortFormVideoByRecipeId_Success() {
        // Setup
        when(recipeRepository.findByRecipeId(1)).thenReturn(testRecipe);
        when(shortFormVideoRepository.findByRecipe(testRecipe)).thenReturn(testVideo);
        
        // Execute
        ShortFormVideo retrievedVideo = shortFormVideoService.getShortFormVideoByRecipeId(1);
        
        // Verify
        assertNotNull(retrievedVideo);
        assertEquals(testVideo, retrievedVideo);
        verify(recipeRepository, times(1)).findByRecipeId(1);
        verify(shortFormVideoRepository, times(1)).findByRecipe(testRecipe);
    }

    @Test
    void testGetShortFormVideoByRecipeId_RecipeNotFound() {
        // Setup
        when(recipeRepository.findByRecipeId(999)).thenReturn(null);
        
        // Execute & Verify
        NomNomException exception = assertThrows(NomNomException.class, 
                () -> shortFormVideoService.getShortFormVideoByRecipeId(999));
        
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("Recipe with ID: 999 not found", exception.getMessage());
        verify(shortFormVideoRepository, never()).findByRecipe(any());
    }

    @Test
    void testGetShortFormVideoByRecipeId_VideoNotFound() {
        // Setup
        when(recipeRepository.findByRecipeId(1)).thenReturn(testRecipe);
        when(shortFormVideoRepository.findByRecipe(testRecipe)).thenReturn(null);
        
        // Execute & Verify
        NomNomException exception = assertThrows(NomNomException.class, 
                () -> shortFormVideoService.getShortFormVideoByRecipeId(1));
        
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("No short form video found for recipe with ID: 1", exception.getMessage());
    }

    @Test
    void testGetAllShortFormVideos_Success() {
        // Setup
        ShortFormVideo video2 = new ShortFormVideo(2, "Video 2", "Description 2", "video2.mp4", testRecipe);
        List<ShortFormVideo> videos = Arrays.asList(testVideo, video2);
        when(shortFormVideoRepository.findAll()).thenReturn(videos);
        
        // Execute
        List<ShortFormVideo> retrievedVideos = shortFormVideoService.getAllShortFormVideos();
        
        // Verify
        assertNotNull(retrievedVideos);
        assertEquals(2, retrievedVideos.size());
        assertTrue(retrievedVideos.contains(testVideo));
        assertTrue(retrievedVideos.contains(video2));
        verify(shortFormVideoRepository, times(1)).findAll();
    }

    @Test
    void testDeleteShortFormVideo_Success() {
        // Setup
        when(shortFormVideoRepository.findByVideoId(1)).thenReturn(testVideo);
        
        // Execute
        shortFormVideoService.deleteShortFormVideo(1);
        
        // Verify
        verify(shortFormVideoRepository, times(1)).findByVideoId(1);
        verify(shortFormVideoRepository, times(1)).delete(testVideo);
        verify(recipeRepository, times(1)).save(testRecipe);
    }

    @Test
    void testDeleteShortFormVideo_NotFound() {
        // Setup
        when(shortFormVideoRepository.findByVideoId(999)).thenReturn(null);
        
        // Execute & Verify
        NomNomException exception = assertThrows(NomNomException.class, 
                () -> shortFormVideoService.deleteShortFormVideo(999));
        
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("Short form video with ID: 999 not found", exception.getMessage());
        verify(shortFormVideoRepository, never()).delete(any());
        verify(recipeRepository, never()).save(any());
    }
} 