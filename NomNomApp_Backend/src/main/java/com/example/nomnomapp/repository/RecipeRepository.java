
package com.example.nomnomapp.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.nomnomapp.model.Recipe;
import com.example.nomnomapp.model.NomNomUser;
import com.example.nomnomapp.model.Recipe.RecipeCategory;


    /**
     * Recipe class repository
     * @author Zahra
     */
    public interface RecipeRepository extends CrudRepository<Recipe,Integer> {
        Recipe findByRecipeId(Integer recipeId);
        List<Recipe> findRecipeByTitle(String title);
        List<Recipe> findRecipeByCategory(RecipeCategory recipeCategory);

    /**
     * Find all recipes created by a specific user
     * @param nomNomUser the user whose recipes to find
     * @return list of recipes created by the user
     */
    List<Recipe> findByNomNomUser(NomNomUser nomNomUser);


        /**
         * Find all recipes for a given user ID
         * @param userId the ID of the user whose recipes to find
         * @return list of recipes created by the user with the given ID
         */
        @Query("SELECT r FROM Recipe r WHERE r.nomNomUser.userId = :userId")
        List<Recipe> findByUserId(@Param("userId") int userId);

        /**
         * Find most recent recipes by a user
         * @param nomNomUser the user whose recipes to find
         * @param limit the maximum number of recipes to return
         * @return list of most recent recipes by the user, limited to the specified number
         */
        @Query("SELECT r FROM Recipe r WHERE r.nomNomUser = :user ORDER BY r.creationDate DESC")
        List<Recipe> findRecentRecipesByUser(@Param("user") NomNomUser user, @Param("limit") int limit);

}
