package world.evgereo.articles.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import world.evgereo.articles.errors.exceptions.BadRequestException;
import world.evgereo.articles.errors.exceptions.NotFoundException;
import world.evgereo.articles.models.Article;
import world.evgereo.articles.models.Comment;
import world.evgereo.articles.repositories.ArticleRepository;
import world.evgereo.articles.repositories.CommentRepository;
import world.evgereo.articles.utils.MapperUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static world.evgereo.articles.mockfactories.ArticleMockFactory.*;
import static world.evgereo.articles.mockfactories.UserMockFactory.getFirstUser;

@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {
    @Mock
    private ArticleRepository articleRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private UserService userService;
    @Mock
    private MapperUtils mapper;
    @InjectMocks
    private ArticleService articleService;
    @Mock
    private Authentication auth;

    @Test
    void loadUserById_withExistingId_getArticle() {
        when(articleRepository.findById(1)).thenReturn(Optional.ofNullable(getFirstArticle()));
        assertEquals(getFirstArticle(), articleService.loadArticleById(1));
        verify(articleRepository, times(1)).findById(1);
    }

    @Test
    void loadUserById_withNotExistingId_throwsException() {
        when(articleRepository.findById(5)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> articleService.loadArticleById(5));
        verify(articleRepository, times(1)).findById(5);
    }

    @Test
    void createArticle_withExistingAuthor_getArticle() {
        SecurityContextHolder.getContext().setAuthentication(auth);
        when(auth.getPrincipal()).thenReturn(getFirstUser().getEmail());
        when(userService.loadUserByEmail(any(String.class))).thenReturn(getFirstUser());
        when(articleRepository.save(any(Article.class))).thenReturn(getFirstArticle());
        assertEquals(getFirstArticle(), articleService.createArticle(getCreateUpdateArticleDto()));
        verify(userService, times(1)).loadUserByEmail(any(String.class));
        verify(articleRepository, times(1)).save(any(Article.class));
    }

    @Test
    void createArticle_withNotExistingAuthor_throwsException() {
        SecurityContextHolder.getContext().setAuthentication(auth);
        when(auth.getPrincipal()).thenReturn(null);
        when(userService.loadUserByEmail(null)).thenThrow(new UsernameNotFoundException("Username not found"));
        assertThrows(BadRequestException.class, () -> articleService.createArticle(getCreateUpdateArticleDto()));
        verify(userService, times(1)).loadUserByEmail(null);
        verify(articleRepository, times(0)).save(any(Article.class));
    }

    @Test
    void updateArticle_withExistingId_getArticle() {
        when(articleRepository.findById(1)).thenReturn(Optional.ofNullable(getFirstArticle()));
        when(articleRepository.save(any(Article.class))).thenReturn(getFirstArticle());
        assertEquals(getFirstArticle(), articleService.updateArticle(getCreateUpdateArticleDto(), 1));
        verify(articleRepository, times(1)).findById(1);
        verify(articleRepository, times(1)).save(any(Article.class));
    }

    @Test
    void updateArticle_withNotExistingId_throwsException() {
        when(articleRepository.findById(5)).thenReturn(Optional.empty());
        assertThrows(BadRequestException.class, () -> articleService.updateArticle(getCreateUpdateArticleDto(), 5));
        verify(articleRepository, times(0)).save(any(Article.class));
    }

    @Test
    void deleteArticle() {
        articleService.deleteArticle(1);
        verify(articleRepository, times(1)).deleteById(1);
    }

    @Test
    void loadCommentById_withExistingId_getComment() {
        when(commentRepository.findById(1)).thenReturn(Optional.ofNullable(getFirstComment()));
        assertEquals(getFirstComment(), articleService.loadCommentById(1));
        verify(commentRepository, times(1)).findById(1);
    }

    @Test
    void loadCommentById_withNotExistingId_throwsException() {
        when(commentRepository.findById(5)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> articleService.loadCommentById(5));
        verify(commentRepository, times(1)).findById(5);
    }

    @Test
    void createComment_withCorrectParentComment_getArticle() {
        SecurityContextHolder.getContext().setAuthentication(auth);
        when(auth.getPrincipal()).thenReturn(getFirstUser().getEmail());
        when(userService.loadUserByEmail(any(String.class))).thenReturn(getFirstUser());
        when(articleRepository.findById(1)).thenReturn(Optional.ofNullable(getFirstArticle()));
        assertEquals(getFirstArticle(), articleService.createComment(getParentCommentDto(), 1));
        verify(userService, times(0)).loadUserById(1);
        verify(userService, times(1)).loadUserByEmail(any(String.class));
        verify(articleRepository, times(2)).findById(1);
        verify(commentRepository, times(0)).findById(1);
        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    void createComment_withCorrectChildComment_getArticle() {
        SecurityContextHolder.getContext().setAuthentication(auth);
        when(auth.getPrincipal()).thenReturn(getFirstUser().getEmail());
        when(userService.loadUserByEmail(any(String.class))).thenReturn(getFirstUser());
        when(userService.loadUserById(1)).thenReturn(getFirstUser());
        when(articleRepository.findById(1)).thenReturn(Optional.ofNullable(getFirstArticle()));
        when(commentRepository.findById(1)).thenReturn(Optional.ofNullable(getFirstComment()));
        assertEquals(getFirstArticle(), articleService.createComment(getCorrectChildCommentDto(), 1));
        verify(userService, times(1)).loadUserById(1);
        verify(userService, times(1)).loadUserByEmail(any(String.class));
        verify(articleRepository, times(2)).findById(1);
        verify(commentRepository, times(1)).findById(1);
        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    void createComment_withNotExistingArticleId_throwsException() {
        when(articleRepository.findById(5)).thenThrow(new NotFoundException("Article not found"));
        assertThrows(BadRequestException.class, () -> articleService.createComment(getParentCommentDto(), 5));
        verify(articleRepository, times(1)).findById(5);
        verify(commentRepository, times(0)).save(any(Comment.class));
    }

    @Test
    void createComment_withNotExistingParentId_throwsException() {
        when(articleRepository.findById(1)).thenReturn(Optional.ofNullable(getFirstArticle()));
        when(commentRepository.findById(2)).thenReturn(Optional.empty());
        assertThrows(BadRequestException.class, () -> articleService.createComment(getIncorrectChildCommentDto(), 1));
        verify(articleRepository, times(1)).findById(1);
        verify(commentRepository, times(1)).findById(2);
        verify(commentRepository, times(0)).save(any(Comment.class));
    }

    @Test
    void createComment_withIncorrectParentId_throwsException() {
        when(articleRepository.findById(1)).thenReturn(Optional.ofNullable(getFirstArticle()));
        when(commentRepository.findById(2)).thenReturn(Optional.ofNullable(getSecondComment()));
        assertThrows(BadRequestException.class, () -> articleService.createComment(getIncorrectChildCommentDto(), 1));
        verify(articleRepository, times(1)).findById(1);
        verify(commentRepository, times(1)).findById(2);
        verify(commentRepository, times(0)).save(any(Comment.class));
    }

    @Test
    void createComment_withNotExistingToUserId_throwsException() {
        when(userService.loadUserById(1)).thenThrow(new NotFoundException("User not found"));
        when(articleRepository.findById(1)).thenReturn(Optional.ofNullable(getFirstArticle()));
        when(commentRepository.findById(1)).thenReturn(Optional.ofNullable(getFirstComment()));
        assertThrows(BadRequestException.class, () -> articleService.createComment(getCorrectChildCommentDto(), 1));
        verify(userService, times(1)).loadUserById(1);
        verify(articleRepository, times(1)).findById(1);
        verify(commentRepository, times(1)).findById(1);
        verify(commentRepository, times(0)).save(any(Comment.class));
    }

    @Test
    void createComment_withNotExistingAuthor_throwsException() {
        SecurityContextHolder.getContext().setAuthentication(auth);
        when(auth.getPrincipal()).thenReturn(getFirstUser().getEmail());
        when(userService.loadUserByEmail(any(String.class))).thenThrow(new UsernameNotFoundException("User not found"));
        when(userService.loadUserById(1)).thenReturn(getFirstUser());
        when(articleRepository.findById(1)).thenReturn(Optional.ofNullable(getFirstArticle()));
        when(commentRepository.findById(1)).thenReturn(Optional.ofNullable(getFirstComment()));
        assertThrows(BadRequestException.class, () -> articleService.createComment(getCorrectChildCommentDto(), 1));
        verify(userService, times(1)).loadUserById(1);
        verify(userService, times(1)).loadUserByEmail(any(String.class));
        verify(articleRepository, times(1)).findById(1);
        verify(commentRepository, times(1)).findById(1);
        verify(commentRepository, times(0)).save(any(Comment.class));
    }

    @Test
    void updateComment_withExistingCommentId_getArticle() {
        when(commentRepository.findById(1)).thenReturn(Optional.ofNullable(getFirstComment()));
        when(articleRepository.findById(1)).thenReturn(Optional.ofNullable(getFirstArticle()));
        assertEquals(getFirstArticle(), articleService.updateComment(getUpdateCommentDto(), 1));
        verify(articleRepository, times(1)).findById(1);
        verify(commentRepository, times(1)).findById(1);
        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    void updateComment_withNotExistingCommentId_throwsException() {
        when(commentRepository.findById(5)).thenReturn(Optional.empty());
        assertThrows(BadRequestException.class, () -> articleService.updateComment(getUpdateCommentDto(), 5));
        verify(commentRepository, times(1)).findById(5);
        verify(commentRepository, times(0)).save(any(Comment.class));
    }
    
    @Test
    void deleteComment_withExistingCommentId_getArticle() {
        when(commentRepository.findById(1)).thenReturn(Optional.ofNullable(getFirstComment()));
        when(articleRepository.findById(1)).thenReturn(Optional.ofNullable(getFirstArticle()));
        assertEquals(getFirstArticle(), articleService.deleteComment(1));
        verify(commentRepository, times(1)).findById(1);
        verify(commentRepository, times(1)).deleteByParentId(1);
        verify(commentRepository, times(1)).deleteById(1);
        verify(articleRepository, times(1)).findById(1);
    }

    @Test
    void deleteComment_withNotExistingCommentId_throwsException() {
        when(commentRepository.findById(5)).thenReturn(Optional.empty());
        assertThrows(BadRequestException.class, () -> articleService.deleteComment(5));
        verify(commentRepository, times(1)).findById(5);
        verify(articleRepository, times(0)).findById(1);
    }
}
