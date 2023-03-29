package nl.abnamro.assessment.recipe.service;

import nl.abnamro.assessment.recipe.component.ObjectMapperUtils;
import nl.abnamro.assessment.recipe.domain.Ingredients;
import nl.abnamro.assessment.recipe.domain.Instructions;
import nl.abnamro.assessment.recipe.domain.Recipe;
import nl.abnamro.assessment.recipe.enums.SearchOperation;
import nl.abnamro.assessment.recipe.model.IngredientsDto;
import nl.abnamro.assessment.recipe.model.InstructionsDto;
import nl.abnamro.assessment.recipe.model.RecipeDto;
import nl.abnamro.assessment.recipe.repository.RecipeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @author Orhan Polat
 */

@ExtendWith(MockitoExtension.class)
public class RecipeServiceTest {

    @Mock
    RecipeRepository recipeRepository;

    @InjectMocks
    RecipeServiceImpl recipeService;

    @InjectMocks
    private ObjectMapperUtils objectMapperUtils;

    private Recipe recipe;
    private RecipeDto recipeDto;
    private Set<RecipeDto> recipeDtoSet;

    @BeforeEach
    void setUp() {
        recipe = new Recipe();
        recipe.setId(1L);
        recipe.setName("Test Recipe");
        recipe.setServingNumber(2);
        recipe.setIsVegetarian(false);

        Set<Ingredients> setIngredients = new HashSet<>();
        Ingredients ingredient = Ingredients.builder().id(1L).ingredientName("Test Ingredient").recipeId(1L).build();
        setIngredients.add(ingredient);

        Set<Instructions> setInstructions = new HashSet<>();
        Instructions isto = Instructions.builder().id(1L).description("Test Instruction").recipeId(1L).build();
        setInstructions.add(isto);

        recipe.setIngredients(setIngredients);
        recipe.setInstructions(setInstructions);

        recipeDto = new RecipeDto();
        recipeDto.setId(1L);
        recipeDto.setName("Test Recipe");
        recipeDto.setServingNumber(2);
        recipeDto.setIsVegetarian(false);
        Set<IngredientsDto> ingredientsSet = new HashSet<>();
        IngredientsDto dto = IngredientsDto.builder().id(1L).ingredientName("Test Ingredient").recipeId(1L).build();
        ingredientsSet.add(dto);

        Set<InstructionsDto> instructionsSet = new HashSet<>();
        InstructionsDto idto = InstructionsDto.builder().id(1L).description("Test Instruction").recipeId(1L).build();
        instructionsSet.add(idto);
        recipeDto.setIngredients( new LinkedHashSet<>());
        recipeDto.setInstructions(new LinkedHashSet<>());
        recipeDto.setIngredients(ingredientsSet);
        recipeDto.setInstructions(instructionsSet);

        recipeDtoSet = new HashSet<>(Set.of(recipeDto));
    }

    @Test
    void testFindAllRecipes() {
        List<Recipe> recipeList = List.of(recipe);

        when(recipeRepository.findAll()).thenReturn(recipeList);

        Set<RecipeDto> result = recipeService.findAll();

        assertThat(result).isEqualTo(recipeDtoSet);
    }

    @Test
    public void testSaveRecipe() {
        when(recipeRepository.save(any())).thenReturn(recipe);

        RecipeDto response = recipeService.saveRecipe(recipeDto);

        assertEquals(response, recipeDto);
    }

    @Test
    public void testSaveRecipe1() {
        // Arrange
        Recipe recipe = objectMapperUtils.map(recipeDto, Recipe.class);
        when(recipeRepository.save(any(Recipe.class))).thenReturn(recipe);

        // Act
        RecipeDto response = recipeService.saveRecipe(recipeDto);

        // Assert
        assertNotNull(response);
        verify(recipeRepository, times(1)).save(any(Recipe.class));
    }

    @Test
    public void testUpdateRecipe() {
        // Arrange
        when(recipeRepository.findById(anyLong())).thenReturn(java.util.Optional.of(recipe));

        // Act
        Recipe recipe = objectMapperUtils.map(recipeDto, Recipe.class);

        when(recipeRepository.save(any())).thenReturn(recipe);

        RecipeDto response = recipeService.updateRecipe(1L, recipeDto);

        // Assert
        verify(recipeRepository, times(1)).findById(anyLong());
    }

    @Test
    public void testDeleteRecipe() {
        // Arrange
        Recipe recipe = new Recipe();
        when(recipeRepository.findById(anyLong())).thenReturn(java.util.Optional.of(recipe));

        // Act
        String response = recipeService.deleteRecipe(1L);

        // Assert
        verify(recipeRepository, times(1)).delete(any(Recipe.class));
    }

    @Test
    public void testFindRecipe() {
        // Arrange
        when(recipeRepository.findById(anyLong())).thenReturn(java.util.Optional.of(recipe));

        // Act
        RecipeDto response = recipeService.findRecipe(1L);

        // Assert
        assertNotNull(response);
        assertEquals(response.getId(), 1L);
        verify(recipeRepository, times(1)).findById(anyLong());
    }

    @Test
    public void testSearchRecipes() {
        // Arrange
        SearchCriteria searchCriteria = new SearchCriteria("name", SearchOperation.EQUALITY, "Pasta");
        List<SearchCriteria> criteriaList = List.of(searchCriteria);

        // Act
        Set<RecipeDto> response = recipeService.searchRecipes(criteriaList);

        // Assert
        assertNotNull(response);
    }
}
