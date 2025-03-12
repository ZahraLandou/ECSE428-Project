package com.example.nomnomapp.service;

import com.example.nomnomapp.model.Rating;
import com.example.nomnomapp.model.Recipe;
import com.example.nomnomapp.model.NomNomUser;
import com.example.nomnomapp.repository.RatingRepository;
import com.example.nomnomapp.repository.RecipeRepository;
import com.example.nomnomapp.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public Rating rateRecipe(int userId, int recipeId, int ratingValue) {
        if (ratingValue < 0 || ratingValue > 5) {
            throw new IllegalArgumentException("Rating must be between 0 and 5.");
        }

        NomNomUser user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new IllegalArgumentException("Recipe not found"));

        Optional<Rating> existingRating = ratingRepository.findByNomNomUser_UserIdAndRecipe_RecipeId(userId, recipeId);

        Rating rating;
        if (existingRating.isPresent()) {
            rating = existingRating.get();
            rating.setRatingValue(ratingValue);
        } else {
            rating = new Rating(user, recipe, ratingValue);
        }

        return ratingRepository.save(rating);
    }

    public Double getAverageRating(int recipeId) {
        return ratingRepository.findAverageRatingByRecipeId(recipeId);
    }

    public Long getRatingCount(int recipeId) {
        return ratingRepository.countRatingsByRecipeId(recipeId);
    }
}
