package world.evgereo.articles.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import world.evgereo.articles.models.Articles;
import world.evgereo.articles.repositories.ArticlesRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ArticlesService {
    private ArticlesRepository articlesRepository;

    public List<Articles> getArticles() {
        return articlesRepository.findAll();
    }

    public Articles getArticle(int id) {
        Optional<Articles> article = articlesRepository.findById(id);
        return article.orElse(null);
    }

    public void updateArticle(Articles article, int id) {
        Optional<Articles> optionalArticles = articlesRepository.findById(id);
        if (optionalArticles.isPresent()) {
            Articles existingArticle = optionalArticles.get();
            existingArticle.setArticleName(article.getArticleName());
            existingArticle.setArticleText(article.getArticleText());
            System.out.println(existingArticle);

            articlesRepository.save(existingArticle);
        }
    }

    public void createArticle(Articles article) {
        articlesRepository.save(article);
    }

    public void deleteArticle(int id) {
        articlesRepository.deleteById(id);
    }
}
