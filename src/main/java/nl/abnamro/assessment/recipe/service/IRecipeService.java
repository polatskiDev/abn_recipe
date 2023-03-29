package nl.abnamro.assessment.recipe.service;

import nl.abnamro.assessment.recipe.model.RecipeDto;

import java.util.List;
import java.util.Set;

/**
 * @author Orhan Polat
 */
public interface IRecipeService {

    Set<RecipeDto> findAll();

    RecipeDto saveRecipe(RecipeDto recipeDto);

    RecipeDto updateRecipe(Long recipeId, RecipeDto recipeDto);

    String deleteRecipe(Long recipeId);

    RecipeDto findRecipe(Long recipeId);

    Set<RecipeDto> searchRecipes(List<SearchCriteria> criteria);
}
