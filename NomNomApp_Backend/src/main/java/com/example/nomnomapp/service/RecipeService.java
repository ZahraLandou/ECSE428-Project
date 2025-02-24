package com.example.nomnomapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;

import com.example.nomnomapp.exception.NomNomException;
import com.example.nomnomapp.model.Recipe;
import com.example.nomnomapp.model.Comment;
import com.example.nomnomapp.repository.RecipeRepository;

import java.util.List;
import java.util.OptionalDouble;

public class RecipeService {
    @Autowired
    private RecipeRepository recipeRepository;

    @Transactional
    public Recipe updateAverageRating(int aRecipeID){
        if (aRecipeID<0) {
            throw new NomNomException(HttpStatus.BAD_REQUEST, "Recipe ID is not valid.");
        }
        Recipe recipe = recipeRepository.findRecipeById(aRecipeID);
        List<Comment> comments = recipe.getComments();
        OptionalDouble averageRating = comments.stream().mapToDouble(Comment::getRating).average();
        if(averageRating.isPresent()){
            // mapToDouble generated a non-empty list
            // https://docs.oracle.com/javase/8/docs/api/java/util/stream/DoubleStream.html#average--
            if(averageRating.getAsDouble() == Double.NaN){
                throw new NomNomException(HttpStatus.INTERNAL_SERVER_ERROR, "Averaging Ratings returned a NaN");
            }else{
                recipe.setAverageRating(averageRating.getAsDouble());
            }
        }else{
            // mapToDouble generated an empty list
            recipe.setAverageRating(0);
        }
        return recipe;
    }
}
