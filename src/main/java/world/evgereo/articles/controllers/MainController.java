package world.evgereo.articles.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import world.evgereo.articles.DAO.ArticlesDAO;
import world.evgereo.articles.DAO.UsersDAO;

@Controller
public class MainController {
//    private final UsersDAO usersDAO;
//    private final ArticlesDAO articlesDAO;
//
//    public MainController(UsersDAO usersDAO, ArticlesDAO articlesDAO) {
//        this.usersDAO = usersDAO;
//        this.articlesDAO = articlesDAO;
//    }

    @GetMapping()
    public String welcomePage(Model model) {
        return "main/welcome";
    }
}
