package recipes.persistence;

import org.springframework.data.repository.CrudRepository;
import recipes.business.entities.User;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByEmail(String email);
    User save(User user);
}
