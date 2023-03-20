package nl.abnamro.assessment.recipe.domain;

import lombok.*;

import javax.persistence.*;

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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ingredients_id_seq")
    @SequenceGenerator(name = "ingredients_id_seq", sequenceName = "ingredients_id_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String ingredientName;

    @Column(name = "recipe_id")
    private Long recipeId;
}
