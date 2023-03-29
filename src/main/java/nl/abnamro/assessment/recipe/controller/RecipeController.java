package nl.abnamro.assessment.recipe.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import nl.abnamro.assessment.recipe.domain.Recipe;
import nl.abnamro.assessment.recipe.enums.SearchOperation;
import nl.abnamro.assessment.recipe.model.RecipeDto;
import nl.abnamro.assessment.recipe.service.IRecipeService;
import nl.abnamro.assessment.recipe.service.SearchCriteria;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Orhan Polat
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/recipe")
public class RecipeController {

    private static final Logger LOG = LogManager.getLogger(RecipeController.class);

    private final IRecipeService recipeService;

    @Operation(summary = "Get all existing recipes")
    @ApiResponse(responseCode = "200", description = "Found the recipes",
            content = { @Content(mediaType = "application/json",
            schema = @Schema(implementation = Recipe.class))})
    @GetMapping
    public ResponseEntity<Set<RecipeDto>> findAll() {
        LOG.info("Get All Recipe Information..");
        return new ResponseEntity<>(recipeService.findAll(), HttpStatus.OK);
    }

    @Operation(summary = "Save recipe with given Request Body")
    @ApiResponses( value = {@ApiResponse(responseCode = "201", description = "Recipe created", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Recipe.class))}),
            @ApiResponse(responseCode = "400" , description = "Recipe could not saved", content= @Content)
    })
    @PostMapping
    public ResponseEntity saveRecipe(@Parameter(description = "Request of recipe to be saved") @RequestBody @Validated RecipeDto recipeDto) {
        LOG.info("Save New Recipe");
        return new ResponseEntity<>(recipeService.saveRecipe(recipeDto), HttpStatus.CREATED);
    }

    @Operation(summary = "Update recipe by its id")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "204", description = "Recipe updated", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Recipe.class))}),
            @ApiResponse(responseCode = "406", description = "Recipe IDs mismatch!", content = @Content),
            @ApiResponse(responseCode = "404", description = "No Recipe data found with given ID", content = @Content),
            @ApiResponse(responseCode = "400" , description = "Recipe could not updated", content= @Content)
    })
    @PutMapping("/{recipeId}")
    public ResponseEntity updateRecipe(@Parameter(description = "id of recipe to be updated") @PathVariable("recipeId") Long recipeId,
                                                     @Parameter(description = "Recipe Object to be updated") @RequestBody @Validated RecipeDto recipeDto) {
        LOG.info("Update Recipe with ID: " + recipeId);
        return new ResponseEntity<>(recipeService.updateRecipe(recipeId, recipeDto), HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Delete recipe by its id")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "204", description = "Recipe deleted", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Recipe.class))}),
            @ApiResponse(responseCode = "404", description = "No Recipe data found with given ID", content = @Content),
            @ApiResponse(responseCode = "400" , description = "Recipe could not deleted", content= @Content)
    })
    @DeleteMapping("/{recipeId}")
    public ResponseEntity deleteRecipe(@Parameter(description = "id of recipe to be updated") @PathVariable("recipeId") Long recipeId) {
        LOG.info("Delete Recipe with ID: " + recipeId);
        return new ResponseEntity<>(recipeService.deleteRecipe(recipeId), HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Find recipe by its id")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description = "Recipe found", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Recipe.class))}),
            @ApiResponse(responseCode = "404", description = "No Recipe data found with given ID", content = @Content),
            @ApiResponse(responseCode = "400" , description = "Recipe could not found", content= @Content)
    })
    @GetMapping("/{recipeId}")
    public ResponseEntity<RecipeDto> getRecipeById(@Parameter(description = "id of recipe to be updated") @PathVariable("recipeId") Long recipeId) {
        LOG.info("Get Recipe By Id");
        return new ResponseEntity<>(recipeService.findRecipe(recipeId), HttpStatus.OK);
    }

    @Operation(summary = "Search recipes with criteria")
    @ApiResponse(responseCode = "200", description = "Found the recipes", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Recipe.class))})
    @GetMapping("/search")
    public ResponseEntity<Set<RecipeDto>> searchRecipes(@Parameter(description = "is Recipe vegetarian") @RequestParam(value = "isVegetarian", required = false) Boolean isVegetarian,
                                                      @Parameter(description = "Number of servings of recipe") @RequestParam(value = "servingNumber", required = false) Integer servingNumber,
                                                      @Parameter(description = "ingredient to be included") @RequestParam(value = "ingredientName", required = false) String ingredientName,
                                                      @Parameter(description = "ingredient to be excluded") @RequestParam(value = "excludeIngredientName", required = false) String excludeIngredientName,
                                                      @Parameter(description = "instruction within recipe") @RequestParam(value = "instructionText", required = false) String instructionText) {

        LOG.info("Search Recipes With Criteria");
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

        return new ResponseEntity<>(recipeService.searchRecipes(criteriaList), HttpStatus.OK);
    }
}
