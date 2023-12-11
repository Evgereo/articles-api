package world.evgereo.articles.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import world.evgereo.articles.DAO.ArticlesDAO;
import world.evgereo.articles.DAO.UsersDAO;

@Controller
public class MainController {

    @GetMapping()
    public String welcomePage(Model model) {
        return "main/home";
    }
}
