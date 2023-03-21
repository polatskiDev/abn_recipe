package nl.abnamro.assessment.recipe.service;

import lombok.AllArgsConstructor;
import nl.abnamro.assessment.recipe.domain.Ingredients;
import nl.abnamro.assessment.recipe.domain.Instructions;
import nl.abnamro.assessment.recipe.domain.Recipe;
import nl.abnamro.assessment.recipe.message.MessageComponent;
import nl.abnamro.assessment.recipe.message.RestResponse;
import nl.abnamro.assessment.recipe.model.IngredientsDto;
import nl.abnamro.assessment.recipe.model.InstructionsDto;
import nl.abnamro.assessment.recipe.model.RecipeDto;
import nl.abnamro.assessment.recipe.repository.RecipeRepository;
import org.apache.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * @author Orhan Polat
 */
@AllArgsConstructor
@Service
public class RecipeServiceImpl implements IRecipeService{

    private static final Logger LOG = Logger.getLogger(RecipeServiceImpl.class);

    private final RecipeRepository recipeRepository;

    private final ModelMapper modelMapper;
    private final MessageComponent messageComponent;
    @Override
    public RestResponse findAll() {

        LOG.info("findAll");

        List<Recipe> recipeList = recipeRepository.findAll();

        List<RecipeDto> dtoList = recipeList.stream()
                .map(this::convertRecipeToDto)
                .collect(Collectors.toList());

        return RestResponse.of(dtoList, HttpStatus.OK,
                messageComponent.getInfoMessage("success.findAll"));
    }

    @Override
    public RestResponse saveRecipe(RecipeDto recipeDto) {
        LOG.info("saveRecipe!!");

        try{
            Recipe recipe = convertRecipeToEntity(recipeDto);
            recipeRepository.save(recipe);

            List<RecipeDto> dtoList = new ArrayList<>();
            dtoList.add(convertRecipeToDto(recipe));

            return RestResponse.of(dtoList, HttpStatus.CREATED,
                    messageComponent.getInfoMessage("success.Save"));

        } catch (Exception e) {
            LOG.error("Error occurred during saving recipe!!");

            return RestResponse.of(null, HttpStatus.BAD_REQUEST,
                    messageComponent.getErrorMessage("error.Save"));
        }
    }

    @Override
    public RestResponse updateRecipe(Long recipeId, RecipeDto recipeDto) {
        LOG.info("updateRecipe!!");

        try{
            Recipe recipe = recipeRepository.findById(recipeId).orElseThrow(NoResultException::new);

            recipe.setName(recipeDto.getName());
            recipe.setServingNumber(recipeDto.getServingNumber());
            recipe.setIsVegetarian(recipeDto.getIsVegetarian());
            recipe.getInstructions().clear();
            recipe.getIngredients().clear();
            recipe.getIngredients().addAll(convertIngredients(recipeDto.getIngredients()));
            recipe.getInstructions().addAll(convertInstructions(recipeDto.getInstructions()));

            List<RecipeDto> dtoList = new ArrayList<>();
            dtoList.add(convertRecipeToDto(recipeRepository.save(recipe)));

            return RestResponse.of(dtoList, HttpStatus.NO_CONTENT,
                    messageComponent.getInfoMessage("success.Update"));

        }catch (Exception e) {
            LOG.error("Error occurred during updating recipe!!");

            return RestResponse.of(null, HttpStatus.BAD_REQUEST,
                    messageComponent.getErrorMessage("error.Update"));
        }
    }

    @Override
    public RestResponse deleteRecipe(Long recipeId) {
        LOG.info("deleteRecipe!!!");

        try {
            Recipe recipe = recipeRepository.findById(recipeId).orElseThrow(NoSuchElementException::new);
            recipeRepository.delete(recipe);

            return RestResponse.of("", HttpStatus.NO_CONTENT,
                    messageComponent.getInfoMessage("success.Delete"));

        } catch (NoSuchElementException e) {
            LOG.warn("There is no data with this ID: " + recipeId);

            return RestResponse.of(null, HttpStatus.NOT_FOUND,
                    messageComponent.getErrorMessage("error.DeleteNoData"));

        } catch (Exception e) {
            LOG.error("Error occured during deleting recipe with ID: " + recipeId);

            return RestResponse.of(null, HttpStatus.BAD_REQUEST,
                    messageComponent.getErrorMessage("error.Delete"));
        }
    }

    private Set<Ingredients> convertIngredients(Set<IngredientsDto> ingredients) {

        return ingredients.stream().map(ingredientsDto -> Ingredients.builder()
                .id(ingredientsDto.getId())
                .ingredientName(ingredientsDto.getIngredientName())
                .recipeId(ingredientsDto.getRecipeId())
                .build()
        ).collect(Collectors.toSet());
    }

    private Set<Instructions> convertInstructions(Set<InstructionsDto> instructions) {

        return instructions.stream().map(instructionsDto -> Instructions.builder()
                .id(instructionsDto.getId())
                .description(instructionsDto.getDescription())
                .recipeId(instructionsDto.getRecipeId())
                .build()).collect(Collectors.toSet());
    }

    private Recipe convertRecipeToEntity(RecipeDto recipeDto) {
        return modelMapper.map(recipeDto, Recipe.class);
    }
    private RecipeDto convertRecipeToDto(Recipe recipe) {
        return modelMapper.map(recipe, RecipeDto.class);
    }
}
