package recipes.business.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import recipes.business.entities.User;
import recipes.persistence.UserRepository;

import java.util.Optional;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private void encryptPassword(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
    }

    public Optional<User> register(User user) {
        Optional<User> optUser = userRepository.findByEmail(user.getEmail());
        if (optUser.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        encryptPassword(user);
        return Optional.of(userRepository.save(user));
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}