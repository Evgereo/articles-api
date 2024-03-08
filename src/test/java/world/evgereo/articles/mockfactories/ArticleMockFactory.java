package world.evgereo.articles.mockfactories;

import world.evgereo.articles.dtos.CreateUpdateArticleDto;
import world.evgereo.articles.models.Article;

import java.util.List;

import static world.evgereo.articles.mockfactories.UserMockFactory.getFirstUser;

public class ArticleMockFactory {
    private static final Article firstArticle = new Article(
            1,
            "test",
            "first",
            "2000-01-01",
            getFirstUser()
    );

    private static final Article secondArticle = new Article(
            2,
            "test",
            "second",
            "2000-01-02",
            getFirstUser()
    );


    public static Article getFirstArticle() {
        return ArticleMockFactory.firstArticle;
    }

    public static List<Article> getListOfTwoArticles() {
        return List.of(ArticleMockFactory.firstArticle, ArticleMockFactory.secondArticle);
    }

    public static CreateUpdateArticleDto getCreateUpdateArticleDTO() {
        return new CreateUpdateArticleDto(
                "name",
                "some text"
        );
    }
}
