package world.evgereo.articles.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import world.evgereo.articles.models.Articles;
import world.evgereo.articles.repositories.ArticlesRepository;
import world.evgereo.articles.repositories.UsersRepository;

import java.util.Optional;

@Controller
@RequestMapping("/articles")
public class ArticlesController {
    private final ArticlesRepository articlesRepository;
    private final UsersRepository usersRepository;

    public ArticlesController(ArticlesRepository articlesRepository, UsersRepository usersRepository) {
        this.articlesRepository = articlesRepository;
        this.usersRepository = usersRepository;
    }

    @GetMapping()
    public String articles(Model model){
        model.addAttribute("articles", articlesRepository.findAll());
        return "/articles/articles";
    }

    @GetMapping("/{id}")
    public String article(Model model, @PathVariable("id") int id) {
        Optional<Articles> article = articlesRepository.findById(id);
        if (article.isPresent()) {
            model.addAttribute("article", article.get());
            model.addAttribute("user", usersRepository.findById(article.get().getAuthorId()).get());
            return "articles/article";
        } else {
            return "notfound";
        }
    }


    @GetMapping("{id}/edit")
    public String editArticle(Model model, @PathVariable("id") int id) {
        Optional<Articles> article = articlesRepository.findById(id);
        if (article.isPresent()) {
            model.addAttribute("article", article.get());
            model.addAttribute("user", usersRepository.findById(article.get().getAuthorId()).get());
            return "/articles/edit";
        } else {
            return "notfound";
        }
    }

    // there may be errors
    @PatchMapping("/{id}/edit")
    public String updateUser(@ModelAttribute("article") Articles article, @PathVariable("id") int id){
        articlesRepository.save(article);
        return "redirect:/articles";
    }
}
