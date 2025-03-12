package com.example.nomnomapp.controller;

import com.example.nomnomapp.model.Rating;
import com.example.nomnomapp.service.RatingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ratings")
public class RatingController {

    private final RatingService ratingService;

    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @PostMapping("/{userId}/{recipeId}")
    public ResponseEntity<Rating> rateRecipe(
            @PathVariable int userId,
            @PathVariable int recipeId,
            @RequestParam int ratingValue) {
        Rating rating = ratingService.rateRecipe(userId, recipeId, ratingValue);
        return ResponseEntity.ok(rating);
    }

    @GetMapping("/{recipeId}/average")
    public ResponseEntity<Double> getAverageRating(@PathVariable int recipeId) {
        Double averageRating = ratingService.getAverageRating(recipeId);
        return ResponseEntity.ok(averageRating != null ? averageRating : 0.0);
    }

    @GetMapping("/{recipeId}/count")
    public ResponseEntity<Long> getRatingCount(@PathVariable int recipeId) {
        Long ratingCount = ratingService.getRatingCount(recipeId);
        return ResponseEntity.ok(ratingCount != null ? ratingCount : 0L);
    }
}
