package nl.abnamro.assessment.recipe.service;

import nl.abnamro.assessment.recipe.domain.Ingredients;
import nl.abnamro.assessment.recipe.domain.Instructions;
import nl.abnamro.assessment.recipe.domain.Recipe;
import nl.abnamro.assessment.recipe.enums.SearchOperation;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Orhan Polat
 */
public class RecipeSpecification implements Specification<Recipe> {

    private List<SearchCriteria> criteriaList;

    public RecipeSpecification() {
        criteriaList = new ArrayList<>();
    }

    public RecipeSpecification(List<SearchCriteria> searchCriteriaList) {
        this.criteriaList = searchCriteriaList;
    }

    public void addCriteria(SearchCriteria criteria) {
        criteriaList.add(criteria);
    }

    @Override
    public Predicate toPredicate(Root<Recipe> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();
        Join<Recipe, Ingredients> recipeIngredients = ingredientsJoin(root);
        Join<Recipe, Instructions> recipeInstructions = instructionsJoin(root);

        for (SearchCriteria criteria : criteriaList) {
            if (criteria.getOperation() == SearchOperation.EQUALITY) {
                predicates.add(criteriaBuilder.equal(root.get(criteria.getKey()), criteria.getValue()));

            } else if (criteria.getOperation() == SearchOperation.IN) {
                predicates.add(criteriaBuilder.in(root.get(criteria.getKey())).value(criteria.getValue()));

            } else if (criteria.getOperation() == SearchOperation.NOT_IN) {
                predicates.add(criteriaBuilder.not(root.get(criteria.getKey())).in(criteria.getValue()));

            } else if (criteria.getOperation() == SearchOperation.LIKE) {
                predicates.add(criteriaBuilder.like(root.get(criteria.getKey()), "%" + criteria.getValue() + "%"));

            } else if (criteria.getOperation() == SearchOperation.GREATER_THAN_OR_EQUAL) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(criteria.getKey()), criteria.getValue().toString()));

            } else if (SearchOperation.CONTAINS.equals(criteria.getOperation())) {

                if ("ingredientName".equals(criteria.getKey())) {
                    predicates.add(criteriaBuilder.like(criteriaBuilder.lower(recipeIngredients.get(criteria.getKey())),
                            "%" + criteria.getValue() + "%"));

                } else if ("description".equals(criteria.getKey())){
                    predicates.add(criteriaBuilder.like(criteriaBuilder.lower(recipeInstructions.get(criteria.getKey())),
                            "%" + criteria.getValue() + "%"));

                } else {
                    predicates.add(criteriaBuilder.like(root.get(criteria.getKey()), "%" + criteria.getKey() + "%"));
                }

            } else if (SearchOperation.NOT_CONTAINS.equals(criteria.getOperation())) {

                if ("ingredientName".equals(criteria.getKey())) {
                    predicates.add(criteriaBuilder.notLike(criteriaBuilder.lower(recipeIngredients.get(criteria.getKey())),
                            "%" + criteria.getValue() + "%"));

                } else if ("description".equals(criteria.getKey())){
                    predicates.add(criteriaBuilder.notLike(criteriaBuilder.lower(recipeInstructions.get(criteria.getKey())),
                            "%" + criteria.getValue() + "%"));

                } else {
                    predicates.add(criteriaBuilder.notLike(root.get(criteria.getKey()), "%" + criteria.getKey() + "%"));
                }

            } else {
                predicates.add(criteriaBuilder.equal(root.get(criteria.getKey()), criteria.getValue()));
            }
        }

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }

    private Join<Recipe, Ingredients> ingredientsJoin(Root<Recipe> root) {
        return root.join("ingredients");
    }

    private Join<Recipe, Instructions> instructionsJoin(Root<Recipe> root) {
        return root.join("instructions");
    }
}
