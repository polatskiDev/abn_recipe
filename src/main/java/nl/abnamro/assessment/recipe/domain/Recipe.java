package nl.abnamro.assessment.recipe.domain;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Orhan Polat
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table( name = "recipe")
public class Recipe {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "serving_number")
    private Integer servingNumber;

    @Column(name = "is_vegetarian")
    private Boolean isVegetarian;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id")
    private Set<Ingredients> ingredients = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id")
    private Set<Instructions> instructions = new HashSet<>();
}
