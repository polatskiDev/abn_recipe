package nl.abnamro.assessment.recipe.service;

import nl.abnamro.assessment.recipe.model.RecipeDto;

import java.util.List;

/**
 * @author Orhan Polat
 */
public interface IRecipeService {

    List<RecipeDto> findAll();

    RecipeDto saveRecipe(RecipeDto recipeDto);

    RecipeDto updateRecipe(Long recipeId, RecipeDto recipeDto);

    RecipeDto deleteRecipe(Long recipeId);
}
