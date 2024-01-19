package world.evgereo.articles.services;

import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import world.evgereo.articles.models.Articles;
import world.evgereo.articles.models.Users;
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
            articlesRepository.save(existingArticle);
        }
    }

    public void createArticle(Articles article) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Users currentUser = (Users) authentication.getPrincipal();
        article.setAuthor(currentUser);
        articlesRepository.save(article);
    }

    public void deleteArticle(int id) {
        articlesRepository.deleteById(id);
    }
}
