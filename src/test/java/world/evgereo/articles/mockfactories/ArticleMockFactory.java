package world.evgereo.articles.mockfactories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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
            getFirstUser(),
            null
    );

    private static final Article secondArticle = new Article(
            2,
            "test",
            "second",
            "2000-01-02",
            getFirstUser(),
            null
    );


    public static Article getFirstArticle() {
        return ArticleMockFactory.firstArticle;
    }

    public static List<Article> getListOfTwoArticles() {
        return List.of(ArticleMockFactory.firstArticle, ArticleMockFactory.secondArticle);
    }

    public static Page<Article> getPageOfTwoArticles() {
        return new PageImpl<>(ArticleMockFactory.getListOfTwoArticles(), PageRequest.of(0, 10), 100L);
    }

    public static Page<Article> getEmptyPage() {
        return new PageImpl<>(List.of(), PageRequest.of(0, 10), 100L);
    }

    public static CreateUpdateArticleDto getCreateUpdateArticleDto() {
        return new CreateUpdateArticleDto(
                "name",
                "some text"
        );
    }
}
