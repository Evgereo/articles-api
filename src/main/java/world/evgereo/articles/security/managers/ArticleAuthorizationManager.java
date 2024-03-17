package world.evgereo.articles.security.managers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;
import world.evgereo.articles.errors.exceptions.BadRequestException;
import world.evgereo.articles.errors.exceptions.NotFoundException;
import world.evgereo.articles.models.Comment;
import world.evgereo.articles.services.ArticleService;
import world.evgereo.articles.services.UserService;

import java.util.function.Supplier;

@Component
@RequiredArgsConstructor
@Slf4j
public class ArticleAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {
    private final ArticleService articleService;
    private final UserService userService;

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext context) {
        if (context.getRequest().getParameterNames().nextElement().equals("comment"))
            return checkCommentPermission(authentication.get(), context);
        else
            return checkArticlePermission(authentication.get(), context);
    }

    private AuthorizationDecision checkArticlePermission(Authentication auth, RequestAuthorizationContext context) {
        int articleId = Integer.parseInt(context.getVariables().get("id"));
        boolean isCurrentUserArticle = userService.loadUserByEmail((String) auth.getPrincipal()).getUserId()
                == articleService.loadArticleById(articleId).getAuthor().getUserId();
        ArticleAuthorizationManager.log.debug("Is user page of current user article: {}", isCurrentUserArticle);
        return new AuthorizationDecision(
                isCurrentUserArticle ||
                        auth.getAuthorities().stream().anyMatch(role ->
                                role.getAuthority().equals("ROLE_MODER")));
    }

    private AuthorizationDecision checkCommentPermission(Authentication auth, RequestAuthorizationContext context) {
        Comment comment;
        try {
            comment = articleService.loadCommentById(Integer.parseInt(context.getRequest().getParameterValues("comment")[0]));
        } catch (NumberFormatException | NotFoundException ex) {
            ArticleAuthorizationManager.log.debug("User with id " + userService.loadUserByEmail((String) auth.getPrincipal()).getUserId() +
                    " tried to edit comment with id " + Integer.parseInt(context.getRequest().getParameterValues("comment")[0]));
            return new AuthorizationDecision(false);
        }
        boolean isCurrentUserComment = userService.loadUserByEmail((String) auth.getPrincipal()).getUserId()
                == comment.getAuthor().getUserId();
        ArticleAuthorizationManager.log.debug("Is user page of current user comment: {}", isCurrentUserComment);
        return new AuthorizationDecision(
                isCurrentUserComment ||
                        auth.getAuthorities().stream().anyMatch(role ->
                                role.getAuthority().equals("ROLE_MODER")));
    }
}
