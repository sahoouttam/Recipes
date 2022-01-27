package com.example.Recipes.Business;

import java.util.List;
import java.util.Optional;

import com.example.Recipes.Persistence.RecipeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RecipeService {
    
    RecipeRepository recipeRepository;

    @Autowired
    public RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public Optional<Recipe> findRecipeById(Integer id) {
        return recipeRepository.findById(id);
    }

    public void saveRecipe(Recipe recipe) {
        recipeRepository.save(recipe);
    }

    public void deleteRecipe(Integer id) {
        recipeRepository.deleteById(id);
    }

    public List<Recipe> findByCategory(String category) {
        return recipeRepository.findAllByCategoryIgnoreCaseOrderByDateDesc(category);
    }

    public List<Recipe> findByName(String name) {
        return recipeRepository.findAllByNameContainingIgnoreCaseOrderByDateDesc(name);
    }

    public void updateRecipe(Integer id, Recipe newRecipe) {
        Optional<Recipe> recipe = findRecipeById(id);
        recipe.get().setName(newRecipe.getName());
        recipe.get().setCategory(newRecipe.getCategory());
        recipe.get().setTime(newRecipe.getTime());
        recipe.get().setDescription(newRecipe.getDescription());
        recipe.get().setIngredients(newRecipe.getIngredients());
        recipe.get().setDirections(newRecipe.getDescriptions());
        saveRecipe(recipe.get());

    }
}
