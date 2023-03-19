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
@Table( name = "instructions")
public class Instructions {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "description")
    private String description;

    @Column(name = "recipe_id")
    private Long recipeId;
}
