package world.evgereo.articles.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.NameValueExpression;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.*;

@RequiredArgsConstructor
@Slf4j
public class AuthEntryPoint implements AuthenticationEntryPoint {
    private final RequestMappingHandlerMapping requestMappingHandlerMapping;
    private List<String> methodsInfo;

    private static boolean existsPattern(HttpServletRequest request, String... patterns) {
        return Arrays.stream(patterns).anyMatch(pattern -> request.getRequestURI().equals(pattern));
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) {
        if (response.getStatus() == HttpStatus.UNAUTHORIZED.value() && isNotEndpointPathExist(request))
            response.setStatus(HttpStatus.NOT_FOUND.value());
        else if (response.getStatus() == HttpStatus.UNAUTHORIZED.value() && isNotMethodExist(request))
            response.setStatus(HttpStatus.METHOD_NOT_ALLOWED.value());
        else response.setStatus(HttpStatus.UNAUTHORIZED.value());
    }

    private boolean isNotEndpointPathExist(HttpServletRequest request) {
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = requestMappingHandlerMapping.getHandlerMethods();
        if (AuthEntryPoint.existsPattern(request, "/actuator", "/actuator/prometheus")) {
            AuthEntryPoint.log.debug("Pattern {} exist", request.getRequestURI());
            methodsInfo = new ArrayList<>(List.of("GET"));
            return false;
        }
        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : handlerMethods.entrySet()) {
            RequestMappingInfo mappingInfo = entry.getKey();
            AuthEntryPoint.log.trace("Exist pattern: {} and request pattern: {}", mappingInfo.getPatternValues(), request.getRequestURI());
            if (new AntPathMatcher().match(mappingInfo.getPatternValues().stream().iterator().next(), request.getRequestURI()) &&
                    new HashSet<>(mappingInfo.getParamsCondition().getExpressions().stream().map(NameValueExpression::getName).toList())
                            .containsAll(Collections.list(request.getParameterNames()))) {
                AuthEntryPoint.log.debug("Pattern {} exist", request.getRequestURI());
                methodsInfo = mappingInfo.getMethodsCondition().getMethods().stream().map(Enum::name).toList();
                return false;
            }
        }
        AuthEntryPoint.log.debug("Pattern {} don't exist", request.getRequestURI());
        return true;
    }

    private boolean isNotMethodExist(HttpServletRequest request) {
        AuthEntryPoint.log.debug("{} patterns methods are allowed", methodsInfo);
        return !methodsInfo.contains(request.getMethod());
    }
}
