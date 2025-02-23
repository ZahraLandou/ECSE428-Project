package com.example.nomnomapp.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.nomnomapp.model.Ingredient;
import com.example.nomnomapp.repository.IngredientRepository;

@Service
public class IngredientService {

    @Autowired
    private IngredientRepository ingredientRepository;

    /**
     * Method to create an ingredient
     * 
     * @param name - name of ingredient, it should be unique
     * @param type - type of ingredient
     * @return ingredient
     * @author Zahra
     * 
     */
    @Transactional
    public Ingredient createIngredient(String name, String type) {

        if (name == null || name.trim().length() == 0) {
            throw new IllegalArgumentException("Ingredient name cannot be empty");
        }

        if (type == null || type.trim().length() == 0) {
            throw new IllegalArgumentException("Ingredient type cannot be empty");
        }

        String normalizedName = name.trim().toLowerCase(); // turn name to lower case and remove space

        // check if an ingredient with the same name already exists
        if (ingredientRepository.findIngredientByName(normalizedName).isPresent()) {
            throw new IllegalArgumentException("Ingredient with name '" + name + "' already exists");
        }

        Ingredient ingredient = new Ingredient(name, type);

        return ingredientRepository.save(ingredient);
    }

    public Ingredient getIngredient(int ingredientId) {
        return ingredientRepository.findIngredientByIngredientId(ingredientId);
    }

    public List<Ingredient> getAllIngredients() {
        return (List<Ingredient>) ingredientRepository.findAll();
    }

    public Ingredient getIngredientByName(String name) {

        if (name == null || name.trim().length() == 0) {
            throw new IllegalArgumentException("Ingredient name cannot be empty");
        }

        return ingredientRepository.findIngredientByName(name)
                .orElseThrow(() -> new IllegalArgumentException("Ingredient with name " + name + " not found"));
    }

    @Transactional
    public Ingredient updateIngredient(int ingredientId, String name, String type) {
        Ingredient ingredient = ingredientRepository.findIngredientByIngredientId(ingredientId);
        if (ingredient == null) {
            throw new IllegalArgumentException("Ingredient does not exist");
        }
        if ((name == null || name.trim().length() == 0) && (type == null || type.trim().length() == 0)) {
            throw new IllegalArgumentException("All ingredient fields are empty");
        }

        if (name != null && name.trim().length() != 0) {

            name = name.toLowerCase();
            // check if an ingredient with the same name already exists
            if (ingredientRepository.findIngredientByName(name).isPresent()) {
                throw new IllegalArgumentException("Ingredient with name '" + name + "' already exists");
            }

            ingredient.setName(name);
        }

        if (type != null && type.trim().length() != 0) {
            type = type.toLowerCase();
            ingredient.setType(type);
        }
        return ingredientRepository.save(ingredient);
    }

    @Transactional
    public Ingredient deleteIngredient(int ingredientId) {
        Ingredient ingredient = ingredientRepository.findIngredientByIngredientId(ingredientId);
        if (ingredient == null) {
            throw new IllegalArgumentException("Ingredient does not exist");
        }
        // note : might want to remove recipeingredient depending on this ingredient

        ingredientRepository.deleteIngredientByIngredientId(ingredientId);
        return ingredient;
    }

    public String getIngredientName(int ingredientId) {

        Ingredient ingredient = ingredientRepository.findIngredientByIngredientId(ingredientId);
        if (ingredient == null) {
            throw new IllegalArgumentException("Ingredient does not exist");
        }
        return ingredient.getName();
    }

    public String getIngredientType(int ingredientId) {
        Ingredient ingredient = ingredientRepository.findIngredientByIngredientId(ingredientId);
        if (ingredient == null) {
            throw new IllegalArgumentException("Ingredient does not exist");
        }

        return ingredient.getType();
    }

}
