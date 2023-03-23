package nl.abnamro.assessment.recipe.service;

import lombok.RequiredArgsConstructor;
import nl.abnamro.assessment.recipe.component.ObjectMapperUtils;
import nl.abnamro.assessment.recipe.domain.Ingredients;
import nl.abnamro.assessment.recipe.domain.Instructions;
import nl.abnamro.assessment.recipe.domain.Recipe;
import nl.abnamro.assessment.recipe.message.MessageComponent;
import nl.abnamro.assessment.recipe.message.RestResponse;
import nl.abnamro.assessment.recipe.model.RecipeDto;
import nl.abnamro.assessment.recipe.repository.RecipeRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.security.InvalidKeyException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;


/**
 * @author Orhan Polat
 */
@Service
@RequiredArgsConstructor
public class RecipeServiceImpl implements IRecipeService{

    private static final Logger LOG = LogManager.getLogger(RecipeServiceImpl.class);

    private final RecipeRepository recipeRepository;
    private final MessageComponent messageComponent;

    ObjectMapperUtils objectMapperUtils = new ObjectMapperUtils();

    @Override
    public RestResponse<Set<RecipeDto>> findAll() {
        LOG.info("findAll");

        Set<RecipeDto> dtoList = objectMapperUtils.mapAll(recipeRepository.findAll(), RecipeDto.class);

        return RestResponse.of(dtoList, HttpStatus.OK,
                messageComponent.getInfoMessage("success.getRecipe"));
    }

    @Override
    public RestResponse<RecipeDto> saveRecipe(RecipeDto recipeDto) {
        LOG.info("saveRecipe");

        try {
            Recipe recipe = objectMapperUtils.map(recipeDto, Recipe.class);
            recipeRepository.save(recipe);

            RecipeDto savedDto = objectMapperUtils.map(recipe, RecipeDto.class);

            return RestResponse.of(savedDto, HttpStatus.CREATED,
                    messageComponent.getInfoMessage("success.Save"));

        } catch (Exception e) {
            LOG.error("Error occurred during saving recipe", e);

            return RestResponse.of(null, HttpStatus.BAD_REQUEST,
                    messageComponent.getErrorMessage("error.Save"));
        }
    }

    @Override
    public RestResponse<RecipeDto> updateRecipe(Long recipeId, RecipeDto recipeDto) {
        LOG.info("updateRecipe!!");

        try {
            if (recipeDto.getId() != null && !recipeDto.getId().equals(recipeId)) {
                throw new InvalidKeyException();
            }
            Recipe recipe = recipeRepository.findById(recipeId).orElseThrow(NoSuchElementException::new);

            recipe.setName(recipeDto.getName());
            recipe.setServingNumber(recipeDto.getServingNumber());
            recipe.setIsVegetarian(recipeDto.getIsVegetarian());
            recipe.getInstructions().clear();
            recipe.getIngredients().clear();
            recipe.getIngredients().addAll(objectMapperUtils.mapAll(recipeDto.getIngredients(), Ingredients.class));
            recipe.getInstructions().addAll(objectMapperUtils.mapAll(recipeDto.getInstructions(),Instructions.class));

            RecipeDto savedRecipeDto = objectMapperUtils.map(recipeRepository.save(recipe), RecipeDto.class);

            return RestResponse.of(savedRecipeDto, HttpStatus.NO_CONTENT,
                    messageComponent.getInfoMessage("success.Update"));

        } catch (InvalidKeyException e){

            LOG.error("There is no data with this ID: " + recipeId, e);

            return RestResponse.of(null, HttpStatus.NOT_ACCEPTABLE,
                    messageComponent.getErrorMessage("error.typeMismatch"));

        } catch(NoSuchElementException e) {

            LOG.error("There is no data with this ID: " + recipeId, e);

            return RestResponse.of(null, HttpStatus.NOT_FOUND,
                    messageComponent.getErrorMessage("error.NoDataFound"));

        } catch (Exception e) {
            LOG.error("Error occurred during updating recipe!!", e);

            return RestResponse.of(null, HttpStatus.BAD_REQUEST,
                    messageComponent.getErrorMessage("error.Update"));
        }
    }

    @Override
    public RestResponse<String> deleteRecipe(Long recipeId) {
        LOG.info("deleteRecipe!!!");

        try {
            Recipe recipe = recipeRepository.findById(recipeId).orElseThrow(NoSuchElementException::new);
            recipeRepository.delete(recipe);

            return RestResponse.of("", HttpStatus.NO_CONTENT,
                    messageComponent.getInfoMessage("success.Delete"));

        } catch (NoSuchElementException e) {
            LOG.error("There is no data with this ID: " + recipeId, e);

            return RestResponse.of(null, HttpStatus.NOT_FOUND,
                    messageComponent.getErrorMessage("error.NoDataFound"));

        } catch (Exception e) {
            LOG.error("Error occurred during deleting recipe with ID: " + recipeId, e);

            return RestResponse.of(null, HttpStatus.BAD_REQUEST,
                    messageComponent.getErrorMessage("error.Delete"));
        }
    }

    @Override
    public RestResponse<RecipeDto> findRecipe(Long recipeId) {

        LOG.info("findRecipe!!!");

        try {
            Recipe recipe = recipeRepository.findById(recipeId).orElseThrow(NoSuchElementException::new);

            return RestResponse.of(objectMapperUtils.map(recipe, RecipeDto.class), HttpStatus.OK,
                    messageComponent.getInfoMessage("success.getRecipe"));

        } catch (NoSuchElementException e) {
            LOG.error("There is no data with this ID: " + recipeId, e);

            return RestResponse.of(null, HttpStatus.NOT_FOUND,
                    messageComponent.getErrorMessage("error.NoDataFound"));

        } catch (Exception e) {
            LOG.error("Error occurred during findRecipe with ID: " + recipeId, e);

            return RestResponse.of(null, HttpStatus.BAD_REQUEST,
                    messageComponent.getErrorMessage("error.findRecipe"));
        }
    }

    @Override
    public RestResponse<Set<RecipeDto>> searchRecipes(List<SearchCriteria> criteriaList){

        LOG.info("searchRecipes!!");

        RecipeSpecification spec = new RecipeSpecification(criteriaList);
        Set<RecipeDto> dtoList = objectMapperUtils.mapAll(recipeRepository.findAll(spec), RecipeDto.class);
        return RestResponse.of(dtoList, HttpStatus.OK,
                messageComponent.getInfoMessage("success.getRecipe"));
    }
}

