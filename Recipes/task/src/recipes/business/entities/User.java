package recipes.business.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import recipes.config.UserDetailsImpl;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private long id;

    @Email
    @NotBlank(message = "Email is required")
    @Pattern(regexp = "[a-zA-z0-9]+@.+\\..+")
    private String email;

    @NotBlank(message = "Password is required")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Size(min = 8)
    private String password;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private Set<Recipe> recipes = new HashSet<>();

    public User(String email, String password) {
        this.email = email.toLowerCase();
        this.password = password;
    }

    public User(UserDetailsImpl userDetails) {
        this.email = userDetails.getUsername();
        this.password = userDetails.getPassword();
    }
}