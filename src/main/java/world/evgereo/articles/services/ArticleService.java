package world.evgereo.articles.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import world.evgereo.articles.dtos.CreateCommentDto;
import world.evgereo.articles.dtos.CreateUpdateArticleDto;
import world.evgereo.articles.dtos.UpdateCommentDto;
import world.evgereo.articles.errors.exceptions.BadRequestException;
import world.evgereo.articles.errors.exceptions.NotFoundException;
import world.evgereo.articles.models.Article;
import world.evgereo.articles.models.Comment;
import world.evgereo.articles.repositories.ArticleRepository;
import world.evgereo.articles.repositories.CommentRepository;
import world.evgereo.articles.utils.MapperUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final CommentRepository commentRepository;
    private final UserService userService;
    private final MapperUtils mapper;

    public List<Article> getArticles() {
        return articleRepository.findAll();
    }

    public Page<Article> getPaginatedArticles(int page, int size) {
        return articleRepository.findAllArticles(PageRequest.of(page, size));
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

    public Article createArticle(CreateUpdateArticleDto createArticle) {
        Article article = new Article();
        String authorEmail = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try {
            article.setAuthor(userService.loadUserByEmail(authorEmail));
        } catch (UsernameNotFoundException ex) {
            throw new BadRequestException("User with email " + authorEmail + "to update has been not found");
        }
        mapper.map(createArticle, article);
        return articleRepository.save(article);
    }

    public Article updateArticle(CreateUpdateArticleDto updateArticle, int id) {
        Article article = getArticle(id);
        if (article != null) {
            mapper.map(updateArticle, article);
            return articleRepository.save(article);
        } else throw new BadRequestException("Article with id " + id + " to update not found");
    }

    public void deleteArticle(int id) {
        articleRepository.deleteById(id);
    }

    public Comment loadCommentById(int id) {
        Comment comment = getComment(id);
        if (comment != null) return comment;
        else throw new NotFoundException("Comment with id " + id + " not found");
    }

    public Comment getComment(int id) {
        return id != 0 ? commentRepository.findById(id).orElse(null) : null;
    }

    @Transactional
    public Article createComment(CreateCommentDto createComment, int articleId) {
        try {
            loadArticleById(articleId);
        } catch (NotFoundException ex) {
            throw new BadRequestException("Article with id " + articleId + " to add comment not found");
        }
        if (createComment.getParentId() != 0) try {
            if (loadCommentById(createComment.getParentId()).getParentId() != 0)
                throw new BadRequestException("Parent comment with id " + createComment.getParentId() + " can't be parent");
        } catch (NotFoundException ex) {
            throw new BadRequestException("Parent comment with id " + createComment.getParentId() + " not found");
        }
        if (createComment.getToUserId() != 0) try {
            userService.loadUserById(createComment.getToUserId());
        } catch (NotFoundException ex) {
            throw new BadRequestException("User with id " + createComment.getToUserId() + " to whom you are replying not found");
        }
        Comment comment = new Comment();
        comment.setArticleId(articleId);
        String authorEmail = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try {
            comment.setAuthor(userService.loadUserByEmail(authorEmail));
        } catch (UsernameNotFoundException ex) {
            throw new BadRequestException("User with email " + authorEmail + "to update has been not found");
        }
        mapper.map(createComment, comment);
        commentRepository.save(comment);
        return loadArticleById(articleId);
    }

    @Transactional
    public Article updateComment(UpdateCommentDto updateComment, int commentId) {
        Comment comment = getComment(commentId);
        if (comment != null) {
            mapper.map(updateComment, comment);
            commentRepository.save(comment);
            return loadArticleById(comment.getArticleId());
        } else throw new BadRequestException("Comment with id " + commentId + " to update not found");
    }

    @Transactional
    public Article deleteComment(int id) {
        Comment comment;
        comment = getComment(id);
        if (comment != null) {
            commentRepository.deleteByParentId(id);
            commentRepository.deleteById(id);
            return loadArticleById(comment.getArticleId());
        } else throw new BadRequestException("Comment with id " + id + " to delete not found");
    }
}
