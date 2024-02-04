package world.evgereo.articles.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RefreshRequestDTO {
    @NotBlank(message = "Refresh token should be not empty or blank")
    private String refreshToken;
}
