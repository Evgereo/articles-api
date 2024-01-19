package world.evgereo.articles.services;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import world.evgereo.articles.models.Roles;
import world.evgereo.articles.models.Users;
import world.evgereo.articles.repositories.UsersRepository;

import java.util.*;

@Service
public class UsersService implements UserDetailsService {
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    public UsersService(UsersRepository usersRepository,@Lazy PasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Users> getUsers() {
        return usersRepository.findAll();
    }

    public Users getUserById(int id) {
        Optional<Users> user = usersRepository.findById(id);
        return user.orElse(null);
    }

    public Users getUserByEmail(String email) {
        Optional<Users> user = usersRepository.findUsersByEmail(email);
        return user.orElse(null);
    }

    @PreAuthorize("hasRole('ROLE_MODERATOR') or authentication.principal.userId == #id")
    public void updateUser(Users user, int id) {
        Optional<Users> optionalUser = usersRepository.findById(id);
        if (optionalUser.isPresent()) {
            Users existingUser = optionalUser.get();
            existingUser.setUserName(user.getUserName());
            existingUser.setUserSurname(user.getUserSurname());
            existingUser.setAge(user.getAge());
            existingUser.setEmail(user.getEmail());
            usersRepository.save(existingUser);
        }
    }

    public boolean createUser(Users user) {
        Users isExitingUser = this.getUserByEmail(user.getEmail());
        if(isExitingUser != null) return false;
        if(!user.getPassword().equals(user.getPasswordConfirm())) return false;
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Collections.singleton(new Roles(1, "ROLE_USER")));
        usersRepository.save(user);
        return true;
    }

    @PreAuthorize("hasRole('ROLE_MODERATOR') or authentication.principal.userId == #id")
    public void deleteUser(int id) {
        usersRepository.deleteById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Users user = this.getUserByEmail(email);
        if (user != null) return user;
        else throw new UsernameNotFoundException("User with " + email + " not found.");
    }
}