package nl.abnamro.assessment.recipe.repository;

import nl.abnamro.assessment.recipe.domain.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Orhan Polat
 */
@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {

}
