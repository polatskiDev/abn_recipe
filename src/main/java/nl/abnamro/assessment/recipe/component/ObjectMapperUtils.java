package nl.abnamro.assessment.recipe.component;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Orhan Polat
 */
@Component
public class ObjectMapperUtils {

    private final ModelMapper modelMapper;

    public ObjectMapperUtils() {
        modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }

    public <D,T> D map(final T entity, Class<D> outClass) {

        return modelMapper.map(entity, outClass);
    }

    public <D,T> Set<D> mapAll(final Collection<T> entityList, Class<D> outClass) {

        return entityList.stream()
                .map(
                        entity -> map(entity,outClass)
                )
                .collect(Collectors.toSet());
    }
}
