package world.evgereo.articles.configs;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import world.evgereo.articles.security.ArticleAuthorizationManager;
import world.evgereo.articles.security.AuthEntryPoint;
import world.evgereo.articles.security.UserAuthorizationManager;
import world.evgereo.articles.services.UserService;
import world.evgereo.articles.utils.JwtFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {
    private final UserService userService;
    private final UserAuthorizationManager userAuthorizationManager;
    private final ArticleAuthorizationManager articleAuthorizationManager;
    private final JwtFilter jwtFilter;
    private final RequestMappingHandlerMapping requestMappingHandlerMapping;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/", "/auth/**", "/articles", "/registration").permitAll()
                        .requestMatchers(HttpMethod.PATCH,"/users/{id}").access(userAuthorizationManager)
                        .requestMatchers(HttpMethod.DELETE,"/users/{id}").access(userAuthorizationManager)
                        .requestMatchers(HttpMethod.PATCH,"/articles/{id}").access(articleAuthorizationManager)
                        .requestMatchers(HttpMethod.DELETE,"/articles/{id}").access(articleAuthorizationManager)
                        .requestMatchers("/users/**", "/articles/**").authenticated())
                .userDetailsService(userService)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exception -> exception
                        .accessDeniedHandler((request, response, accessDeniedException) -> response.setStatus(HttpStatus.FORBIDDEN.value()))
                        .authenticationEntryPoint(new AuthEntryPoint(requestMappingHandlerMapping)))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        http.csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}