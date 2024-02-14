package world.evgereo.articles.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.Collections;
import java.util.Map;

@RequiredArgsConstructor
@Slf4j
public class AuthEntryPoint implements AuthenticationEntryPoint {
    private final RequestMappingHandlerMapping requestMappingHandlerMapping;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) {
        if (response.getStatus() == HttpStatus.UNAUTHORIZED.value() && this.isNotEndpointPathExist(request)) {
            response.setStatus(HttpStatus.NOT_FOUND.value());
        } else {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
        }
    }

    private boolean isNotEndpointPathExist(HttpServletRequest request) {
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = requestMappingHandlerMapping.getHandlerMethods();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : handlerMethods.entrySet()) {
            RequestMappingInfo mappingInfo = entry.getKey();
            log.trace("Exist pattern: {} and request pattern: {}", mappingInfo.getPatternValues(), request.getRequestURI());
            if (mappingInfo.getPatternValues().contains(request.getRequestURI()) &&
                    (!request.getParameterNames().hasMoreElements() ||
                            mappingInfo.getParamsCondition().getExpressions().stream()
                                    .map(String::valueOf)
                                    .anyMatch(paramName -> Collections.list(request.getParameterNames()).stream().anyMatch(paramName::equals)))) {
                log.debug("Pattern {} exist", request.getRequestURI());
                return false;
            }
        }
        log.debug("Pattern {} don't exist", request.getRequestURI());
        return true;
    }
}