package world.evgereo.articles.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import world.evgereo.articles.security.UsersSecurity;
import world.evgereo.articles.services.UsersService;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {
    private final UsersService usersService;
    private final UsersSecurity usersSecurity;
    //private final ArticlesSecurity articlesSecurity;

    public SecurityConfig(UsersService usersService, UsersSecurity usersSecurity) {
        this.usersService = usersService;
        this.usersSecurity = usersSecurity;
        //this.articlesSecurity = articlesSecurity;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/", "/login", "/articles", "/registration").permitAll()
                        .requestMatchers("/users/{id}/edit").access(usersSecurity)
                        //.requestMatchers("/articles/{id}/edit").access(articlesSecurity)
                        .anyRequest().authenticated())
                .userDetailsService(usersService)
                .formLogin(form -> form
                        .loginPage("/login"))
                .logout(logout -> logout
                        .logoutUrl("/users/{id}").logoutSuccessUrl("/")
                        .addLogoutHandler(new SecurityContextLogoutHandler()).deleteCookies("JSESSIONID")
                );

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