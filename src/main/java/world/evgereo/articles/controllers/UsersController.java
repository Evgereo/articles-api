package world.evgereo.articles.controllers;

import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import world.evgereo.articles.models.Users;
import world.evgereo.articles.services.UsersService;

import java.util.List;

@RestController
@RequestMapping(path = "/users", produces = "application/json")
@CrossOrigin(origins = "http://localhost:8080")
public class UsersController {
    private final UsersService usersService;

    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    @GetMapping()
    public ResponseEntity<List<Users>> users(){
        return new ResponseEntity<>(usersService.getUsers(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Users> user(@PathVariable("id") int id) {
        Users user = usersService.getUserById(id);
        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/{id}", params = "edit")
    public ResponseEntity<Users> editUser(@PathVariable("id") int id) {
        Users user = usersService.getUserById(id);
        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping(value = "/{id}", params = "edit")
    public ResponseEntity<Users> updateUser(@RequestBody @Valid Users user, @PathVariable("id") int id){
        usersService.updateUser(user, id);
        return new ResponseEntity<>(usersService.getUserById(id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable("id") int id){
        usersService.deleteUser(id);
    }
}