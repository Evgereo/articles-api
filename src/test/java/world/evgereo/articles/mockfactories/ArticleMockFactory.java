package world.evgereo.articles.mockfactories;

import org.checkerframework.checker.units.qual.C;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import world.evgereo.articles.dtos.CreateCommentDto;
import world.evgereo.articles.dtos.CreateUpdateArticleDto;
import world.evgereo.articles.dtos.UpdateCommentDto;
import world.evgereo.articles.models.Article;
import world.evgereo.articles.models.Comment;

import java.util.List;

import static world.evgereo.articles.mockfactories.UserMockFactory.getFirstUser;

public class ArticleMockFactory {
    private static final Comment firstComment = new Comment(
            1,
            0,
            1,
            getFirstUser(),
            0,
            "comment 1"
    );

    private static final Comment secondComment = new Comment(
            2,
            1,
            1,
            getFirstUser(),
            1,
            "comment 2"
    );

    private static final Comment incorrectComment = new Comment(
            3,
            2,
            1,
            getFirstUser(),
            1,
            "comment 3"
    );

    private static final Article firstArticle = new Article(
            1,
            "test",
            "first",
            "2000-01-01",
            getFirstUser(),
            List.of(firstComment)
    );

    private static final Article secondArticle = new Article(
            2,
            "test",
            "second",
            "2000-01-02",
            getFirstUser(),
            List.of(firstComment)
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

    public static Comment getFirstComment() {
        return ArticleMockFactory.firstComment;
    }

    public static Comment getSecondComment() {
        return ArticleMockFactory.secondComment;
    }

    public static CreateCommentDto getParentCommentDto() {
        return new CreateCommentDto("content");
    }

    public static CreateCommentDto getCorrectChildCommentDto() {
        return new CreateCommentDto(1, 1, "content");
    }

    public static CreateCommentDto getIncorrectChildCommentDto() {
        return new CreateCommentDto(2, 1, "content");
    }

    public static UpdateCommentDto getUpdateCommentDto() {
        return new UpdateCommentDto("text");
    }
}
