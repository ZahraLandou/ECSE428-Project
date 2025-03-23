package com.example.nomnomapp.repository;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import com.example.nomnomapp.model.Recipe;



public interface CollectionRepository extends CrudRepository<List<Recipe>,Integer> {
        List<Recipe> findByCollectionId(Integer collectionId);
        List<Recipe> findCollectionByTitle(String title);
}
