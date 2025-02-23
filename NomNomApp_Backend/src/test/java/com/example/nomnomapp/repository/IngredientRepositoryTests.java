package com.example.nomnomapp.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.nomnomapp.model.Ingredient;

/**
 * Test class for IngredientRepository
 * 
 * @author Zahra
 */
@SpringBootTest
public class IngredientRepositoryTests {

    @Autowired
    IngredientRepository ingredientRepository;

    @AfterEach
    public void clearDatabase() {
        ingredientRepository.deleteAll();
    }

    @Test
    public void testPersistAndLoadIngredient() {

        Ingredient shrimp = new Ingredient("shrimp", "seafood");
        Ingredient garlic = new Ingredient("garlic", "spice");

        shrimp = ingredientRepository.save(shrimp);
        garlic = ingredientRepository.save(garlic);

        Optional<Ingredient> savedShrimp = ingredientRepository.findIngredientByName("shrimp");
        Optional<Ingredient> savedGarlic = ingredientRepository.findIngredientByName("garlic");

        assertTrue(savedShrimp.isPresent(), "Shrimp should exist in database");
        assertTrue(savedGarlic.isPresent(), "Garlic should exist in database");

        assertEquals(shrimp.getType(), savedShrimp.get().getType());
        assertEquals(garlic.getType(), savedGarlic.get().getType());
    }

}
