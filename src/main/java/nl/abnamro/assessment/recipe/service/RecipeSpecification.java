package nl.abnamro.assessment.recipe.service;

import nl.abnamro.assessment.recipe.domain.Ingredients;
import nl.abnamro.assessment.recipe.domain.Instructions;
import nl.abnamro.assessment.recipe.domain.Recipe;
import nl.abnamro.assessment.recipe.enums.SearchOperation;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

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

        for (SearchCriteria criteria : criteriaList) {
            if (SearchOperation.EQUALITY.equals(criteria.getOperation())) {
                predicates.add(criteriaBuilder.equal(root.get(criteria.getKey()), criteria.getValue()));

            } else if (SearchOperation.IN.equals(criteria.getOperation())) {
                predicates.add(criteriaBuilder.in(root.get(criteria.getKey())).value(criteria.getValue()));

            } else if (SearchOperation.NOT_IN.equals(criteria.getOperation())) {
                predicates.add(criteriaBuilder.not(root.get(criteria.getKey())).in(criteria.getValue()));

            } else if (SearchOperation.LIKE.equals(criteria.getOperation())) {
                predicates.add(criteriaBuilder.like(root.get(criteria.getKey()), "%" + criteria.getValue() + "%"));

            } else if (SearchOperation.GREATER_THAN_OR_EQUAL.equals(criteria.getOperation())) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(criteria.getKey()), criteria.getValue().toString()));

            } else if (SearchOperation.CONTAINS.equals(criteria.getOperation())) {

                if ("ingredientName".equals(criteria.getKey())) {
                    Join<Recipe, Ingredients> recipeIngredients = getExistingJoin(root, "ingredients", Ingredients.class);
                    predicates.add(criteriaBuilder.like(criteriaBuilder.lower(recipeIngredients.get(criteria.getKey())),
                            "%" + criteria.getValue() + "%"));

                } else if ("description".equals(criteria.getKey())){
                    Join<Recipe, Instructions> recipeInstructions = getExistingJoin(root, "instructions", Instructions.class);
                    predicates.add(criteriaBuilder.like(criteriaBuilder.lower(recipeInstructions.get(criteria.getKey())),
                            "%" + criteria.getValue() + "%"));

                } else {
                    predicates.add(criteriaBuilder.like(root.get(criteria.getKey()), "%" + criteria.getKey() + "%"));
                }

            } else if (SearchOperation.NOT_CONTAINS.equals(criteria.getOperation())) {

                if ("ingredientName".equals(criteria.getKey())) {
                    Join<Recipe, Ingredients> recipeIngredients = getExistingJoin(root, "ingredients", Ingredients.class);
                    predicates.add(criteriaBuilder.notLike(criteriaBuilder.lower(recipeIngredients.get(criteria.getKey())),
                            "%" + criteria.getValue() + "%"));

                } else if ("description".equals(criteria.getKey())){
                    Join<Recipe, Instructions> recipeInstructions = getExistingJoin(root, "instructions", Instructions.class);
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

    public <R, J> Join<R, J> getExistingJoin(Root<R> root, String attribute, Class<J> joinedEntity) {
        Join<R, J> genericJoin = null;
        final Set<Join<R, ?>> joins = root.getJoins();
        if (!joins.isEmpty()) {
            final Iterator<Join<R, ?>> iterator = joins.iterator();
            while (iterator.hasNext()) {
                final Join<R, ?> next = iterator.next();
                if (next.getModel().getBindableJavaType().getSimpleName().equals(joinedEntity.getSimpleName())) {
                    genericJoin = (Join<R, J>) next;
                    break;
                }
            }
            if (genericJoin == null) {
                genericJoin = root.join(attribute);
            }
        } else {
            genericJoin = root.join(attribute);
        }
        return genericJoin;
    }
}
