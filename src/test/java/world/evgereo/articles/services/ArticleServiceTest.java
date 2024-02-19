package world.evgereo.articles.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import world.evgereo.articles.errors.exceptions.NotFoundException;
import world.evgereo.articles.repositories.ArticleRepository;
import world.evgereo.articles.utils.MapperUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static world.evgereo.articles.mockfactories.ArticleMockFactory.getFirstArticle;

@ExtendWith(MockitoExtension.class)
public class ArticleServiceTest {
    @Mock
    private ArticleRepository articleRepository;
    @Mock
    private MapperUtils mapper;
    @InjectMocks
    private ArticleService articleService;

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
}
