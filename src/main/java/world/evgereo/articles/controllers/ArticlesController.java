package world.evgereo.articles.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import world.evgereo.articles.DAO.ArticlesDAO;

@Controller
@RequestMapping("/articles")
public class ArticlesController {
    private final ArticlesDAO articlesDAO;

    public ArticlesController(ArticlesDAO articlesDAO) {
        this.articlesDAO = articlesDAO;
    }

    @GetMapping()
    public String articles(Model model){
        model.addAttribute("articles", articlesDAO.getArticles());
        return "/articles/articles";
    }
}
