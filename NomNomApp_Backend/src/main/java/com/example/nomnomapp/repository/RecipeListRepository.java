package com.example.nomnomapp.repository;

import com.example.nomnomapp.model.NomNomUser;
import com.example.nomnomapp.model.RecipeList;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RecipeListRepository extends CrudRepository<RecipeList,Integer> {
    List<RecipeList> findByNomNomUser(NomNomUser nomNomUser);
    List<RecipeList> findByCategory(RecipeList.ListCategory category);

}
