package recipes.presentation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import recipes.business.entities.Recipe;
import recipes.business.entities.User;
import recipes.business.services.AuthService;
import recipes.business.services.RecipesService;
import recipes.business.wrappers.Id;
import recipes.config.UserDetailsImpl;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
public class RecipesController {
    @Autowired
    RecipesService recipesService;
    @Autowired
    AuthService authService;

    @GetMapping("/api/recipe/{id}")
    public ResponseEntity<Recipe> getRecipe(@PathVariable int id) {
        Optional<Recipe> recipe = recipesService.getRecipe(id);

        return recipe.map(value -> ResponseEntity.ok().body(value)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/api/recipe/search")
    public ResponseEntity find(@RequestParam(required = false) String category, @RequestParam(required = false) String name) {
        if (category == null && name == null || category != null && name != null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        List list;

        if (category != null) {
            list = recipesService.findAllByCategory(category);
        } else {
            list = recipesService.findAllByNameContaining(name);
        }

        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @PostMapping("/api/recipe/new")
    public Id addRecipe(@AuthenticationPrincipal UserDetailsImpl userDetails, @Valid @RequestBody Recipe recipe) {
        User user = authService.findByEmail(userDetails.getUsername()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        recipe.setUser(user);
        recipesService.addRecipe(recipe);
        return new Id(recipe.getId());
    }

    @PutMapping("/api/recipe/{id}")
    public ResponseEntity updateRecipe(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable int id, @Valid @RequestBody Recipe recipe) {
        User authenticatedUser = authService.findByEmail(userDetails.getUsername()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Recipe dbRecipe = recipesService.getRecipe(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        recipe.setUser(dbRecipe.getUser());

        if (!authenticatedUser.getEmail().equals(dbRecipe.getUser().getEmail())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        return recipesService.updateRecipe(id, recipe) ? new ResponseEntity<>(HttpStatus.NO_CONTENT) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/api/recipe/{id}")
    public ResponseEntity deleteRecipe(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable int id) {
        Recipe recipe = recipesService.getRecipe(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        User authenticatedUser = new User(userDetails);
        if (!authenticatedUser.getEmail().equals(recipe.getUser().getEmail())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        boolean deleted = recipesService.deleteRecipe(id);
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
