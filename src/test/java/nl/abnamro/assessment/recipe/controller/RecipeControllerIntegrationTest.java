package nl.abnamro.assessment.recipe.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.abnamro.assessment.recipe.model.IngredientsDto;
import nl.abnamro.assessment.recipe.model.InstructionsDto;
import nl.abnamro.assessment.recipe.model.RecipeDto;
import nl.abnamro.assessment.recipe.service.IRecipeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Orhan Polat
 */
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class RecipeControllerIntegrationTest {

    @Autowired
    private IRecipeService recipeService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testFindAll() throws Exception {

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/recipe")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        RecipeDto[] recipeDto = objectMapper.readValue(responseBody, RecipeDto[].class);
        assertThat(recipeDto).isNotNull();
        assertThat(recipeDto[0].getId()).isNotNull();
    }

    @Test
    public void testSaveRecipe() throws Exception {

        IngredientsDto ingredientsDto = IngredientsDto.builder()
                .ingredientName("Ingredient1").build();
        InstructionsDto instructionsDto1 = InstructionsDto.builder()
                .description("Instruction1").build();
        RecipeDto recipeDto1 = RecipeDto.builder()
                .name("Recipe 1")
                .servingNumber(2)
                .isVegetarian(true)
                .ingredients(Set.of(ingredientsDto))
                .instructions(Set.of(instructionsDto1)).build();

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/recipe")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(recipeDto1)))
                .andExpect(status().isCreated())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        RecipeDto savedRecipe = objectMapper.readValue(responseBody, RecipeDto.class);
        assertThat(savedRecipe).isNotNull();
        assertThat(savedRecipe.getId()).isNotNull();
        assertThat(savedRecipe.getName()).isEqualTo(recipeDto1.getName());
    }

    @Test
    public void testUpdateRecipe() throws Exception {

        IngredientsDto ingredientsDto1 = IngredientsDto.builder()
                .ingredientName("Ingredient1").build();
        InstructionsDto instructionsDto1 = InstructionsDto.builder()
                .description("Instruction1").build();
        RecipeDto recipeDto1 = RecipeDto.builder()
                .name("Recipe 1")
                .servingNumber(2)
                .isVegetarian(true)
                .ingredients(Set.of(ingredientsDto1))
                .instructions(Set.of(instructionsDto1)).build();
        RecipeDto saveRecipe = recipeService.saveRecipe(recipeDto1);

        Long saveRecipeId = saveRecipe.getId();

        IngredientsDto ingredientsDto = IngredientsDto.builder()
                .ingredientName("Ingredient1 Updated").build();
        InstructionsDto instructionsDto = InstructionsDto.builder()
                .description("Instruction1 Updated").build();
        RecipeDto recipeDto = RecipeDto.builder()
                .id(saveRecipeId)
                .name("Recipe 1 Updated")
                .servingNumber(2)
                .isVegetarian(true)
                .ingredients(Set.of(ingredientsDto))
                .instructions(Set.of(instructionsDto)).build();

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/recipe/{recipeId}", saveRecipe.getId())
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(recipeDto)))
                .andExpect(status().isNoContent())
                .andReturn();

        // Then
        RecipeDto actualRecipe = recipeService.findRecipe(saveRecipe.getId());
        assertThat(actualRecipe).isNotNull();
        assertThat(actualRecipe.getName()).isEqualTo(recipeDto.getName());
    }

    @Test
    public void testDeleteRecipe() throws Exception {
        IngredientsDto ingredientsDto = IngredientsDto.builder()
                .ingredientName("IngredientDelete").build();
        InstructionsDto instructionsDto1 = InstructionsDto.builder()
                .description("InstructionDelete").build();
        RecipeDto recipeDto1 = RecipeDto.builder()
                .name("Recipe Delete")
                .servingNumber(2)
                .isVegetarian(true)
                .ingredients(Set.of(ingredientsDto))
                .instructions(Set.of(instructionsDto1)).build();
        RecipeDto savedRecipeDto = recipeService.saveRecipe(recipeDto1);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/recipe/{recipeId}", savedRecipeDto.getId())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn();

        // Then
        Exception exception = assertThrows(NotFoundException.class, () -> {
            recipeService.findRecipe(savedRecipeDto.getId());
        });

        String expectedMessage = "NotFoundException";
        String actualMessage = exception.getClass().getName();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testGetRecipeById() throws Exception {

        IngredientsDto ingredientsDto2 = IngredientsDto.builder()
                .ingredientName("Ingredient2").build();
        InstructionsDto instructionsDto2 = InstructionsDto.builder()
                .description("Instruction2").build();
        RecipeDto recipeDto2 = RecipeDto.builder()
                .name("Recipe 2")
                .servingNumber(4)
                .isVegetarian(false)
                .ingredients(Set.of(ingredientsDto2))
                .instructions(Set.of(instructionsDto2)).build();

        RecipeDto savedRecipe2 = recipeService.saveRecipe(recipeDto2);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/recipe/{recipeId}", savedRecipe2.getId())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        String responseBody = result.getResponse().getContentAsString();
        RecipeDto actualRecipe = objectMapper.readValue(responseBody, RecipeDto.class);
        assertThat(actualRecipe).isNotNull();
        assertThat(actualRecipe.getId()).isEqualTo(savedRecipe2.getId());
        assertThat(actualRecipe.getName()).isEqualTo(savedRecipe2.getName());
    }

    @Test
    public void testSearchRecipes() throws Exception {

        IngredientsDto ingredientsDto2 = IngredientsDto.builder()
                .ingredientName("IngredientSearch").build();
        InstructionsDto instructionsDto2 = InstructionsDto.builder()
                .description("InstructionSearch").build();
        RecipeDto recipeDto2 = RecipeDto.builder()
                .name("Recipe Search")
                .servingNumber(11)
                .isVegetarian(false)
                .ingredients(Set.of(ingredientsDto2))
                .instructions(Set.of(instructionsDto2)).build();
        recipeService.saveRecipe(recipeDto2);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/recipe/search")
                        .param("isVegetarian", "false")
                        .param("servingNumber", "10"))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        RecipeDto[] actualRecipes = objectMapper.readValue(responseBody, RecipeDto[].class);
        assertThat(actualRecipes).hasAtLeastOneElementOfType(RecipeDto.class);
        assertThat(actualRecipes[0].getName()).isEqualTo(recipeDto2.getName());
    }
}
