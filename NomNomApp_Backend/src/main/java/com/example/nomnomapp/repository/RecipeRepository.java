
package com.example.nomnomapp.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.nomnomapp.model.Recipe;
import com.example.nomnomapp.model.Recipe.RecipeCategory;


/**
 * Recipe class repository
 * @author Zahra
 */
@Repository
public interface RecipeRepository extends CrudRepository<Recipe,Integer> {

    Recipe findRecipeById(Integer id);
    List<Recipe> findRecipeByTitle(String title);
    List<Recipe> findRecipeByCategory(RecipeCategory recipeCategory);

}
