package nl.abnamro.assessment.recipe.domain;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Orhan Polat
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table( name = "ingredients")
public class Ingredients {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String ingredientName;

    @Column(name = "recipe_id")
    private Long recipeId;
}
