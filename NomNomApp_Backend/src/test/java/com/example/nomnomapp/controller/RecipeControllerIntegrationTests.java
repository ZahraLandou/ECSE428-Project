package com.example.nomnomapp.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.nomnomapp.model.Ingredient;
import com.example.nomnomapp.model.NomNomUser;
import com.example.nomnomapp.model.RecipeIngredients;
import com.example.nomnomapp.repository.IngredientRepository;
import com.example.nomnomapp.repository.RecipeIngredientsRepository;
import com.example.nomnomapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.example.nomnomapp.model.Recipe;
import com.example.nomnomapp.model.Recipe.RecipeCategory;
import com.example.nomnomapp.repository.RecipeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.Date;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class RecipeControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RecipeRepository recipeRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private RecipeIngredientsRepository recipeIngredientsRepository;

    @Autowired
    private ObjectMapper objectMapper; // For converting objects to JSON

    @BeforeEach
    void setUp() {
        // Create and save a test user
        NomNomUser testUser = new NomNomUser();
        testUser.setUsername("Manon");
        testUser.setEmailAddress("test@testing.com");
        testUser.setPassword("abc123password");
        testUser = userRepository.save(testUser);

        // Create and save test recipes with ingredients
        // Recipe 1
        Recipe testRecipe0 = new Recipe();
        testRecipe0.setTitle("Pasta Carbonara");
        testRecipe0.setDescription("Classic Italian dish");
        testRecipe0.setInstructions("Cook pasta, add eggs and cheese");
        testRecipe0.setCategory(RecipeCategory.Dinner);
        testRecipe0.setNomNomUser(testUser);
        testRecipe0.setCreationDate(new Date(System.currentTimeMillis()));
        testRecipe0 = recipeRepository.save(testRecipe0);

        Ingredient ingredient0 = ingredientRepository.save(new Ingredient("pasta", "glucid"));
        Ingredient ingredient1 = ingredientRepository.save(new Ingredient("eggs", "protein"));
        Ingredient ingredient2 = ingredientRepository.save(new Ingredient("cheese", "protein"));

        RecipeIngredients recipeIngredient0 = new RecipeIngredients(400.00, "mg", testRecipe0, ingredient0);
        recipeIngredient0.setRecipe(testRecipe0); // Explicitly set recipe
        RecipeIngredients recipeIngredient1 = new RecipeIngredients(4.00, "count", testRecipe0, ingredient1);
        recipeIngredient1.setRecipe(testRecipe0);
        RecipeIngredients recipeIngredient2 = new RecipeIngredients(15.00, "mg", testRecipe0, ingredient2);
        recipeIngredient2.setRecipe(testRecipe0);

        testRecipe0.addRecipeIngredient(recipeIngredient0);
        testRecipe0.addRecipeIngredient(recipeIngredient1);
        testRecipe0.addRecipeIngredient(recipeIngredient2);

        testRecipe0 = recipeRepository.save(testRecipe0);

        // Recipe 2
        Recipe testRecipe1 = new Recipe();
        testRecipe1.setTitle("Avocado Toast");
        testRecipe1.setDescription("Healthy breakfast");
        testRecipe1.setInstructions("Toast the bread, mash the avocado with lemon and spread it.");
        testRecipe1.setCategory(RecipeCategory.Breakfast);
        testRecipe1.setCreationDate(new Date(System.currentTimeMillis()));

        testRecipe1.setNomNomUser(testUser);
        testRecipe1 = recipeRepository.save(testRecipe1);

        Ingredient ingredient3 = ingredientRepository.save(new Ingredient("bread", "glucid"));
        Ingredient ingredient4 = ingredientRepository.save(new Ingredient("avocado", "fruit"));
        Ingredient ingredient5 = ingredientRepository.save(new Ingredient("lemon", "fruit"));

        RecipeIngredients recipeIngredient3 = new RecipeIngredients(1.00, "slice", testRecipe1, ingredient3);
        recipeIngredient3.setRecipe(testRecipe1);
        RecipeIngredients recipeIngredient4 = new RecipeIngredients(0.5, "count", testRecipe1, ingredient4);
        recipeIngredient4.setRecipe(testRecipe1);
        RecipeIngredients recipeIngredient5 = new RecipeIngredients(1, "tsp", testRecipe1, ingredient5);
        recipeIngredient5.setRecipe(testRecipe1);

        testRecipe1.addRecipeIngredient(recipeIngredient3);
        testRecipe1.addRecipeIngredient(recipeIngredient4);
        testRecipe1.addRecipeIngredient(recipeIngredient5);
        testRecipe1 = recipeRepository.save(testRecipe1);
    }

    // @AfterEach
    // void tearDown() {
    //     // Clean up database after each test
    //     recipeRepository.deleteAll();
    //     userRepository.deleteAll();
    // }

    // Test retrieving all recipes (GET /recipes)
    @Test
    void testGetAllRecipes() throws Exception {
        mockMvc.perform(get("/api/recipes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].title").value("Pasta Carbonara"));
    }

    // Test retrieving a recipe by title (GET /recipes)
    @Test
    void testGetRecipeByTitle() throws Exception {
        mockMvc.perform(get("/api/recipes")
                        .param("title", "Pasta Carbonara"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].title").value("Pasta Carbonara"));
    }

    // Test retrieving recipes by category (GET /recipes/category/{category})
    @Test
    void testGetRecipesByCategory() throws Exception {
        mockMvc.perform(get("/api/recipes/category/Dinner"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].category").value("Dinner"));
    }

    // Test creating a new recipe (POST /recipes)
    @Test
    void testCreateRecipe() throws Exception {
        NomNomUser testUser = userRepository.findByUsername("Manon").orElseThrow();
        Recipe newRecipe = new Recipe("Garlic Butter Shrimp", "Delicious seafood dish",
                "Cook shrimp with garlic and butter", new Date(System.currentTimeMillis()),
                RecipeCategory.Dinner, 0, null, 0.0, testUser);

        mockMvc.perform(post("/api/recipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newRecipe)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Garlic Butter Shrimp"));
    }

    // Test getting a recipe by ingredients (GET /recipes/ingredients)
    @Test
    void testGetRecipesByIngredients() throws Exception {
        mockMvc.perform(get("/api/recipes/ingredientNames/{ingredient}", "ingredient")
                        .param("ingredientNames", "garlic,shrimp"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].title").value("Pasta Carbonara"));
    }
}   
