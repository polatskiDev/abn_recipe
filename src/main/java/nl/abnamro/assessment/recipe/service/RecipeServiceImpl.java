package nl.abnamro.assessment.recipe.service;

import lombok.RequiredArgsConstructor;
import nl.abnamro.assessment.recipe.component.ObjectMapperUtils;
import nl.abnamro.assessment.recipe.controller.NotFoundException;
import nl.abnamro.assessment.recipe.domain.Ingredients;
import nl.abnamro.assessment.recipe.domain.Instructions;
import nl.abnamro.assessment.recipe.domain.Recipe;
import nl.abnamro.assessment.recipe.model.RecipeDto;
import nl.abnamro.assessment.recipe.repository.RecipeRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;


/**
 * @author Orhan Polat
 */
@Service
@RequiredArgsConstructor
public class RecipeServiceImpl implements IRecipeService{

    private static final Logger LOG = LogManager.getLogger(RecipeServiceImpl.class);

    private final RecipeRepository recipeRepository;

    ObjectMapperUtils objectMapperUtils = new ObjectMapperUtils();

    @Override
    public Set<RecipeDto> findAll() {
        LOG.info("findAll");

        return objectMapperUtils.mapAll(recipeRepository.findAll(), RecipeDto.class);
    }

    @Override
    public RecipeDto saveRecipe(RecipeDto recipeDto) {
        LOG.info("saveRecipe");

        try {
            Recipe recipe = objectMapperUtils.map(recipeDto, Recipe.class);
            recipeRepository.save(recipe);

            return objectMapperUtils.map(recipe, RecipeDto.class);

        } catch (Exception e) {
            LOG.error("Error occurred during saving recipe", e);

            return null;
        }
    }

    @Override
    public RecipeDto updateRecipe(Long recipeId, RecipeDto recipeDto) {
        LOG.info("updateRecipe!!");

        if (recipeDto.getId() != null && !recipeDto.getId().equals(recipeId)) {
            throw new NotFoundException();
        }
        Recipe recipe = recipeRepository.findById(recipeId).orElseThrow(NotFoundException::new);

        recipe.setName(recipeDto.getName());
        recipe.setServingNumber(recipeDto.getServingNumber());
        recipe.setIsVegetarian(recipeDto.getIsVegetarian());
        recipe.getInstructions().clear();
        recipe.getIngredients().clear();
        recipe.getIngredients().addAll(objectMapperUtils.mapAll(recipeDto.getIngredients(), Ingredients.class));
        recipe.getInstructions().addAll(objectMapperUtils.mapAll(recipeDto.getInstructions(),Instructions.class));

        return objectMapperUtils.map(recipeRepository.save(recipe), RecipeDto.class);
    }

    @Override
    public String deleteRecipe(Long recipeId) {
        LOG.info("deleteRecipe!!!");

        Recipe recipe = recipeRepository.findById(recipeId).orElseThrow(NotFoundException::new);
        recipeRepository.delete(recipe);

        return null;
    }

    @Override
    public RecipeDto findRecipe(Long recipeId) {

        LOG.info("findRecipe!!!");

        Recipe recipe = recipeRepository.findById(recipeId).orElseThrow(NotFoundException::new);

        return objectMapperUtils.map(recipe, RecipeDto.class);
    }

    @Override
    public Set<RecipeDto> searchRecipes(List<SearchCriteria> criteriaList){

        LOG.info("searchRecipes!!");

        RecipeSpecification spec = new RecipeSpecification(criteriaList);
        return objectMapperUtils.mapAll(recipeRepository.findAll(spec), RecipeDto.class);
    }
}

