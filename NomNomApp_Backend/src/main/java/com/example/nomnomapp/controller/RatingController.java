package com.example.nomnomapp.controller;

import com.example.nomnomapp.model.Rating;
import com.example.nomnomapp.service.RatingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.nomnomapp.repository.UserRepository;


@RestController
@RequestMapping("/api/ratings")
public class RatingController {

    private final RatingService ratingService;
    private final UserRepository userRepository;

    public RatingController(RatingService ratingService, UserRepository userRepository) {
        this.ratingService = ratingService;
        this.userRepository = userRepository;
    }

    @PostMapping("/{userId}/{recipeId}")
    public ResponseEntity<?> rateRecipe(
            @PathVariable int userId,
            @PathVariable int recipeId,
            @RequestParam int ratingValue) {
        if (!userRepository.existsById(userId)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Unauthorized: You must be logged in to rate recipes.");
        }

        try {
            Rating rating = ratingService.rateRecipe(userId, recipeId, ratingValue);
            return ResponseEntity.ok(rating);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{userId}/{recipeId}")
    public ResponseEntity<Rating> updateRating(
            @PathVariable int userId,
            @PathVariable int recipeId,
            @RequestParam int newRatingValue) {

        Rating updatedRating = ratingService.updateRating(userId, recipeId, newRatingValue);
        return ResponseEntity.ok(updatedRating);
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
    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<String> handleSecurityException(SecurityException ex) {
        return ResponseEntity.status(401).body(ex.getMessage());
    }

}
