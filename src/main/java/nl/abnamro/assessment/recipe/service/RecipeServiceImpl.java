package nl.abnamro.assessment.recipe.service;

import lombok.AllArgsConstructor;
import nl.abnamro.assessment.recipe.domain.Ingredients;
import nl.abnamro.assessment.recipe.domain.Instructions;
import nl.abnamro.assessment.recipe.domain.Recipe;
import nl.abnamro.assessment.recipe.model.IngredientsDto;
import nl.abnamro.assessment.recipe.model.InstructionsDto;
import nl.abnamro.assessment.recipe.model.RecipeDto;
import nl.abnamro.assessment.recipe.repository.RecipeRepository;
import org.apache.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
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
    @Override
    public List<RecipeDto> findAll() {

        LOG.info("findAll");
        List<Recipe> recipeList = recipeRepository.findAll();
        return recipeList.stream()
                .map(this::convertRecipeToDto)
                .collect(Collectors.toList());
    }

    @Override
    public RecipeDto saveRecipe(RecipeDto recipeDto) {
        LOG.info("saveRecipe!!");
        try{
            Recipe recipe = convertRecipeToEntity(recipeDto);
            recipeRepository.save(recipe);
            return convertRecipeToDto(recipe);
        } catch (Exception e) {
            LOG.error("Error occured during saving recipe!!");
        }
        return null;
    }

    @Override
    public RecipeDto updateRecipe(Long recipeId, RecipeDto recipeDto) {
        LOG.info("updateRecipe!!");
        try{
            Recipe recipe = recipeRepository.findById(recipeId).orElseThrow();

            recipe.setName(recipeDto.getName());
            recipe.setServingNumber(recipeDto.getServingNumber());
            recipe.setIsVegetarian(recipeDto.getIsVegetarian());
            recipe.getInstructions().clear();
            recipe.getIngredients().clear();
            recipe.getIngredients().addAll(convertIngredients(recipeDto.getIngredients()));
            recipe.getInstructions().addAll(convertInstructions(recipeDto.getInstructions()));
            return convertRecipeToDto(recipeRepository.save(recipe));
        }catch (Exception e) {
            LOG.error("Error occured during updating recipe!!");
        }
        return null;
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
