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

import jakarta.transaction.Transactional;


@Service
public class RecipeListService {

    @Autowired
    private RecipeListRepository recipeListRepository;
    @Autowired
    private RecipeRepository recipeRepository;
    @Autowired
    private UserRepository nomUserRepository;


    public RecipeList createRecipeList(RecipeList aRecipeList) {
        return recipeListRepository.save(aRecipeList);
    }

    public RecipeList getRecipeListById(int recipeListId) {
        return recipeListRepository.findById(recipeListId)
                .orElseThrow(() -> new NoSuchElementException("RecipeList with id: " + recipeListId + "does not exist"));
    }   

    public boolean deleteRecipeListByIdint(int recipeListId) {
        Optional<RecipeList> recipeList = recipeListRepository.findById(recipeListId);
        if (recipeList.isPresent()) {
            recipeListRepository.delete(recipeList.get());
            return true;
        } else {
            throw new NoSuchElementException("RecipeList with id: " + recipeListId + "does not exist");
        }
    }

    @Transactional
    public void deleteAllRecipeLists() {
        recipeListRepository.deleteAll();
    }
}
