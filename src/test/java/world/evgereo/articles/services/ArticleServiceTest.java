package world.evgereo.articles.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import world.evgereo.articles.errors.exceptions.NotFoundException;
import world.evgereo.articles.models.Article;
import world.evgereo.articles.repositories.ArticleRepository;
import world.evgereo.articles.utils.MapperUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static world.evgereo.articles.mockfactories.ArticleMockFactory.getCreateUpdateArticleDTO;
import static world.evgereo.articles.mockfactories.ArticleMockFactory.getFirstArticle;
import static world.evgereo.articles.mockfactories.UserMockFactory.getFirstUser;

@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {
    @Mock
    private ArticleRepository articleRepository;
    @Mock
    private MapperUtils mapper;
    @InjectMocks
    private ArticleService articleService;
    @Mock
    private Authentication auth;

    @Test
    void loadUserById_withExistingId_getArticle() {
        when(articleRepository.findById(1)).thenReturn(Optional.ofNullable(getFirstArticle()));
        assertEquals(1, articleService.loadArticleById(1).getArticleId());
        verify(articleRepository, times(1)).findById(1);
    }

    @Test
    void loadUserById_withNotExistingId_throwsException() {
        when(articleRepository.findById(5)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> articleService.loadArticleById(5));
        verify(articleRepository, times(1)).findById(5);
    }

    @Test
    void loadArticleAuthorById_withExistingId_getUser() {
        when(articleRepository.findById(1)).thenReturn(Optional.ofNullable(getFirstArticle()));
        assertEquals(1, articleService.loadArticleById(1).getAuthor().getUserId());
        verify(articleRepository, times(1)).findById(1);
    }

    @Test
    void loadArticleAuthorById_withNotExistingId_throwsException() {
        when(articleRepository.findById(5)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> articleService.loadArticleById(5));
        verify(articleRepository, times(1)).findById(5);
    }

    @Test
    void updateArticle_getArticle() {
        when(articleRepository.findById(1)).thenReturn(Optional.ofNullable(getFirstArticle()));
        articleService.updateArticle(getCreateUpdateArticleDTO(), 1);
        verify(articleRepository, times(1)).save(any(Article.class));
    }

    @Test
    void createArticle_getArticle() {
        SecurityContextHolder.getContext().setAuthentication(auth);
        when(auth.getPrincipal()).thenReturn(getFirstUser());
        articleService.createArticle(getCreateUpdateArticleDTO());
        verify(articleRepository, times(1)).save(any(Article.class));
    }

    @Test
    void deleteArticle() {
        articleService.deleteArticle(1);
        verify(articleRepository, times(1)).deleteById(1);
    }
}
