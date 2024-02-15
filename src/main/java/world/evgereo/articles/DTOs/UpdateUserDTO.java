package world.evgereo.articles.DTOs;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserDTO {
    @Size(min=2, max=100, message = "Size of your name too short or long")
    private String username;

    @Size(min=2, max=100, message = "Size of your surname too short or long")
    private String userSurname;

    @Min(3)
    private int age;

    @Email(message = "Please provide a valid email address")
    @Size(max=150, message = "Maximum size of email is 150")
    private String email;
}
