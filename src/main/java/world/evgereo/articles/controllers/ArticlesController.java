package world.evgereo.articles.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import world.evgereo.articles.DAO.ArticlesDAO;
import world.evgereo.articles.DAO.UsersDAO;
import world.evgereo.articles.models.Articles;
import world.evgereo.articles.models.Users;

@Controller
@RequestMapping("/articles")
public class ArticlesController {
    private final UsersDAO usersDAO;
    private final ArticlesDAO articlesDAO;

    public ArticlesController(UsersDAO usersDAO,ArticlesDAO articlesDAO) {
        this.usersDAO = usersDAO;
        this.articlesDAO = articlesDAO;
    }

    @GetMapping()
    public String articles(Model model){
        model.addAttribute("articles", articlesDAO.getArticles());
        return "/articles/articles";
    }

    @GetMapping("/{id}")
    public String article(Model model, @PathVariable("id") int id) {
        model.addAttribute("article", articlesDAO.getArticle(id));
        model.addAttribute("user", usersDAO.getUser(articlesDAO.getArticle(id).getAuthor_id()));
        return "articles/article";
    }


    @GetMapping("{id}/edit")
    public String editArticle(Model model, @PathVariable("id") int id) {
        model.addAttribute("article", articlesDAO.getArticle(id));
        model.addAttribute("user", usersDAO.getUser(articlesDAO.getArticle(id).getAuthor_id()));
        return "/articles/edit";
    }

    @PatchMapping("/{id}/edit")
    public String updateUser(@ModelAttribute("article") Articles article, @PathVariable("id") int id){
        articlesDAO.patchArticle(article, id);
        return "redirect:/articles";
    }
}
