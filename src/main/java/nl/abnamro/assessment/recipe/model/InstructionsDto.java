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
public class InstructionsDto {

    private Long id;
    private String description;
    private Long recipeId;
}
