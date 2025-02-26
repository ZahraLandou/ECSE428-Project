package com.example.nomnomapp.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.nomnomapp.model.Ingredient;
import com.example.nomnomapp.model.Recipe;
import com.example.nomnomapp.model.RecipeIngredients;

/**
 * Test class for RecipeRepository
 * 
 * @author Zahra
 */
@SpringBootTest
public class RecipeRepositoryTests {
    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private IngredientRepository ingredientRepository;

    @AfterEach
    public void clearDatabase() {
        recipeRepository.deleteAll();
        ingredientRepository.deleteAll();
    }

    @Test
    public void testPersistAndLoadRecipe() {

        /*
         * TODO: Create and save user
         */

        // Create recipe and its attributes

        // create and save ingredients
        Ingredient shrimp = new Ingredient("shrimp", "seafood");
        Ingredient garlic = new Ingredient("garlic", "spice");
        Ingredient paprika = new Ingredient("paprika", "spice");
        Ingredient salt = new Ingredient("salt", "spice");
        Ingredient pepper = new Ingredient("pepper", "spice");
        Ingredient lemonJuice = new Ingredient("lemon", "juice");
        Ingredient butter = new Ingredient("butter", "dairy");
        Ingredient parsley = new Ingredient("parsley", "herb");

        shrimp = ingredientRepository.save(shrimp);
        garlic = ingredientRepository.save(garlic);
        paprika = ingredientRepository.save(paprika);
        salt = ingredientRepository.save(salt);
        pepper = ingredientRepository.save(pepper);
        lemonJuice = ingredientRepository.save(lemonJuice);
        butter = ingredientRepository.save(butter);
        parsley = ingredientRepository.save(parsley);

        Recipe recipe = new Recipe();
        String recipeTitle = "Garlic Butter Shrimp";
        String instructions = "1. Melt butter in a pan over medium heat.\n" +
                "2. Add garlic and sauté for 30 seconds.\n" +
                "3. Toss in shrimp, paprika, salt, and pepper. Cook for 2-3 minutes per side.\n" +
                "4. Add lemon juice, stir, and remove from heat.\n" +
                "5. Garnish with parsley and serve.";
        String description = "This is my old great-uncle's recipe.";

        // create recipe ingredient relationships
        RecipeIngredients shrimpRI = new RecipeIngredients(1.00, "lb", recipe, shrimp);
        RecipeIngredients garlicRI = new RecipeIngredients(4.00, "cloves", recipe, garlic);
        RecipeIngredients paprikaRI = new RecipeIngredients(1.00, "tsp", recipe, paprika);
        RecipeIngredients saltRI = new RecipeIngredients(1.00, "tsp", recipe, salt);
        RecipeIngredients pepperRI = new RecipeIngredients(1.00, "tsp", recipe, pepper);
        RecipeIngredients lemonJuiceRI = new RecipeIngredients(1.00, "tbsp", recipe, lemonJuice);
        RecipeIngredients butterRI = new RecipeIngredients(3.00, "tbsp", recipe, butter);
        RecipeIngredients parsleyRI = new RecipeIngredients(1.00, "tbsp", recipe, parsley);


        // add ingredients to the recipe
        recipe.addRecipeIngredient(shrimpRI);
        recipe.addRecipeIngredient(garlicRI);
        recipe.addRecipeIngredient(paprikaRI);
        recipe.addRecipeIngredient(saltRI);
        recipe.addRecipeIngredient(pepperRI);
        recipe.addRecipeIngredient(lemonJuiceRI);
        recipe.addRecipeIngredient(butterRI);
        recipe.addRecipeIngredient(parsleyRI);

        // set recipe attributes
        recipe.setTitle(recipeTitle);
        recipe.setInstructions(instructions);
        recipe.setDescription(description);

        // save object to database (recipe ingredients automatically saved)
        recipe = recipeRepository.save(recipe);

        // retrieve recipe by ID
        Recipe savedRecipe = recipeRepository.findByRecipeId(recipe.getRecipeID());

        // assert that object has correct attributes
        assertNotNull(savedRecipe);
        assertEquals(recipeTitle, savedRecipe.getTitle());
        assertEquals(description, savedRecipe.getDescription());
        assertEquals(instructions, savedRecipe.getInstructions());
        assertEquals(8, savedRecipe.getRecipeIngredients().size());

    }

}
