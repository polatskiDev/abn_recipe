package nl.abnamro.assessment.recipe.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.abnamro.assessment.recipe.message.RestResponse;
import nl.abnamro.assessment.recipe.model.IngredientsDto;
import nl.abnamro.assessment.recipe.model.InstructionsDto;
import nl.abnamro.assessment.recipe.model.RecipeDto;
import nl.abnamro.assessment.recipe.service.IRecipeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Orhan Polat
 */
@ExtendWith(SpringExtension.class)
@WebMvcTest(RecipeController.class)
public class RecipeControllerTest {

    @MockBean
    IRecipeService recipeService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void testFindAll() throws Exception {

        given(recipeService.findAll()).willReturn(new RestResponse(List.of(getValidRecipeDto()), HttpStatus.OK));

        mockMvc.perform(get("/api/v1/recipe").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    @Test
    void testSaveRecipe() throws Exception {

        RecipeDto recipeDto = getValidRecipeDto();

        String recipeDtoJson = objectMapper.writeValueAsString(recipeDto);

        given(recipeService.saveRecipe(any())).willReturn(new RestResponse(getValidRecipeDto(), HttpStatus.CREATED));

        mockMvc.perform(post("/api/v1/recipe")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(recipeDtoJson))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateRecipe() throws Exception {

        given(recipeService.updateRecipe(any(), any()))
                .willReturn(new RestResponse(getValidRecipeDto(), HttpStatus.NO_CONTENT));

        RecipeDto recipeDto = getValidRecipeDto();
        String recipeDtoJson = objectMapper.writeValueAsString(recipeDto);

        mockMvc.perform(put("/api/v1/recipe/" + 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(recipeDtoJson))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteRecipe() throws Exception {

        given(recipeService.deleteRecipe(any()))
                .willReturn(new RestResponse(null, HttpStatus.NO_CONTENT));

        mockMvc.perform(delete("/api/v1/recipe/" + 1L).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testGetRecipeById() throws Exception {
        //given
        given(recipeService.findRecipe(any())).willReturn(new RestResponse(getValidRecipeDto(), HttpStatus.OK));

        mockMvc.perform(get("/api/v1/recipe/" + 1L).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void searchRecipesTest() throws Exception {

        given(recipeService.searchRecipes(any())).willReturn(new RestResponse(List.of(getValidRecipeDto()), HttpStatus.OK));


        mockMvc.perform(get("/api/v1/recipe/search")
                        .param("isVegetarian", "true")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    private RecipeDto getValidRecipeDto() {
        return RecipeDto.builder()
                .id(1L)
                .isVegetarian(true)
                .name("Recipe1")
                .servingNumber(4)
                .ingredients(Set.of(new IngredientsDto()))
                .instructions(Set.of(new InstructionsDto()))
                .build();
    }
}