package world.evgereo.articles.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import world.evgereo.articles.security.auth.handlers.AuthFailureHandler;
import world.evgereo.articles.security.UsersAuthorizationManager;
import world.evgereo.articles.security.auth.handlers.AuthSuccessHandler;
import world.evgereo.articles.services.UsersService;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {
    private final UsersService usersService;
    private final UsersAuthorizationManager usersAuthorizationManager;
    //private final ArticlesAuthorizationManager articlesAuthorizationManager;
    private final AuthFailureHandler authFailureHandler;
    private final AuthSuccessHandler authSuccessHandler;

    public SecurityConfig(UsersService usersService, UsersAuthorizationManager usersAuthorizationManager, AuthFailureHandler authFailureHandler, AuthSuccessHandler authSuccessHandler) {
        this.usersService = usersService;
        this.usersAuthorizationManager = usersAuthorizationManager;
        this.authFailureHandler = authFailureHandler;
        this.authSuccessHandler = authSuccessHandler;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/", "/login", "/articles", "/registration").permitAll()
                        .requestMatchers(HttpMethod.POST, "/login").permitAll()
                        .requestMatchers("/users/{id}/edit").access(usersAuthorizationManager)
                        //.requestMatchers("/articles/{id}/edit").access(articlesSecurity)
                        .anyRequest().authenticated())
                .userDetailsService(usersService)
                .formLogin(form -> form
                        .failureHandler(authFailureHandler)
                        .successHandler(authSuccessHandler)
                        .usernameParameter("email")
                        .loginPage("/login"))
                .logout(logout -> logout
                        .logoutUrl("/users/{id}").logoutSuccessUrl("/")
                        .addLogoutHandler(new SecurityContextLogoutHandler()).deleteCookies("JSESSIONID"));

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