package world.evgereo.articles.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import world.evgereo.articles.DTOs.UpdateUserDTO;
import world.evgereo.articles.models.User;
import world.evgereo.articles.services.UserService;
import world.evgereo.articles.utils.UriPageBuilder;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "http://localhost:8080")
public class UserController {
    private final UserService userService;

    @GetMapping()
    public RedirectView getUsers() {
        return new RedirectView("/users?page&size", true);
    }

    @GetMapping(params = {"page", "size"})
    public ResponseEntity<List<User>> getPaginatedUsers(@RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "10") int size) {
        Page<User> paginatedUsers = userService.getPaginatedUsers(page, size);
        if (paginatedUsers.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(paginatedUsers.getContent(), new UriPageBuilder("/users", paginatedUsers).getAllPagesUri(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable("id") int id) {
        return new ResponseEntity<>(userService.loadUserById(id), HttpStatus.OK);
    }

    @PatchMapping(value = "/{id}", params = "edit", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> editUser(@RequestBody @Valid UpdateUserDTO user, @PathVariable("id") int id) {
        return new ResponseEntity<>(userService.updateUser(user, id), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}", params = "delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable("id") int id) {
        userService.deleteUser(id);
    }
}
