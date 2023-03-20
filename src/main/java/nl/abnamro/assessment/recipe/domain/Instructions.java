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
@Table( name = "instructions")
public class Instructions {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "instructions_id_seq")
    @SequenceGenerator(name = "instructions_id_seq", sequenceName = "instructions_id_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "description")
    private String description;

    @Column(name = "recipe_id")
    private Long recipeId;
}
