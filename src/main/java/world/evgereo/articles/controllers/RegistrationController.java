package world.evgereo.articles.controllers;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import world.evgereo.articles.models.Users;
import world.evgereo.articles.services.UsersService;

@RestController
public class RegistrationController {
    private final UsersService usersService;

    public RegistrationController(UsersService usersService) {
        this.usersService = usersService;
    }

    @GetMapping("/registration")
    public ResponseEntity<Users> newUser() {
        return new ResponseEntity<>(new Users(), HttpStatus.OK);
    }

    @PostMapping(value = "/registration", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Users> createUser(@RequestBody @Valid Users user) {
        return (usersService.createUser(user)) ?
                new ResponseEntity<>(usersService.getUserByEmail(user.getEmail()), HttpStatus.CREATED) :
                null;
    }
}

// {
//         "userName": "some",
//         "userSurname": "user",
//         "age": 12,
//         "email": "3@gmail.com",
//         "password": "root",
//         "passwordConfirm": "root"
//         }