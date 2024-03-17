package world.evgereo.articles.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateCommentDto {
    private int parentId;

    private int toUserId;

    @NotBlank(message = "Comment should be not empty")
    private String content;

    public CreateCommentDto(int userId, String content) {
        this(0, 0, content);
    }
}
