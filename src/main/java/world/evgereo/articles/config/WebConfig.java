package world.evgereo.articles.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        //registry.addStatusController("/login", HttpStatus.OK);
        registry.addViewController("/").setViewName("main/home");
//        registry.addViewController("/login").setViewName("login");
    }
}
