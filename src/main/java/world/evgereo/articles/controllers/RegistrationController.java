package world.evgereo.articles.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import world.evgereo.articles.DTO.RegistrationUserDTO;
import world.evgereo.articles.models.User;
import world.evgereo.articles.services.UserService;

@RestController
@RequiredArgsConstructor
public class RegistrationController {
    private final UserService usersService;

    @PostMapping(value = "/registration", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> createUser(@RequestBody @Valid RegistrationUserDTO user) {
        return new ResponseEntity<>(usersService.createUser(user), HttpStatus.CREATED);
    }
}