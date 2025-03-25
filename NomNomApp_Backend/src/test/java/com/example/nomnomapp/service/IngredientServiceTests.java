package com.example.nomnomapp.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.nomnomapp.model.Ingredient;
import com.example.nomnomapp.repository.IngredientRepository;

/**
 * Test class for the IngredientService class
 * 
 * @author Zahra
 */
@ExtendWith(MockitoExtension.class)
public class IngredientServiceTests {

    @Mock
    private IngredientRepository ingredientRepository;

    @InjectMocks
    private IngredientService ingredientService;

    private Ingredient shrimp;
    private Ingredient garlic;

    @BeforeEach
    void setUp() {
        // create test ingredients
        shrimp = new Ingredient("shrimp", "seafood");
        garlic = new Ingredient("garlic", "spice");
    }

    @AfterEach
    void cleanup() {
        // reset mock to avoid interference between the tests
        reset(ingredientRepository);
    }

    // CREATE TESTS

    /**
     * Successfully create ingredient
     */
    @Test
    public void testCreateIngredient_Success() {
        Ingredient newIngredient = new Ingredient("apple", "fruit");
        when(ingredientRepository.findIngredientByName("apple")).thenReturn(Optional.empty());
        when(ingredientRepository.save(any(Ingredient.class))).thenReturn(newIngredient);

        Ingredient savedIngredient = ingredientService.createIngredient("apple", "fruit");

        assertNotNull(savedIngredient);
        assertEquals("apple", savedIngredient.getName());
        assertEquals("fruit", savedIngredient.getType());
    }

    /**
     * Error case: Attempting to create an ingredient with a duplicate name
     */
    @Test
    public void testCreateIngredient_Duplicate() {
        when(ingredientRepository.findIngredientByName("shrimp")).thenReturn(Optional.of(shrimp));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            ingredientService.createIngredient("shrimp", "seafood");
        });

        assertEquals("Ingredient with name 'shrimp' already exists", exception.getMessage());
    }

    /**
     * Error case: Attempting to create an ingredient with a invalid name
     */
    @Test
    public void testCreateIngredient_InvalidName() {

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            ingredientService.createIngredient("", "seafood");
        });

        assertEquals("Ingredient name cannot be empty", exception.getMessage());
    }

    /**
     * Error case: Attempting to create an ingredient with a invalid type
     */
    @Test
    public void testCreateIngredient_InvalidType() {

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            ingredientService.createIngredient("lobster", "");
        });

        assertEquals("Ingredient type cannot be empty", exception.getMessage());
    }

    // READ TESTS

    /** Successfully get an ingredient by name */
    @Test
    public void testGetIngredientByName_Success() {
        when(ingredientRepository.findIngredientByName("garlic")).thenReturn(Optional.of(garlic));

        Ingredient foundIngredient = ingredientService.getIngredientByName("garlic");

        assertNotNull(foundIngredient);
        assertEquals("garlic", foundIngredient.getName());
        assertEquals("spice", foundIngredient.getType());
    }

    /** Error case: Attempting to get a non-existing ingredient */
    @Test
    public void testGetIngredientByName_IngredientNotFound() {
        when(ingredientRepository.findIngredientByName("onion")).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            ingredientService.getIngredientByName("onion");
        });

        assertEquals("Ingredient with name 'onion' not found", exception.getMessage());
    }

    /** Test retrieving all ingredients */
    @Test
    public void testGetAllIngredients() {
        List<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(shrimp);
        ingredients.add(garlic);

        when(ingredientRepository.findAll()).thenReturn(ingredients);

        List<Ingredient> result = ingredientService.getAllIngredients();
        assertEquals(2, result.size());
        assertTrue(result.contains(shrimp));
        assertTrue(result.contains(garlic));
    }

    // UPDATE TESTS

    /**
     * Successfully update the name of an ingredient
     */
    @Test
    public void testUpdateIngredientName_Success() {

        when(ingredientRepository.findIngredientByIngredientId(1)).thenReturn(shrimp);
        when(ingredientRepository.findIngredientByName("prawn")).thenReturn(Optional.empty()); 
        when(ingredientRepository.save(any(Ingredient.class))).thenReturn(shrimp);

        Ingredient updatedIngredient = ingredientService.updateIngredientById(1, "prawn", null);

        assertNotNull(updatedIngredient);
        assertEquals("prawn", updatedIngredient.getName());
        assertEquals("seafood", updatedIngredient.getType()); // type remains unchanged
    }

    /**
     * Error case: update ingredient name with an empty string
     */
    @Test
    public void testUpdateIngredientName_InvalidName() {
        when(ingredientRepository.findIngredientByIngredientId(1)).thenReturn(shrimp);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            ingredientService.updateIngredientById(1, "", null);
        });

        assertEquals("All ingredient fields are empty", exception.getMessage());
    }

    /**
     * Error case: update ingredient name using an already existing ingredient name
     */
    @Test
    public void testUpdateIngredientName_DuplicateName() {
        when(ingredientRepository.findIngredientByIngredientId(1)).thenReturn(shrimp);
        when(ingredientRepository.findIngredientByName("garlic")).thenReturn(Optional.of(garlic)); // name conflict

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            ingredientService.updateIngredientById(1, "garlic", null);
        });

        assertEquals("Ingredient with name 'garlic' already exists", exception.getMessage());
    }

    /**
     * Successfully update the type of an ingredient
     */
    @Test
    public void testUpdateIngredientType_Success() {
        when(ingredientRepository.findIngredientByIngredientId(1)).thenReturn(shrimp);
        when(ingredientRepository.save(any(Ingredient.class))).thenReturn(shrimp);

        Ingredient updatedIngredient = ingredientService.updateIngredientById(1, null, "protein");

        assertNotNull(updatedIngredient);
        assertEquals("shrimp", updatedIngredient.getName()); // name remains unchanged
        assertEquals("protein", updatedIngredient.getType());
    }

    /**
     * Error case: update ingredient type with an empty string
     */
    @Test
    public void testUpdateIngredientType_InvalidType() {
        when(ingredientRepository.findIngredientByIngredientId(1)).thenReturn(shrimp);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            ingredientService.updateIngredientById(1, null, "");
        });

        assertEquals("All ingredient fields are empty", exception.getMessage());
    }

    // DELETE TESTS

    /**
     * Successfully deleting an ingredient
     */
    @Test
    public void testDeleteIngredient_Success() {
        when(ingredientRepository.findIngredientByIngredientId(1)).thenReturn(shrimp);

        Ingredient deletedIngredient = ingredientService.deleteIngredient(1);

        assertNotNull(deletedIngredient);
        assertEquals("shrimp", deletedIngredient.getName());
        assertEquals("seafood", deletedIngredient.getType());
    }

    /**
     * Error case: deleting a non-existing ingredient
     */
    @Test
    public void testDeleteIngredient_IngredientNotFound() {
        when(ingredientRepository.findIngredientByIngredientId(999)).thenReturn(null);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            ingredientService.deleteIngredient(999);
        });

        assertEquals("Ingredient does not exist", exception.getMessage());
    }
}
