package world.evgereo.articles.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import world.evgereo.articles.dtos.PasswordUserDto;
import world.evgereo.articles.dtos.UpdateUserDto;
import world.evgereo.articles.models.User;
import world.evgereo.articles.services.UserService;
import world.evgereo.articles.utils.UriPageBuilder;

import java.util.List;

import static world.evgereo.articles.utils.UriPageBuilder.buildPageUri;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "http://localhost:8080")
public class UserController {
    private final UserService userService;

    @GetMapping()
    public ResponseEntity<?> getUsers() {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>(1);
        map.add("Location", buildPageUri(0, 10));
        return new ResponseEntity<>(map, HttpStatus.MOVED_PERMANENTLY);
    }

    @GetMapping(params = {"page", "size"})
    public ResponseEntity<List<User>> getPaginatedUsers(@RequestParam(name = "page", defaultValue = "0", required = false) int page,
                                                        @RequestParam(name = "size", defaultValue = "10", required = false) int size) {
        Page<User> paginatedUsers = userService.getPaginatedUsers(page, size);
        if (paginatedUsers.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(paginatedUsers.getContent(), new UriPageBuilder(paginatedUsers).getAllPagesUri(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable("id") int id) {
        return new ResponseEntity<>(userService.loadUserById(id), HttpStatus.OK);
    }

    @PatchMapping(value = "/{id}", params = "edit", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> editUser(@RequestBody @Valid UpdateUserDto user, @PathVariable("id") int id) {
        return new ResponseEntity<>(userService.updateUser(user, id), HttpStatus.OK);
    }

    @PatchMapping(value = "/{id}", params = "password", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> editPasswordOfUser(@RequestBody @Valid PasswordUserDto user, @PathVariable("id") int id) {
        return new ResponseEntity<>(userService.updatePassword(user, id), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}", params = "delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable("id") int id) {
        userService.deleteUser(id);
    }
}
