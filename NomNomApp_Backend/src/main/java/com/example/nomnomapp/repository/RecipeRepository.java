package com.example.nomnomapp.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.example.nomnomapp.model.Recipe;
import com.example.nomnomapp.model.Recipe.RecipeCategory;


/**
 * Recipe class repository
 * @author Zahra
 */
public interface RecipeRepository extends CrudRepository<Recipe,Integer> {

    Recipe findRecipeById(Integer id);
    List<Recipe> findRecipeByTitle(String title);
    List<Recipe> findRecipeByCategory(RecipeCategory recipeCategory);

}
