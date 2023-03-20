package nl.abnamro.assessment.recipe.service;

import lombok.AllArgsConstructor;
import nl.abnamro.assessment.recipe.domain.Recipe;
import nl.abnamro.assessment.recipe.model.RecipeDto;
import nl.abnamro.assessment.recipe.repository.RecipeRepository;
import org.apache.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


/**
 * @author Orhan Polat
 */
@AllArgsConstructor
@Service
public class RecipeServiceImpl implements IRecipeService{

    private static final Logger LOG = Logger.getLogger(RecipeServiceImpl.class);

    private final RecipeRepository recipeRepository;
    private ModelMapper modelMapper;
    @Override
    public List<RecipeDto> findAll() {

        LOG.info("findAll");
        List<Recipe> recipeList = recipeRepository.findAll();
        return recipeList.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private RecipeDto convertToDto(Recipe recipe) {
        return modelMapper.map(recipe, RecipeDto.class);
    }
}
