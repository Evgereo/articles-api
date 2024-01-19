package world.evgereo.articles.controllers;

import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import world.evgereo.articles.models.Users;
import world.evgereo.articles.services.UsersService;

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
        Users user = usersService.getUserById(id);
        if (user != null) {
            model.addAttribute("user", user);
            return "users/profile";
        } else {
            return "notfound";
        }
    }

    @GetMapping("/{id}/edit")
    public String editUser(Model model, @PathVariable("id") int id) {
        Users user = usersService.getUserById(id);
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
}