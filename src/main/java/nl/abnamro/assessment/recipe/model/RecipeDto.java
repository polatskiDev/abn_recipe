package nl.abnamro.assessment.recipe.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * @author Orhan Polat
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeDto {

    private Long id;
    private String name;
    private Integer servingNumber;
    private Boolean isVegetarian;
    private Set<IngredientsDto> ingredients;
    private Set<InstructionsDto> instructions;
}
