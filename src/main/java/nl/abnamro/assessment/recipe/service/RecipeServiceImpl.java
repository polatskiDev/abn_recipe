package nl.abnamro.assessment.recipe.service;

import lombok.RequiredArgsConstructor;
import nl.abnamro.assessment.recipe.domain.Ingredients;
import nl.abnamro.assessment.recipe.domain.Instructions;
import nl.abnamro.assessment.recipe.domain.Recipe;
import nl.abnamro.assessment.recipe.message.MessageComponent;
import nl.abnamro.assessment.recipe.message.RestResponse;
import nl.abnamro.assessment.recipe.model.IngredientsDto;
import nl.abnamro.assessment.recipe.model.InstructionsDto;
import nl.abnamro.assessment.recipe.model.RecipeDto;
import nl.abnamro.assessment.recipe.repository.RecipeRepository;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.security.InvalidKeyException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * @author Orhan Polat
 */
@Service
@RequiredArgsConstructor
public class RecipeServiceImpl implements IRecipeService{

    private static final Logger LOG = LogManager.getLogger(RecipeServiceImpl.class);

    private final RecipeRepository recipeRepository;
    private final ModelMapper modelMapper;
    private final MessageComponent messageComponent;

    @Override
    public RestResponse<List<RecipeDto>> findAll() {
        LOG.info("findAll");

        List<RecipeDto> dtoList = recipeRepository.findAll().stream()
                .map(this::convertRecipeToDto)
                .collect(Collectors.toList());

        return RestResponse.of(dtoList, HttpStatus.OK,
                messageComponent.getInfoMessage("success.getRecipe"));
    }

    @Override
    public RestResponse<List<RecipeDto>> saveRecipe(RecipeDto recipeDto) {
        LOG.info("saveRecipe");

        try {
            Recipe recipe = convertRecipeToEntity(recipeDto);
            recipeRepository.save(recipe);

            List<RecipeDto> dtoList = List.of(convertRecipeToDto(recipe));

            return RestResponse.of(dtoList, HttpStatus.CREATED,
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
            recipe.getIngredients().addAll(convertIngredients(recipeDto.getIngredients()));
            recipe.getInstructions().addAll(convertInstructions(recipeDto.getInstructions()));

            RecipeDto savedRecipeDto = convertRecipeToDto(recipeRepository.save(recipe));

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

            return RestResponse.of(convertRecipeToDto(recipe), HttpStatus.OK,
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
    public RestResponse<List<RecipeDto>> searchRecipes(List<SearchCriteria> criteriaList){

        LOG.info("searchRecipes!!");

        RecipeSpecification spec = new RecipeSpecification(criteriaList);
        List<RecipeDto> dtoList = recipeRepository.findAll(spec).stream().map(this::convertRecipeToDto)
                .collect(Collectors.toList());
        return RestResponse.of(dtoList, HttpStatus.OK,
                messageComponent.getInfoMessage("success.getRecipe"));
    }

    private Set<Ingredients> convertIngredients(Set<IngredientsDto> ingredients) {

        return ingredients.stream().map(
                ingredientsDto -> modelMapper.map(ingredientsDto, Ingredients.class))
                .collect(Collectors.toSet());
    }

    private Set<Instructions> convertInstructions(Set<InstructionsDto> instructions) {

        return instructions.stream().map(
                instructionsDto -> modelMapper.map(instructionsDto, Instructions.class))
                .collect(Collectors.toSet());
    }

    private Recipe convertRecipeToEntity(RecipeDto recipeDto) {
        return modelMapper.map(recipeDto, Recipe.class);
    }
    private RecipeDto convertRecipeToDto(Recipe recipe) {
        return modelMapper.map(recipe, RecipeDto.class);
    }
}

