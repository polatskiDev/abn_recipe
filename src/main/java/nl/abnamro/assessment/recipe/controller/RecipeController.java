package nl.abnamro.assessment.recipe.controller;

import lombok.RequiredArgsConstructor;
import nl.abnamro.assessment.recipe.model.RecipeDto;
import nl.abnamro.assessment.recipe.service.IRecipeService;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<List<RecipeDto>> findAll() {
        LOG.info("Get All Recipe Informations..");
        return ResponseEntity.ok().body(recipeService.findAll());
    }

    @PostMapping
    public ResponseEntity saveRecipe(@RequestBody @Validated RecipeDto recipeDto) {
        LOG.info("Save New Recipe");
        return new ResponseEntity<>(recipeService.saveRecipe(recipeDto), HttpStatus.CREATED);
    }
}
