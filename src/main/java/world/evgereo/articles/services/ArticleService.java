package world.evgereo.articles.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import world.evgereo.articles.DTOs.CreateUpdateArticleDTO;
import world.evgereo.articles.errors.exceptions.NotFoundException;
import world.evgereo.articles.models.Article;
import world.evgereo.articles.models.User;
import world.evgereo.articles.repositories.ArticleRepository;
import world.evgereo.articles.utils.MapperUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final MapperUtils mapper;

    public List<Article> getArticles() {
        return articleRepository.findAll();
    }

    public Page<Article> getPaginatedArticles(int page, int size) {
        return articleRepository.findAll(PageRequest.of(page, size));
    }

    public Page<Article> getPaginatedArticlesByAuthorId(int authorId, int page, int size) {
        return articleRepository.findAllByAuthorUserId(authorId, PageRequest.of(page, size));
    }

    public Article loadArticleById(int id) {
        Article article = getArticle(id);
        if (article != null) return article;
        else throw new NotFoundException("Article with id " + id + " not found");
    }

    private Article getArticle(int id) {
        return id != 0 ? articleRepository.findById(id).orElse(null) : null;
    }

    public Article updateArticle(CreateUpdateArticleDTO updateArticle, int id) {
        Article article = loadArticleById(id);
        mapper.map(updateArticle, article);
        articleRepository.save(article);
        return article;
    }

    public Article createArticle(CreateUpdateArticleDTO createArticle) {
        Article article = new Article();
        article.setAuthor((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        mapper.map(createArticle, article);
        articleRepository.save(article);
        return article;
    }

    public void deleteArticle(int id) {
        articleRepository.deleteById(id);
    }
}
