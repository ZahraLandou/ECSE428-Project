package com.example.nomnomapp.service;
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


import org.springframework.transaction.annotation.Transactional;
import com.example.nomnomapp.model.RecipeList.ListCategory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RecipeListService {

    @Autowired
    private RecipeListRepository recipeListRepository;

    @Autowired
    private RecipeRepository recipeRepository;
    @Autowired
    private UserRepository nomUserRepository;

    public RecipeList createRecipeList(RecipeList recipeList) {
        return recipeListRepository.save(recipeList);
    }

    public List<RecipeList> getAllRecipeLists() {
        return (List<RecipeList>) recipeListRepository.findAll();
    }

    public Optional<RecipeList> getRecipeListById(int id) {
        return recipeListRepository.findById(id);
    }

    public List<RecipeList> getRecipeListsByUser(NomNomUser user) {
        return recipeListRepository.findByNomNomUser(user);
    }

    public List<RecipeList> getRecipeListsByCategory(ListCategory category) {
        return recipeListRepository.findByCategory(category);
    }

    public void deleteRecipeList(int id) {
        recipeListRepository.deleteById(id);
    }

    public void deleteAllRecipeLists() {
        recipeListRepository.deleteAll();
    }


    @Transactional
    public List<RecipeList> getFavoriteRecipeListsByUser(NomNomUser user) {
        List<RecipeList> favouriteRecipeLists = new ArrayList<>();
        favouriteRecipeLists = recipeListRepository.findByNomNomUser(user).stream()
                .filter(list -> list.getCategory() == ListCategory.Favorites)
                .toList();

        if (favouriteRecipeLists.isEmpty()) {
            throw new IllegalArgumentException("No favorites lists found for the provided user");

        }
        return favouriteRecipeLists;

    }

    @Transactional
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
        
        try {recipeListRepository.save(recipeList);}
        catch(IllegalArgumentException e){
            return false;
        }

        return wasAdded;
    }

    @Transactional
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
        
        try {recipeListRepository.save(recipeList);}
        catch(IllegalArgumentException e){
            return false;
        }
        
        return wasRemoved;
    }


}
