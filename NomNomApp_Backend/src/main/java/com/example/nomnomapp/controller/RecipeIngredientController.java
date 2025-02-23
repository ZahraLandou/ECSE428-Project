package com.example.nomnomapp.controller;

import com.example.nomnomapp.dto.RecipeIngredientRequestDto;
import com.example.nomnomapp.dto.RecipeIngredientResponseDto;
import com.example.nomnomapp.model.RecipeIngredients;
import com.example.nomnomapp.service.RecipeIngredientsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/recipeIngredients")
public class RecipeIngredientsController {

    @Autowired
    private RecipeIngredientsService recipeIngredientsService;

    /**
     * Create a new RecipeIngredient.
     * Example: POST /api/recipeIngredients
     */
    @PostMapping
    public ResponseEntity<RecipeIngredientResponseDto> createRecipeIngredient(
            @RequestBody RecipeIngredientRequestDto requestDto) {
        // Convert DTO to entity and create record
        RecipeIngredients recipeIngredient = recipeIngredientsService.createRecipeIngredientFromDto(requestDto);
        // Convert entity to response DTO
        RecipeIngredientResponseDto responseDto = convertToResponseDto(recipeIngredient);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    /**
     * Retrieve a RecipeIngredient by its ID.
     * Example: GET /api/recipeIngredients/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<RecipeIngredientResponseDto> getRecipeIngredientById(@PathVariable int id) {
        RecipeIngredients recipeIngredient = recipeIngredientsService.getRecipeIngredientById(id);
        RecipeIngredientResponseDto responseDto = convertToResponseDto(recipeIngredient);
        return ResponseEntity.ok(responseDto);
    }

    /**
     * Retrieve all RecipeIngredients.
     * Example: GET /api/recipeIngredients
     */
    @GetMapping
    public ResponseEntity<List<RecipeIngredientResponseDto>> getAllRecipeIngredients() {
        List<RecipeIngredients> ingredients = (List<RecipeIngredients>) recipeIngredientsService.getAllRecipeIngredients();
        List<RecipeIngredientResponseDto> responseDtos = ingredients.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDtos);
    }

    /**
     * Retrieve all RecipeIngredients for a specific Recipe.
     * Example: GET /api/recipeIngredients/recipe/{recipeId}
     */
    @GetMapping("/recipe/{recipeId}")
    public ResponseEntity<List<RecipeIngredientResponseDto>> getRecipeIngredientsByRecipeId(@PathVariable int recipeId) {
        List<RecipeIngredients> ingredients = recipeIngredientsService.getRecipeIngredientsByRecipeId(recipeId);
        List<RecipeIngredientResponseDto> responseDtos = ingredients.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDtos);
    }

    /**
     * Update an existing RecipeIngredient.
     * Example: PUT /api/recipeIngredients/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<RecipeIngredientResponseDto> updateRecipeIngredient(@PathVariable int id,
                                                                              @RequestBody RecipeIngredientRequestDto requestDto) {
        RecipeIngredients updatedEntity = recipeIngredientsService.updateRecipeIngredientFromDto(id, requestDto);
        RecipeIngredientResponseDto responseDto = convertToResponseDto(updatedEntity);
        return ResponseEntity.ok(responseDto);
    }

    /**
     * Delete a RecipeIngredient by its ID.
     * Example: DELETE /api/recipeIngredients/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecipeIngredient(@PathVariable int id) {
        recipeIngredientsService.deleteRecipeIngredient(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Helper method to convert a RecipeIngredients entity into a RecipeIngredientResponseDto.
     */
    private RecipeIngredientResponseDto convertToResponseDto(RecipeIngredients entity) {
        RecipeIngredientResponseDto dto = new RecipeIngredientResponseDto();
        dto.setRecipeIngredientId(entity.getRecipeIngredientID());
        dto.setQuantity(entity.getQuantity());
        dto.setUnit(entity.getUnit());
        dto.setRecipeId(entity.getRecipe().getRecipeID());
        dto.setIngredientName(entity.getIngredient().getName());
        return dto;
    }
}
