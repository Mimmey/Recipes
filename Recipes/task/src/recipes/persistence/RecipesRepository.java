package recipes.persistence;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import recipes.business.entities.Recipe;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecipesRepository extends CrudRepository<Recipe, Integer> {

    Recipe save(Recipe recipe);
    Optional<Recipe> findById(Integer id);
    boolean existsById(Integer integer);
    void deleteById(Integer id);
    List<Recipe> findAllByCategoryIgnoreCaseOrderByDateDesc(String category);
    List<Recipe> findAllByNameContainsIgnoreCaseOrderByDateDesc(String name);
}
