package world.evgereo.articles.controllers;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import world.evgereo.articles.DTO.UsersDTO;
import world.evgereo.articles.models.Users;
import world.evgereo.articles.services.UsersService;

import java.util.List;

@RestController
@RequestMapping(path = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
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


    /** same methods (must be deleted) */
    @GetMapping("/{id}")
    public ResponseEntity<Users> user(@PathVariable("id") int id) {
        Users user = usersService.getUserById(id);
        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

//    @GetMapping(value = "/{id}", params = "edit")
//    public ResponseEntity<Users> editUser(@PathVariable("id") int id) {
//        Users user = usersService.getUserById(id);
//        if (user != null) {
//            return new ResponseEntity<>(user, HttpStatus.OK);
//        } else {
//            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
//        }
//    }

    @PatchMapping(value = "/{id}", params = "edit", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Users> updateUser(@RequestBody @Valid UsersDTO user, @PathVariable("id") int id){ // DTO is really stupid solution
        usersService.updateUser(user, id);
        return new ResponseEntity<>(usersService.getUserById(id), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}", params = "delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable("id") int id){
        usersService.deleteUser(id);
    }
}