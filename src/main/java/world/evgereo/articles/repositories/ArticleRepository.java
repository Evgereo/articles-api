package world.evgereo.articles.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import world.evgereo.articles.models.Article;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Integer> {
    @Query(value = "select a.articleId, a.articleName, a.articleText, a.author, a.postingDate from Article a")
    Page<Article> findAllArticles(Pageable pageable);

    @Query(value = "select a.articleId, a.articleName, a.articleText, a.author, a.postingDate from Article a where a.author.userId = :authorId")
    Page<Article> findAllByAuthorUserId(int authorId, Pageable pageable);
}
