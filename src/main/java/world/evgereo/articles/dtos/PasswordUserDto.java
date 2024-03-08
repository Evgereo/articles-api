package world.evgereo.articles.dtos;

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
public class PasswordUserDto {
    @NotBlank(message = "Password should be not empty")
    @Size(min = 2, max = 100, message = "Size of your password too short or long")
    private String password;

    @NotBlank(message = "Password should be not empty")
    @Size(min = 2, max = 100, message = "Size of your password too short or long")
    private String passwordConfirm;
}
