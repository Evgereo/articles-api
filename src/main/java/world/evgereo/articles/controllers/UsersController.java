package world.evgereo.articles.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import world.evgereo.articles.DAO.ArticlesDAO;
import world.evgereo.articles.DAO.UsersDAO;
import world.evgereo.articles.models.Users;

@Controller
@RequestMapping("/users")
public class UsersController {
    private final UsersDAO usersDAO;
    private final ArticlesDAO articlesDAO;

    public UsersController(UsersDAO userDAO, ArticlesDAO articlesDAO) {
        this.usersDAO = userDAO;
        this.articlesDAO = articlesDAO;
    }

    @GetMapping()
    public String users(Model model){
        model.addAttribute("users", usersDAO.getUsers());
        return "users/users";
    }

    @GetMapping("/{id}")
    public String user(Model model, @PathVariable("id") int id) {
        model.addAttribute("user", usersDAO.getUser(id));
        model.addAttribute("articles", articlesDAO.getUserArticles(id));
        return "users/profile";
    }

    @GetMapping("/{id}/edit")
    public String editUser(Model model, @PathVariable("id") int id) {
        model.addAttribute("user", usersDAO.getUser(id));
        return "users/edit";
    }

    @PatchMapping("/{id}/edit")
    public String updateUser(@ModelAttribute("user") Users user, @PathVariable("id") int id){
        usersDAO.patchUser(user, id);
        return "redirect:/users/" + id;
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable("id") int id){
        usersDAO.deleteUser(id);
        return "redirect:/users";
    }

    @GetMapping("/new")
    public String newUser(@ModelAttribute("user") Users user) {
        return "users/new";
    }

    @PostMapping("/new")
    public String createUser(@ModelAttribute("user") Users user) {
        usersDAO.postUser(user);
        return "redirect:/users";
    }
}