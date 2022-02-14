package recipes.business.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Data
@AllArgsConstructor
@Entity
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    private int id;

    @NotBlank
    private String name = "";

    @NotBlank
    private String category = "";

    private String date;

    @NotBlank
    private String description = "";

    @NotNull
    @Size(min = 1)
    private ArrayList<String> ingredients = new ArrayList<>();

    @NotNull
    @Size(min = 1)
    private ArrayList<String> directions = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "recipes", nullable = false)
    @JsonIgnore
    private User user;

    public Recipe() {
        this.date = LocalDateTime.now().toString();
    }
}
