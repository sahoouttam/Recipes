package com.example.Recipes.Presentation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.example.Recipes.Business.Recipe;
import com.example.Recipes.Business.RecipeService;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.core.context.SecurityContextHolder;

@RestController
@RequestMapping("/api")
@Validated
public class RecipeController {
    
    private RecipeService recipeService;

    @Autowired
    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping("/recipe/{id}")
    public Recipe getRecipe(HttpServletResponse response, @PathVariable int id) {
        response.addHeader("Content-type", "application/json");
        Optional<Recipe> recipe = recipeService.findRecipeById(id);
        if (recipe.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe not found");
        return recipe.get();
    }

    @PostMapping("/recipe/new")
    public String addRecipe(HttpServletResponse response, @Valid @RequestBody Recipe recipe) {
        response.addHeader("Content-type", "application/json");
        Recipe newRecipe = new Recipe(recipe.getName(), recipe.getCategory(), 
                            LocalDateTime.now(), recipe.getDescription(), recipe.getIngredients(), recipe.getDirections());
        
        newRecipe.setEmail(getLoggedInUser());
        recipeService.saveRecipe(newRecipe);
        return String.valueOf("id :" + newRecipe.getId());
    }

    @DeleteMapping("/recipe/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRecipe(@PathVariable int id) {
        Optional<Recipe> recipe = recipeService.findRecipeById(id);
        if (recipe.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe not found");
        }
        if (!recipe.get().getEmail().equals(getLoggedInUser())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only delete your own recipe");
        }
        recipeService.deleteRecipe(id);
    }

    @PutMapping("/recipe/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateRecipe(@PathVariable int id, @Valid @RequestBody Recipe newRecipe) {
        Optional<Recipe> recipe = recipeService.findRecipeById(id);
        if (!recipe.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe not found");
        }
        if (!recipeService.findRecipeById(id).get().getEmail().equals(getLoggedInUser())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only update your own recipe");
        }

        recipeService.updateRecipe(id, newRecipe);
    }

    @GetMapping(value = "/recipe/search", params = "category")
    public List<Recipe> getRecipeByCategory(@RequestParam("category") String category) {
        return recipeService.findByCategory(category);
    }

    @GetMapping(value = "/recipe/search", params = "name")
    public List<Recipe> getRecipeByName(@RequestParam("name") String name) {
        return recipeService.findByName(name);
    }
    
    public String getLoggedInUser() {
        org.springframework.security.core.Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getPrincipal().toString();
    }
}
