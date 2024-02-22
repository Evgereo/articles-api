package world.evgereo.articles.security.managers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;
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
        Authentication auth = authentication.get();
        int articleId = Integer.parseInt(context.getVariables().get("id"));
        boolean isCurrentUserArticle = userService.loadUserByEmail((String) auth.getPrincipal()).getUserId()
                == articleService.loadArticleAuthorById(articleId).getUserId();
        ArticleAuthorizationManager.log.debug("Is user page of current user article: {}", isCurrentUserArticle);
        return new AuthorizationDecision(
                isCurrentUserArticle ||
                        auth.getAuthorities().stream().anyMatch(role ->
                                role.getAuthority().equals("ROLE_MODERATOR")));
    }
}
