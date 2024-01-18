package world.evgereo.articles.controllers;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import world.evgereo.articles.models.Users;
import world.evgereo.articles.services.UsersService;

@Controller
public class RegistrationController {
    private final UsersService usersService;

    public RegistrationController(UsersService usersService) {
        this.usersService = usersService;
    }

    @GetMapping("/registration")
    public String newUser(@ModelAttribute("user") Users user) {
        return "registration";
    }

    @PostMapping("/registration")
    public String createUser(@ModelAttribute("user") @Valid Users user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "registration";
        }
        if(usersService.createUser(user)) return "redirect:/users";
        bindingResult.rejectValue("email", "error.user", "An account already exists for this email.");
        return "registration";
    }
}
