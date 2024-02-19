package world.evgereo.articles.mockfactories;

import world.evgereo.articles.models.Article;

import static world.evgereo.articles.mockfactories.UserMockFactory.getFirstUser;

public class ArticleMockFactory {
    private static final Article firstArticle = new Article(
            1,
            "test",
            "first",
            "2000-01-01",
            getFirstUser()
    );


    public static Article getFirstArticle() {
        return ArticleMockFactory.firstArticle;
    }
}
