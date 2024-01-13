package world.evgereo.articles.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Users {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;

    @NotBlank(message = "Name should be not empty or blank")
    @Size(min=2, max=100, message = "Size of your name too short or long")
    private String userName;

    @NotBlank(message = "Surname should be not empty or blank")
    @Size(min=2, max=100, message = "Size of your surname too short or long")
    private String userSurname;

    @Min(0)
    private int age;

    @NotBlank(message = "Email should be not empty")
    @Email(message = "Please provide a valid email address")
    @Size(max=150, message = "Maximum size of email is 150")
    private String email;

    @Size(min=2, max=100, message = "Size of your password too short or long")
    private String password;

    @OneToMany(mappedBy = "author") // look for information on the cascade types // orphanRemoval = true, cascade = CascadeType.ALL
    private List<Articles> articles = new ArrayList<>();
}
