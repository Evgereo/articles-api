package world.evgereo.articles.models;

import lombok.Data;
import lombok.Getter;
import org.springframework.data.annotation.Id;

@Data
public class Articles {
    @Id
    private int articleId;
    @Getter
    private int authorId;
    private String articleName;
    private String articleText;
    private String postingDate;
}
