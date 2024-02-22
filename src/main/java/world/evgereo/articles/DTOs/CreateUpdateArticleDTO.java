package world.evgereo.articles.DTOs;

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
public class CreateUpdateArticleDTO {
    @NotBlank(message = "Article name should be not empty")
    @Size(min = 2, max = 300, message = "Article name should be between 2 and 300")
    private String articleName;

    private String articleText;
}
