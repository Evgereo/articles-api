package world.evgereo.articles.services;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import world.evgereo.articles.DTO.RegistrationUserDTO;
import world.evgereo.articles.DTO.UpdateUserDTO;
import world.evgereo.articles.errors.exceptions.DuplicateUserException;
import world.evgereo.articles.errors.exceptions.NotFoundException;
import world.evgereo.articles.errors.exceptions.PasswordMismatchException;
import world.evgereo.articles.models.Role;
import world.evgereo.articles.models.User;
import world.evgereo.articles.repositories.UserRepository;

import java.util.Collections;
import java.util.List;


@Service
public class UserService implements UserDetailsService {
    private final UserRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper mapper;

    public UserService(UserRepository usersRepository, @Lazy PasswordEncoder passwordEncoder, ModelMapper mapper) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
        this.mapper = mapper;
    }

    public List<User> getUsers() {
        return usersRepository.findAll();
    }

    public User loadUserById(int id) {
        User user = this.getUserById(id);
        if (user != null) {
            return user;
        } else {
            throw new NotFoundException("User with id " + id + " not found");
        }
    }

    public User loadUserByEmail(String email) {
        User user = this.getUserByEmail(email);
        if (user != null) {
            return user;
        } else {
            throw new UsernameNotFoundException("User with " + email + " not found");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        return loadUserByEmail(email);
    }

    private User getUserById(int id) {
        return id != 0 ? usersRepository.findById(id).orElse(null) : null;
    }

    private User getUserByEmail(String email) {
        return !email.isEmpty() ? usersRepository.findUsersByEmail(email).orElse(null) : null;
    }

    public User createUser(RegistrationUserDTO dto) {
        if(this.getUserByEmail(dto.getEmail()) != null) {
            throw new DuplicateUserException("A user with the email " + dto.getEmail() + " already exists");
        }
        if(!dto.getPassword().equals(dto.getPasswordConfirm())) {
            throw new PasswordMismatchException("Entered passwords don't match");
        }
        User user = new User();
        mapper.map(dto, user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Collections.singleton(new Role(1, "ROLE_USER")));
        usersRepository.save(user);
        return user;
    }

    public User updateUser(UpdateUserDTO updateUser, int id) {
        User user = loadUserById(id);
        mapper.map(updateUser, user);
        usersRepository.save(user);
        return user;
    }
    // delete refresh token
    public void deleteUser(int id) {
        usersRepository.deleteById(id);
    }
}