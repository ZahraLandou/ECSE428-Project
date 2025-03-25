package com.example.nomnomapp.repository;

import com.example.nomnomapp.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Integer> {



    Optional<Rating> findByNomNomUser_UserIdAndRecipe_RecipeId(int userId, int recipeId);

    @Query("SELECT AVG(r.ratingValue) FROM Rating r WHERE r.recipe.recipeId = :recipeId")
    Double findAverageRatingByRecipeId(@Param("recipeId") int recipeId);

    @Query("SELECT COUNT(r) FROM Rating r WHERE r.recipe.recipeId = :recipeId")
    Long countRatingsByRecipeId(@Param("recipeId") int recipeId);

}
