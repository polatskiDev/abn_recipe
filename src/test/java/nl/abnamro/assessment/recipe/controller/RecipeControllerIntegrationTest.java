package nl.abnamro.assessment.recipe.controller;

import nl.abnamro.assessment.recipe.message.RestResponse;
import nl.abnamro.assessment.recipe.model.IngredientsDto;
import nl.abnamro.assessment.recipe.model.InstructionsDto;
import nl.abnamro.assessment.recipe.model.RecipeDto;
import nl.abnamro.assessment.recipe.service.IRecipeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * @author Orhan Polat
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RecipeControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private IRecipeService recipeService;

    @Test
    public void testFindAll() {
        ResponseEntity<RestResponse> responseEntity = restTemplate.getForEntity("/api/v1/recipe", RestResponse.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody().getData()).isNotNull();
        assertThat(responseEntity.getBody().getData()).isInstanceOf(List.class);
        assertThat(((List<?>) responseEntity.getBody().getData()).size()).isGreaterThanOrEqualTo(0);
    }

    @Test
    public void testSaveRecipe() {

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

        HttpEntity<RecipeDto> request = new HttpEntity<>(recipeDto1);

        ResponseEntity<RestResponse> responseEntity = restTemplate.postForEntity("/api/v1/recipe", request, RestResponse.class);

        LinkedHashMap<String, Object> data = (LinkedHashMap<String, Object>) responseEntity.getBody().getData();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat((Integer) data.get("id")).isNotNull();
        assertThat((List<LinkedHashMap<String, Object>>)data.get("ingredients")).isNotNull();
        assertThat((List<LinkedHashMap<String, Object>>)data.get("instructions")).isNotNull();
        assertThat((Boolean) data.get("isVegetarian")).isEqualTo(true);
        assertThat((String) data.get("name")).isEqualTo("Recipe 1");
        assertThat((Integer) data.get("servingNumber")).isEqualTo(2);
    }

    @Test
    public void testUpdateRecipe() {

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
        RestResponse<RecipeDto> restResponse = recipeService.saveRecipe(recipeDto1);

        Long saveRecipeId = restResponse.getData().getId();

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

        HttpEntity<RecipeDto> request = new HttpEntity<>(recipeDto);
        ResponseEntity<RestResponse> responseEntity = restTemplate.exchange("/api/v1/recipe/" + saveRecipeId, HttpMethod.PUT, request, RestResponse.class);

        LinkedHashMap<String, Object> data = (LinkedHashMap<String, Object>) responseEntity.getBody().getData();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody().getData()).isNotNull();
        assertThat((Integer) data.get("id")).isEqualTo(saveRecipeId.intValue());
        assertThat((String) data.get("name")).isEqualTo("Recipe 1 Updated");
    }

    @Test
    public void testDeleteRecipe() {
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
        RestResponse<RecipeDto> savedRecipeDto = recipeService.saveRecipe(recipeDto1);
        Long savedRecipeId = savedRecipeDto.getData().getId();

        ResponseEntity<RestResponse> responseEntity = restTemplate.exchange("/api/v1/recipe/" +savedRecipeId, HttpMethod.DELETE, null, RestResponse.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody().getData()).isEqualTo("");
    }

    @Test
    public void testGetRecipeById() {

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

        RestResponse<RecipeDto> savedRecipe2 = recipeService.saveRecipe(recipeDto2);
        Integer savedRecipeId = savedRecipe2.getData().getId().intValue();

        ResponseEntity<RestResponse> responseEntity = restTemplate.getForEntity("/api/v1/recipe/" + savedRecipeId, RestResponse.class);
        LinkedHashMap<String, Object> data = (LinkedHashMap<String, Object>) responseEntity.getBody().getData();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody().getData()).isNotNull();
        assertThat((Integer) data.get("id")).isEqualTo(savedRecipeId);
    }

    @Test
    public void testSearchRecipes() {

        IngredientsDto ingredientsDto2 = IngredientsDto.builder()
                .ingredientName("IngredientSearch").build();
        InstructionsDto instructionsDto2 = InstructionsDto.builder()
                .description("InstructionSearch").build();
        RecipeDto recipeDto2 = RecipeDto.builder()
                .name("Recipe Search")
                .servingNumber(10)
                .isVegetarian(false)
                .ingredients(Set.of(ingredientsDto2))
                .instructions(Set.of(instructionsDto2)).build();
        recipeService.saveRecipe(recipeDto2);

        // Test with one search criteria
        ResponseEntity<RestResponse> responseEntity1 = restTemplate.getForEntity("/api/v1/recipe/search?servingNumber=10", RestResponse.class);

        LinkedHashMap<String, Object> data = (LinkedHashMap<String, Object>)
                ((List<?>) responseEntity1.getBody().getData()).get(0);

        assertThat(responseEntity1.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity1.getBody().getData()).isNotNull();
        assertThat(responseEntity1.getBody().getData()).isInstanceOf(List.class);
        assertThat(((List<?>) responseEntity1.getBody().getData()).size()).isGreaterThanOrEqualTo(1);
        assertThat(data.get("servingNumber")).isEqualTo(10);
    }
}
