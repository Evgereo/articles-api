package world.evgereo.articles.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import world.evgereo.articles.models.Article;
import world.evgereo.articles.models.User;
import world.evgereo.articles.repositories.ArticleRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articlesRepository;

    public List<Article> getArticles() {
        return articlesRepository.findAll();
    }

    public Article getArticle(int id) {
        Optional<Article> article = articlesRepository.findById(id);
        return article.orElse(null);
    }

    public void updateArticle(Article article, int id) {
        Optional<Article> optionalArticles = articlesRepository.findById(id);
        if (optionalArticles.isPresent()) {
            Article existingArticle = optionalArticles.get();
            existingArticle.setArticleName(article.getArticleName());
            existingArticle.setArticleText(article.getArticleText());
            articlesRepository.save(existingArticle);
        }
    }

    public void createArticle(Article article) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        article.setAuthor(currentUser);
        articlesRepository.save(article);
    }

    public void deleteArticle(int id) {
        articlesRepository.deleteById(id);
    }
}
