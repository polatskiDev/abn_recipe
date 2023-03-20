package nl.abnamro.assessment.recipe.service;

import nl.abnamro.assessment.recipe.domain.Recipe;
import nl.abnamro.assessment.recipe.model.RecipeDto;

import java.util.List;

/**
 * @author Orhan Polat
 */
public interface IRecipeService {

    List<RecipeDto> findAll();

    Recipe saveRecipe(RecipeDto recipeDto);

}
