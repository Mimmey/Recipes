package recipes.business.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import recipes.business.entities.Recipe;
import recipes.persistence.RecipesRepository;

import java.util.List;
import java.util.Optional;

@Service
public class RecipesService {

    @Autowired
    RecipesRepository recipesRepository;

    private boolean existsById(Integer id) {
        return recipesRepository.existsById(id);
    }

    public Recipe addRecipe(Recipe recipe) {
        return recipesRepository.save(recipe);
    }

    public Optional<Recipe> getRecipe(Integer id) {
        return recipesRepository.findById(id);
    }

    public boolean deleteRecipe(Integer id) {
        boolean exists = existsById(id);

        if (exists) {
            recipesRepository.deleteById(id);
        }

        return exists;
    }

    public List<Recipe> findAllByCategory(String category) {
        return recipesRepository.findAllByCategoryIgnoreCaseOrderByDateDesc(category);
    }

    public List<Recipe> findAllByNameContaining(String name) {
        return recipesRepository.findAllByNameContainsIgnoreCaseOrderByDateDesc(name);
    }

    public boolean updateRecipe(Integer id, Recipe recipe) {
        recipe.setId(id);
        boolean exists = existsById(id);

        if (exists) {
            recipesRepository.save(recipe);
        }

        return exists;
    }
}
