package com.example.nomnomapp.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.orm.jpa.JpaSystemException;

import com.example.nomnomapp.model.Recipe;
import com.example.nomnomapp.model.ShortFormVideo;
import com.example.nomnomapp.model.NomNomUser;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

/**
 * Test class for ShortFormVideoRepository
 * Tests the repository methods for retrieving ShortFormVideo entities
 */
@SpringBootTest
@Transactional
public class ShortFormVideoRepositoryTests {

    @Autowired
    private ShortFormVideoRepository shortFormVideoRepository;

    @Autowired
    private RecipeRepository recipeRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Clean up any entities to prevent test interference
     */
    @AfterEach
    public void cleanup() {
        try {
            // First manually break bidirectional relationships
            Iterable<ShortFormVideo> videosIterable = shortFormVideoRepository.findAll();
            List<ShortFormVideo> videos = new ArrayList<>();
            videosIterable.forEach(videos::add);
            
            for (ShortFormVideo video : videos) {
                Recipe recipe = video.getRecipe();
                if (recipe != null) {
                    recipe.setShortFormVideo(null);
                    recipeRepository.save(recipe);
                }
            }
            
            // Force flush to ensure relationships are updated in the database
            entityManager.flush();
            
            // Now delete in the correct order
            shortFormVideoRepository.deleteAll();
            recipeRepository.deleteAll();
            userRepository.deleteAll();
        } catch (Exception e) {
            // Just log the exception during cleanup - test will still complete
            System.err.println("Cleanup error (can be ignored for test results): " + e.getMessage());
        }
    }

    /**
     * Test for finding a ShortFormVideo by its ID
     */
    @Test
    public void testFindByVideoId() {
        // Create a user for the recipe
        NomNomUser user = new NomNomUser();
        user.setUsername("videoTestUser");
        user.setEmailAddress("videotest@example.com");
        user.setPassword("password");
        user = userRepository.save(user);
        
        // Create a recipe for the video
        Recipe recipe = new Recipe();
        recipe.setTitle("Video Test Recipe");
        recipe.setInstructions("Test Instructions");
        recipe.setDescription("Test Description");
        recipe.setCreationDate(new Date(System.currentTimeMillis()));
        recipe.setNomNomUser(user);
        recipe = recipeRepository.save(recipe);

        // Create a short form video
        ShortFormVideo video = new ShortFormVideo(0, "Test Video", "Test Description", "video_url.mp4", recipe);
        video = shortFormVideoRepository.save(video);
        
        // Make sure the bidirectional relationship is properly established
        recipe.setShortFormVideo(video);
        recipe = recipeRepository.save(recipe);
        
        int savedVideoId = video.getVideoId();

        // Retrieve the video by ID
        ShortFormVideo retrievedVideo = shortFormVideoRepository.findByVideoId(savedVideoId);

        // Assertions
        assertNotNull(retrievedVideo);
        assertEquals(savedVideoId, retrievedVideo.getVideoId());
        assertEquals("Test Video", retrievedVideo.getVideoTitle());
        assertEquals("Test Description", retrievedVideo.getVideoDescription());
        assertEquals("video_url.mp4", retrievedVideo.getVideo());
    }

    /**
     * Test for finding a ShortFormVideo by associated Recipe
     */
    @Test
    public void testFindByRecipe() {
        // Create a user for the recipe with completely unique identifier
        NomNomUser user = new NomNomUser();
        user.setUsername("recipeVideo123");
        user.setEmailAddress("recipe_video123@example.com");
        user.setPassword("password");
        user = userRepository.save(user);
        
        // Create a recipe for the video
        Recipe recipe = new Recipe();
        recipe.setTitle("Recipe for Video Test");
        recipe.setInstructions("Recipe Instructions for Video");
        recipe.setDescription("Recipe Description for Video");
        recipe.setCreationDate(new Date(System.currentTimeMillis()));
        recipe.setNomNomUser(user);
        recipe = recipeRepository.save(recipe);

        // Create a short form video
        ShortFormVideo video = new ShortFormVideo(0, "Recipe Video", "Video for Recipe Test", "recipe_video_test.mp4", recipe);
        video = shortFormVideoRepository.save(video);
        
        // Make sure the bidirectional relationship is properly established
        recipe.setShortFormVideo(video);
        recipe = recipeRepository.save(recipe);

        // Retrieve the video by recipe
        ShortFormVideo retrievedVideo = shortFormVideoRepository.findByRecipe(recipe);

        // Assertions
        assertNotNull(retrievedVideo);
        assertEquals(video.getVideoId(), retrievedVideo.getVideoId());
        assertEquals("Recipe Video", retrievedVideo.getVideoTitle());
        assertEquals(recipe.getRecipeID(), retrievedVideo.getRecipe().getRecipeID());
    }
} 