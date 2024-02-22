package world.evgereo.articles.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import world.evgereo.articles.models.Article;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Integer> {
}
