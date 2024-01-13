package world.evgereo.articles.controllers;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import world.evgereo.articles.models.Users;
import world.evgereo.articles.repositories.ArticlesRepository;
import world.evgereo.articles.repositories.UsersRepository;
import world.evgereo.articles.services.UsersService;

import java.util.Optional;

@Controller
@RequestMapping("/users")
public class UsersController {
    private final UsersService usersService;

    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    @GetMapping()
    public String users(Model model){
        model.addAttribute("users", usersService.getUsers());
        return "users/users";
    }

    @GetMapping("/{id}")
    public String user(Model model, @PathVariable("id") int id) {
        Users user = usersService.getUser(id);
        if (user != null) {
            model.addAttribute("user", user);
            return "users/profile";
        } else {
            return "notfound";
        }
    }

    @GetMapping("/{id}/edit")
    public String editUser(Model model, @PathVariable("id") int id) {
        Users user = usersService.getUser(id);
        if (user != null) {
            model.addAttribute("user", user);
        } else {
            return "notfound";
        }
        return "users/edit";
    }

    // there may be errors
    @PatchMapping("/{id}/edit")
    public String updateUser(@Valid @ModelAttribute("user") Users user, BindingResult bindingResult, @PathVariable("id") int id){
        if (bindingResult.hasErrors()) {
            return "users/edit";
        }
        usersService.updateUser(user, id);
        return "redirect:/users/{id}";
    }

    // must will be changed
    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable("id") int id){
        usersService.deleteUser(id);
        return "redirect:/users";
    }

    @GetMapping("/new")
    public String newUser(@ModelAttribute("user") Users user) {
        return "users/new";
    }

    @PostMapping("/new")
    public String createUser(@ModelAttribute("user") @Valid Users user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "users/new";
        }
        usersService.createUser(user);
        return "redirect:/users";
    }
}