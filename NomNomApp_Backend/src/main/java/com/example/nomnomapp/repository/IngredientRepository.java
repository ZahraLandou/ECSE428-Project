package com.example.nomnomapp.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.example.nomnomapp.model.Ingredient;


/**
 * Ingredient class repository
 * @author Zahra
 */
public interface IngredientRepository extends CrudRepository<Ingredient, String> {
    Ingredient findIngredientByName(String name);
    List<Ingredient> findIngredientByType(String type);   
} 
