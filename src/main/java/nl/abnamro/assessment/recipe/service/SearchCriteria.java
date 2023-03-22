package nl.abnamro.assessment.recipe.service;

import lombok.Getter;
import lombok.Setter;
import nl.abnamro.assessment.recipe.enums.SearchOperation;

/**
 * @author Orhan Polat
 */

@Getter
@Setter
public class SearchCriteria {

    private String key;
    private SearchOperation operation;
    private Object value;

    public SearchCriteria(String key, SearchOperation operation, Object value) {
        this.key = key;
        this.operation = operation;
        this.value = value;
    }
}
