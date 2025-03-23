package com.example.nomnomapp.service;

import com.example.nomnomapp.model.Rating;
import com.example.nomnomapp.model.Recipe;
import com.example.nomnomapp.model.NomNomUser;
import com.example.nomnomapp.repository.RatingRepository;
import com.example.nomnomapp.repository.RecipeRepository;
import com.example.nomnomapp.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;


import java.util.Optional;

@Service
public class RatingService {

    private final RatingRepository ratingRepository;
    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;

    public RatingService(RatingRepository ratingRepository, RecipeRepository recipeRepository, UserRepository userRepository) {
        this.ratingRepository = ratingRepository;
        this.recipeRepository = recipeRepository;
        this.userRepository = userRepository;
    }

    /**
     * Allows a user to rate a recipe. If the user has already rated the recipe, their rating is updated.
     *
     * @param userId The ID of the user submitting the rating.
     * @param recipeId The ID of the recipe being rated.
     * @param ratingValue The rating value (0 to 5).
     * @return The saved rating entity.
     */
    @Transactional
    public Rating rateRecipe(int userId, int recipeId, int ratingValue) {
        if (userId == 0) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You must log in to rate recipes.");
        }

        if (ratingValue < 0 || ratingValue > 5) {
            throw new IllegalArgumentException("Rating must be between 0 and 5.");
        }

        NomNomUser user = userRepository.findById(userId)
                .orElseThrow(() -> new SecurityException("User not found"));

        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new IllegalArgumentException("Recipe not found"));

        Optional<Rating> existingRating = ratingRepository.findByNomNomUser_UserIdAndRecipe_RecipeId(userId, recipeId);

        Rating rating;
        if (existingRating.isPresent()) {
            rating = existingRating.get();
            rating.setRatingValue(ratingValue);
        } else {
            rating = new Rating(user, recipe, ratingValue);
            recipe.getRatings().add(rating);
        }
        recipe.setAverageRating(recipe.calculateAverageRatingForRating());
        recipeRepository.save(recipe);
        return ratingRepository.save(rating);
    }

    /**
     * Fetches the average rating of a recipe.
     *
     * @param recipeId The ID of the recipe.
     * @return The average rating as a double. Returns 0 if no ratings exist.
     */
    public Double getAverageRating(int recipeId) {
        Double avgRating = ratingRepository.findAverageRatingByRecipeId(recipeId);
        System.out.println("Backend calculated average: " + avgRating);
        return (avgRating != null) ? avgRating : 0.0;
    }

    /**
     * Fetches the total number of ratings for a recipe.
     *
     * @param recipeId The ID of the recipe.
     * @return The number of ratings.
     */
    public Long getRatingCount(int recipeId) {
        return ratingRepository.countRatingsByRecipeId(recipeId);
    }

    /**
     * Retrieves the rating a specific user gave to a specific recipe.
     *
     * @param userId The ID of the user.
     * @param recipeId The ID of the recipe.
     * @return The rating value if found, otherwise throws an exception.
     */
    public int getUserRating(int userId, int recipeId) {
        return ratingRepository.findByNomNomUser_UserIdAndRecipe_RecipeId(userId, recipeId)
                .map(Rating::getRatingValue)
                .orElseThrow(() -> new IllegalArgumentException("No rating found for this user and recipe."));
    }

    /**
     * Submits a rating for a recipe by a user.
     * If the user already rated the recipe, the rating is updated.
     *
     * @param userId The ID of the user submitting the rating.
     * @param recipeId The ID of the recipe being rated.
     * @param ratingValue The rating value (0 to 5).
     * @return The updated or new Rating entity.
     */
    @Transactional
    public Rating submitRating(int userId, int recipeId, int ratingValue) {
        return rateRecipe(userId, recipeId, ratingValue);
    }
    public Optional<Rating> findUserRating(int userId, int recipeId) {
        return ratingRepository.findByNomNomUser_UserIdAndRecipe_RecipeId(userId, recipeId);
    }

    @Transactional
    public Rating updateRating(int userId, int recipeId, int newRatingValue) {
        if (newRatingValue < 0 || newRatingValue > 5) {
            throw new IllegalArgumentException("Rating must be between 0 and 5.");
        }
        Rating rating = ratingRepository.findByNomNomUser_UserIdAndRecipe_RecipeId(userId, recipeId)
                .orElseThrow(() -> new IllegalArgumentException("Rating not found for user and recipe"));

        rating.setRatingValue(newRatingValue);

        return ratingRepository.save(rating);
    }

}
