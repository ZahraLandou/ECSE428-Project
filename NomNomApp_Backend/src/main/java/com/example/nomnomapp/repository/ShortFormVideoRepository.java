package com.example.nomnomapp.repository;



import org.springframework.data.repository.CrudRepository;

import com.example.nomnomapp.model.ShortFormVideo;
import com.example.nomnomapp.model.Recipe;


public interface ShortFormVideoRepository extends CrudRepository<ShortFormVideo, Integer> {
    
    ShortFormVideo findByVideoId(Integer videoId);
    ShortFormVideo findByRecipe(Recipe recipe);

} 