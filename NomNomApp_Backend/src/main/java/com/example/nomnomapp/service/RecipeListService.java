package com.example.nomnomapp.service;

import com.example.nomnomapp.model.Recipe;
import com.example.nomnomapp.model.RecipeList;
import com.example.nomnomapp.model.NomNomUser;
import com.example.nomnomapp.repository.RecipeListRepository;
import com.example.nomnomapp.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
    public boolean addRecipeToList(int listId, Recipe recipe) {
        Optional<RecipeList> optionalRecipeList = recipeListRepository.findById(listId);
        if (optionalRecipeList.isEmpty()) {
            System.out.println("NO RECIPELISTS FOUND for id: " + listId);
            return false;
        }

        RecipeList recipeList = optionalRecipeList.get();

        if (!recipeList.getRecipes().contains(recipe)) {
            boolean added = recipeList.addRecipe(recipe);
            recipe.addRecipeList(recipeList);

            if (added) {
                recipeRepository.save(recipe);
                recipeListRepository.save(recipeList);
            }
            return added;
        }
        return false;
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
    public boolean removeRecipeFromList(int listId, Recipe recipe) {
        Optional<RecipeList> optionalRecipeList = recipeListRepository.findById(listId);
        if (optionalRecipeList.isPresent()) {
            RecipeList recipeList = optionalRecipeList.get();
            boolean removed = recipeList.removeRecipe(recipe);
            if (removed) {
                recipeListRepository.save(recipeList);
            }
            return removed;
        }
        return false;
    }
}