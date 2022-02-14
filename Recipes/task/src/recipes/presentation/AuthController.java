package recipes.presentation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import recipes.business.entities.User;
import recipes.business.services.AuthService;

import javax.validation.Valid;

@RestController
public class AuthController {
    @Autowired
    AuthService authService;

    @PostMapping("/api/register")
    public void register(@Valid @RequestBody User user) {
        authService.register(user);
    }
}
