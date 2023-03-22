package nl.abnamro.assessment.recipe.controller;

import lombok.RequiredArgsConstructor;
import nl.abnamro.assessment.recipe.enums.SearchOperation;
import nl.abnamro.assessment.recipe.message.RestResponse;
import nl.abnamro.assessment.recipe.model.RecipeDto;
import nl.abnamro.assessment.recipe.service.IRecipeService;

import nl.abnamro.assessment.recipe.service.SearchCriteria;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Orhan Polat
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/recipe")
public class RecipeController {

    private static final Logger LOG = LogManager.getLogger(RecipeController.class);

    private final IRecipeService recipeService;

    @GetMapping
    public ResponseEntity<RestResponse> findAll() {
        LOG.info("Get All Recipe Information..");
        return ResponseEntity.ok(recipeService.findAll());
    }

    @PostMapping
    public ResponseEntity<RestResponse> saveRecipe(@RequestBody @Validated RecipeDto recipeDto) {
        LOG.info("Save New Recipe");
        return ResponseEntity.ok(recipeService.saveRecipe(recipeDto));
    }

    @PutMapping("/{recipeId}")
    public ResponseEntity<RestResponse> updateRecipe(@PathVariable("recipeId") Long recipeId, @RequestBody @Validated RecipeDto recipeDto) {
        LOG.info("Update Recipe with ID: " + recipeId);
        return ResponseEntity.ok(recipeService.updateRecipe(recipeId, recipeDto));
    }

    @DeleteMapping("/{recipeId}")
    public ResponseEntity<RestResponse> deleteRecipe(@PathVariable("recipeId") Long recipeId) {
        LOG.info("Delete Recipe with ID: " + recipeId);
        return ResponseEntity.ok(recipeService.deleteRecipe(recipeId));
    }

    @GetMapping("/{recipeId}")
    public ResponseEntity<RestResponse> getRecipeById(@PathVariable("recipeId") Long recipeId) {
        LOG.info("Get Recipe By Id");
        return ResponseEntity.ok(recipeService.findRecipe(recipeId));
    }

    @GetMapping("/search")
    public ResponseEntity<RestResponse> searchRecipes(@RequestParam(value = "isVegetarian", required = false) Boolean isVegetarian,
                                                     @RequestParam(value = "servingNumber", required = false) Integer servingNumber,
                                                     @RequestParam(value = "ingredientName", required = false) String ingredientName,
                                                     @RequestParam(value = "excludeIngredientName", required = false) String excludeIngredientName,
                                                     @RequestParam(value = "instructionText", required = false) String instructionText) {

        List<SearchCriteria> criteriaList = new ArrayList<>();

        if (isVegetarian != null) {
            criteriaList.add(new SearchCriteria("isVegetarian", SearchOperation.EQUALITY, isVegetarian));
        }

        if (servingNumber != null) {
            criteriaList.add(new SearchCriteria("servingNumber", SearchOperation.GREATER_THAN_OR_EQUAL, servingNumber));
        }

        if (ingredientName != null) {
            criteriaList.add(new SearchCriteria("ingredientName", SearchOperation.CONTAINS, ingredientName));
        }

        if (excludeIngredientName != null) {
            criteriaList.add(new SearchCriteria("ingredientName", SearchOperation.NOT_CONTAINS, excludeIngredientName));
        }

        if (instructionText != null) {
            criteriaList.add(new SearchCriteria("description", SearchOperation.CONTAINS, instructionText));
        }

        return ResponseEntity.ok(recipeService.searchRecipes(criteriaList));

    }
}
