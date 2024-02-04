package world.evgereo.articles.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import world.evgereo.articles.DTO.UpdateUserDTO;
import world.evgereo.articles.models.User;
import world.evgereo.articles.services.UserService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "http://localhost:8080")
public class UserController {
    private final UserService usersService;

    @GetMapping()
    public ResponseEntity<List<User>> users(){
        return new ResponseEntity<>(usersService.getUsers(), HttpStatus.OK);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<User> user(@PathVariable("id") int id) {
        User user = usersService.getUserById(id);
        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping(value = "/{id}", params = "edit", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> updateUser(@RequestBody @Valid UpdateUserDTO user, @PathVariable("id") int id) {
        return new ResponseEntity<>(usersService.updateUser(user, id), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}", params = "delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable("id") int id){
        usersService.deleteUser(id);
    }
}