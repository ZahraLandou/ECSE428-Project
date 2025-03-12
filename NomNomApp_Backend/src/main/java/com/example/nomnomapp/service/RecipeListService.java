package com.example.nomnomapp.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.nomnomapp.model.RecipeList;
import com.example.nomnomapp.model.Recipe;
import com.example.nomnomapp.model.NomNomUser;
import com.example.nomnomapp.repository.RecipeListRepository;
import com.example.nomnomapp.repository.RecipeRepository;
import com.example.nomnomapp.repository.UserRepository;

import jakarta.transaction.Transactional;


@Service
public class RecipeListService {

    @Autowired
    private RecipeListRepository recipeListRepository;
    @Autowired
    private RecipeRepository recipeRepository;
    @Autowired
    private UserRepository nomUserRepository;


    // public RecipeList createRecipeList() {
    //     // TODO:
    //     return new RecipeList();
    // }

    public boolean addRecipeToList(int aNomNomUserId, int aRecipeListId, int aRecipeId){
        
        boolean wasAdded = false;

        RecipeList recipeList;
        NomNomUser nomUser;
        try{
            recipeList = recipeListRepository.findById(aRecipeListId).get();
            nomUser = nomUserRepository.findById(aNomNomUserId).get();
        }
        catch(NoSuchElementException e){
            return false;
        }
        Recipe recipe = recipeRepository.findByRecipeId(aRecipeId);
        
        if(recipeList.getNomNomUser().getUsername() != nomUser.getUsername()){
            return false;
        }

        if(recipeList.addRecipe(recipe)){
            wasAdded = true;
        }
        return wasAdded;
    }

    public boolean removeRecipeFromList(int aNomNomUserId, int aRecipeListId, int aRecipeId){
        boolean wasRemoved = false;

        RecipeList recipeList;
        NomNomUser nomUser;
        try{
            recipeList = recipeListRepository.findById(aRecipeListId).get();
            nomUser = nomUserRepository.findById(aNomNomUserId).get();
        }
        catch(NoSuchElementException e){
            return false;
        }
        Recipe recipe = recipeRepository.findByRecipeId(aRecipeId);
        
        if(recipeList.getNomNomUser().getUsername() != nomUser.getUsername()){
            return false;
        }

        if(recipeList.removeRecipe(recipe)){
            wasRemoved = true;
        }
        return wasRemoved;
    }

    // @Transactional
    // public void deleteAllRecipeIngredients() {
    //     recipeListRepository.deleteAll();
    // }
}
