package com.example.nomnomapp.stepdefinitions;

import static org.junit.jupiter.api.Assertions.*;

import com.example.nomnomapp.model.RecipeList;
import com.example.nomnomapp.model.Recipe;
import com.example.nomnomapp.model.NomNomUser;

import com.example.nomnomapp.service.RecipeListService;
import com.example.nomnomapp.service.RecipeService;
import com.example.nomnomapp.service.UserService;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

public class ViewCollectionsStepDefinitions {

    @Autowired
    private RecipeListService recipeListService;

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private UserService userService;

    private final Map<String, Recipe> recipeDatabase = new HashMap<>(); // Simulated recipe storage
    private final Map<String, RecipeList> collectionDatabase = new HashMap<>(); // Simulated recipe storage
    private Exception exception;

    @Before
    public void setUp() {
        recipeDatabase.clear();
        collectionDatabase.clear();
        exception = null;
    }

    @After
    public void cleanUp() {
        recipeDatabase.clear();
        collectionDatabase.clear();
    }

}