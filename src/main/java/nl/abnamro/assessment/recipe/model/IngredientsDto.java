package nl.abnamro.assessment.recipe.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Orhan Polat
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IngredientsDto {

    private Long id;
    private String ingredientName;
    private Long recipeId;
}
