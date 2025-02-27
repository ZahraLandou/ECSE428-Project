package com.example.nomnomapp.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.nomnomapp.exception.NomNomException;
import com.example.nomnomapp.model.Recipe;
import com.example.nomnomapp.model.ShortFormVideo;
import com.example.nomnomapp.repository.RecipeRepository;
import com.example.nomnomapp.repository.ShortFormVideoRepository;

@Service
public class ShortFormVideoService {

    @Autowired
    private ShortFormVideoRepository shortFormVideoRepository;
    
    @Autowired
    private RecipeRepository recipeRepository;
    
    /**
     * Creates a new short form video
     * 
     * @param shortFormVideo the video to be created
     * @return the created video
     */
    public ShortFormVideo createShortFormVideo(ShortFormVideo shortFormVideo) {
        if (shortFormVideo == null) {
            throw new NomNomException(HttpStatus.BAD_REQUEST, "Short form video cannot be null");
        }
        
        Recipe recipe = shortFormVideo.getRecipe();
        if (recipe == null) {
            throw new NomNomException(HttpStatus.BAD_REQUEST, "Short form video must be associated with a recipe");
        }
        
        // Verify the recipe exists in the database
        Recipe existingRecipe = recipeRepository.findByRecipeId(recipe.getRecipeID());
        if (existingRecipe == null) {
            throw new NomNomException(HttpStatus.NOT_FOUND, "Recipe with ID: " + recipe.getRecipeID() + " not found");
        }
        
        // Check if recipe already has a video
        ShortFormVideo existingVideo = shortFormVideoRepository.findByRecipe(existingRecipe);
        if (existingVideo != null) {
            throw new NomNomException(HttpStatus.CONFLICT, "Recipe already has an associated short form video");
        }
        
        // Ensure the bidirectional relationship is properly set
        shortFormVideo.setRecipe(existingRecipe);
        
        return shortFormVideoRepository.save(shortFormVideo);
    }
    
    /**
     * Retrieves a short form video by its ID
     * 
     * @param videoId the ID of the video to retrieve
     * @return the requested video
     * @throws NomNomException if the video is not found
     */
    public ShortFormVideo getShortFormVideoById(int videoId) {
        ShortFormVideo video = shortFormVideoRepository.findByVideoId(videoId);
        if (video == null) {
            throw new NomNomException(HttpStatus.NOT_FOUND, "Short form video with ID: " + videoId + " not found");
        }
        return video;
    }
    
    /**
     * Retrieves a short form video associated with a specific recipe
     * 
     * @param recipeId the ID of the recipe
     * @return the short form video associated with the recipe
     * @throws NomNomException if the recipe or video is not found
     */
    public ShortFormVideo getShortFormVideoByRecipeId(int recipeId) {
        Recipe recipe = recipeRepository.findByRecipeId(recipeId);
        if (recipe == null) {
            throw new NomNomException(HttpStatus.NOT_FOUND, "Recipe with ID: " + recipeId + " not found");
        }
        
        ShortFormVideo video = shortFormVideoRepository.findByRecipe(recipe);
        if (video == null) {
            throw new NomNomException(HttpStatus.NOT_FOUND, "No short form video found for recipe with ID: " + recipeId);
        }
        
        return video;
    }
    
    /**
     * Retrieves all short form videos
     * 
     * @return a list of all short form videos
     */
    public List<ShortFormVideo> getAllShortFormVideos() {
        return (List<ShortFormVideo>) shortFormVideoRepository.findAll();
    }
    
    /**
     * Deletes a short form video
     * 
     * @param videoId the ID of the video to delete
     * @throws NomNomException if the video is not found
     */
    @Transactional
    public void deleteShortFormVideo(int videoId) {
        ShortFormVideo video = shortFormVideoRepository.findByVideoId(videoId);
        if (video == null) {
            throw new NomNomException(HttpStatus.NOT_FOUND, "Short form video with ID: " + videoId + " not found");
        }
        
        // Handle the relationship before deleting
        Recipe recipe = video.getRecipe();
        if (recipe != null) {
            recipe.setShortFormVideo(null);
            recipeRepository.save(recipe);
        }
        
        shortFormVideoRepository.delete(video);
    }
} 