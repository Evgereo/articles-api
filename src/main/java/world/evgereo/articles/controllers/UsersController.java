package world.evgereo.articles.controllers;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import world.evgereo.articles.models.Users;
import world.evgereo.articles.repositories.ArticlesRepository;
import world.evgereo.articles.repositories.UsersRepository;

import java.util.Optional;

@Controller
@RequestMapping("/users")
public class UsersController {
    private final UsersRepository usersRepository;
    private final ArticlesRepository articlesRepository;

    public UsersController(UsersRepository usersRepository, ArticlesRepository articlesRepository) {
        this.usersRepository = usersRepository;
        this.articlesRepository = articlesRepository;
    }

    @GetMapping()
    public String users(Model model){
        model.addAttribute("users", usersRepository.findAll());
        return "users/users";
    }

    @GetMapping("/{id}")
    public String user(Model model, @PathVariable("id") int id) {
        Optional<Users> user = usersRepository.findById(id);
        if (user.isPresent()) {
            model.addAttribute("user", user.get());
            model.addAttribute("articles", articlesRepository.findByAuthorId(id));
            return "users/profile";
        } else {
            return "notfound";
        }
    }

    @GetMapping("/{id}/edit")
    public String editUser(Model model, @PathVariable("id") int id) {
        Optional<Users> user = usersRepository.findById(id);
        if (user.isPresent()) {
            model.addAttribute("user", user.get());
        } else {
            return "notfound";
        }
        System.out.println(usersRepository.findById(id));
        return "users/edit";
    }

    // there may be errors
    @PatchMapping("/*/edit")
    public String updateUser(@Valid @ModelAttribute("user") Users user, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            return "users/edit";
        }
        usersRepository.save(user);
        return "redirect:/users/" + user.getUserId();
    }

    // must will be changed
    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable("id") int id){
        usersRepository.deleteById(id);
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
        usersRepository.save(user);
        return "redirect:/users";
    }
}