package com.example.nomnomapp.stepdefinitions;

import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

import com.example.nomnomapp.model.NomNomUser;
import com.example.nomnomapp.model.Recipe;
import com.example.nomnomapp.model.RecipeList;

import com.example.nomnomapp.repository.UserRepository;
import com.example.nomnomapp.repository.RecipeRepository;
import com.example.nomnomapp.repository.RecipeListRepository;

import com.example.nomnomapp.service.RecipeListService;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.Before;
import io.cucumber.java.en.*;

import org.springframework.beans.factory.annotation.Autowired;

public class RecipeListStepDefinitions {
    
    @Autowired 
    private RecipeListService recipeListService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RecipeRepository recipeRepository;
    @Autowired
    private RecipeListRepository recipeListRepository;


    @Before
    public void setup(){
        recipeListService.deleteAllRecipeLists();
    }

    

}
