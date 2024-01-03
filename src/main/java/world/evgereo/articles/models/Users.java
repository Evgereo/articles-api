package world.evgereo.articles.models;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class Users {
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
}
