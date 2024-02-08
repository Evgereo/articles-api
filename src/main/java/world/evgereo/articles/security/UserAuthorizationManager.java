package world.evgereo.articles.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;
import world.evgereo.articles.models.User;
import world.evgereo.articles.services.UserService;

import java.util.function.Supplier;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {
    private final UserService userService;

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext context) {
        int userId = Integer.parseInt(context.getVariables().get("id"));
        Authentication auth = authentication.get();
        return new AuthorizationDecision(isCurrentUserPage(auth, userId) ||
                auth.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_MODERATOR")));
    }

    public boolean isCurrentUserPage(Authentication authentication, int userId) {
        User currentUser = userService.getUserByEmail((String) authentication.getPrincipal());
        boolean isUserPage = currentUser != null && currentUser.getUserId() == userId;
        log.debug("Is user page of current user page: {}", isUserPage);
        return isUserPage;
    }
}