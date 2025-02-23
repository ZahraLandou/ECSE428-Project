package com.example.nomnomapp.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
    public void clearDatabase(){
        ingredientRepository.deleteAll();
    }

    @Test
    public void testPersistAndLoadIngredient(){

        Ingredient shrimp = new Ingredient("shrimp", "seafood");
        Ingredient garlic = new Ingredient("garlic", "spice");

        shrimp=ingredientRepository.save(shrimp);
        garlic = ingredientRepository.save(garlic);

        Ingredient savedShrimp = ingredientRepository.findIngredientByName("shrimp");
        Ingredient savedGarlic = ingredientRepository.findIngredientByName("garlic");
        assertNotNull(savedShrimp);
        assertNotNull(savedGarlic);
        assertEquals(shrimp.getType(), savedShrimp.getType());
        assertEquals(garlic.getType(), savedGarlic.getType());
    }


    
}
