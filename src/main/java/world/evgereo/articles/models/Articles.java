package world.evgereo.articles.models;

import lombok.Data;

@Data
public class Articles {
    private int articleId;
    private int authorId;
    private String articleName;
    private String articleText;
    private String postingDate;
}
