package world.evgereo.articles.controllers;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import world.evgereo.articles.models.Articles;
import world.evgereo.articles.services.ArticlesService;

@Controller
@RequestMapping("/articles")
public class ArticlesController {
    private final ArticlesService articlesService;

    public ArticlesController(ArticlesService articlesService) {
        this.articlesService = articlesService;
    }

    @GetMapping()
    public String articles(Model model){
        model.addAttribute("articles", articlesService.getArticles());
        return "/articles/articles";
    }

    @GetMapping("/{id}")
    public String article(Model model, @PathVariable("id") int id) {
        Articles article = articlesService.getArticle(id);
        if (article != null) {
            model.addAttribute("article", article);
            return "articles/article";
        } else {
            return "notfound";
        }
    }


    @GetMapping("{id}/edit")
    public String editArticle(Model model, @PathVariable("id") int id) {
        Articles article = articlesService.getArticle(id);
        if (article != null) {
            model.addAttribute("article", article);
            return "/articles/edit";
        } else {
            return "notfound";
        }
    }

    // there may be errors
    @PatchMapping("/{id}/edit")
    public String updateUser(@ModelAttribute("article") Articles article, @PathVariable("id") int id){
        System.out.println(article);
        articlesService.updateArticle(article, id);
        return "redirect:/articles/{id}";
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable("id") int id){
        articlesService.deleteArticle(id);
        return "redirect:/users";
    }

    @GetMapping("/new")
    public String newUser(@ModelAttribute("article") Articles article) {
        return "articles/new";
    }

    @PostMapping("/new")
    public String createArticle(@ModelAttribute("article") @Valid Articles article, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "articles/new";
        }
        articlesService.createArticle(article);
        return "redirect:/articles";
    }
}
