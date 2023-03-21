package nl.abnamro.assessment.recipe.controller;

import lombok.RequiredArgsConstructor;
import nl.abnamro.assessment.recipe.message.RestResponse;
import nl.abnamro.assessment.recipe.model.RecipeDto;
import nl.abnamro.assessment.recipe.service.IRecipeService;
import org.apache.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author Orhan Polat
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/recipe")
public class RecipeController {

    private static final Logger LOG = Logger.getLogger(RecipeController.class);

    private final IRecipeService recipeService;

    @GetMapping
    public ResponseEntity<RestResponse> findAll() {
        LOG.info("Get All Recipe Informations..");
        return ResponseEntity.ok().body(recipeService.findAll());
    }

    @PostMapping
    public ResponseEntity<RestResponse> saveRecipe(@RequestBody @Validated RecipeDto recipeDto) {
        LOG.info("Save New Recipe");
        return ResponseEntity.ok().body(recipeService.saveRecipe(recipeDto));
    }

    @PutMapping("/{recipeId}")
    public ResponseEntity<RestResponse> updateRecipe(@PathVariable("recipeId") Long recipeId, @RequestBody @Validated RecipeDto recipeDto) {
        LOG.info("Update Recipe with ID: " + recipeId);
        return ResponseEntity.ok().body(recipeService.updateRecipe(recipeId, recipeDto));
    }

    @DeleteMapping("/{recipeId}")
    public ResponseEntity<RestResponse> deleteRecipe(@PathVariable("recipeId") Long recipeId) {
        LOG.info("Delete Recipe with ID: " + recipeId);
        return ResponseEntity.ok().body(recipeService.deleteRecipe(recipeId));
    }
}
