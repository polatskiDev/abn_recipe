package nl.abnamro.assessment.recipe.service;

import nl.abnamro.assessment.recipe.message.RestResponse;
import nl.abnamro.assessment.recipe.model.RecipeDto;

import java.util.List;

/**
 * @author Orhan Polat
 */
public interface IRecipeService {

    RestResponse findAll();

    RestResponse saveRecipe(RecipeDto recipeDto);

    RestResponse updateRecipe(Long recipeId, RecipeDto recipeDto);

    RestResponse deleteRecipe(Long recipeId);

    RestResponse findRecipe(Long recipeId);

    RestResponse searchRecipes(List<SearchCriteria> criteria);
}
