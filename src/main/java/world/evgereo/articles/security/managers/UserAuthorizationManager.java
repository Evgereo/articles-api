package world.evgereo.articles.security.managers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;
import world.evgereo.articles.services.UserService;

import java.util.function.Supplier;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {
    private final UserService userService;

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext context) {
        Authentication auth = authentication.get();
        boolean isCurrentUserPage = userService.loadUserByEmail((String) auth.getPrincipal()).getUserId()
                == Integer.parseInt(context.getVariables().get("id"));
        UserAuthorizationManager.log.debug("Is user page of current user page: {}", isCurrentUserPage);
        return new AuthorizationDecision(
                isCurrentUserPage ||
                        auth.getAuthorities().stream().anyMatch(role ->
                                role.getAuthority().equals("ROLE_MODER")));
    }
}
