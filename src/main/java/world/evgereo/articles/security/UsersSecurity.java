package world.evgereo.articles.security;

import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;
import world.evgereo.articles.models.Users;

import java.util.function.Supplier;

@Component
public class UsersSecurity implements AuthorizationManager<RequestAuthorizationContext> {

@Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext context) {
        int userId = Integer.parseInt(context.getVariables().get("id"));
        Authentication auth = authentication.get();
        return new AuthorizationDecision(isCurrentUserPage(auth, userId) ||
                auth.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_MODERATOR")));
    }

    public boolean isCurrentUserPage(Authentication authentication, int userId) {
        Users currentUser = (Users) authentication.getPrincipal();
        return currentUser.getUserId() == userId;
    }
}