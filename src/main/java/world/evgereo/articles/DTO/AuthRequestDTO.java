package world.evgereo.articles.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthRequestDTO {
    @NotBlank(message = "Email should be not empty")
    @Email(message = "Please provide a valid email address")
    @Size(max=150, message = "Maximum size of email is 150")
    private String email;

    @NotBlank(message = "Password should be not empty")
    @Size(min=2, max=100, message = "Size of your password too short or long")
    private String password;
}
