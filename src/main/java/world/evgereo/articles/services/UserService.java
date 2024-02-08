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
import world.evgereo.articles.errors.exceptions.PasswordMismatchException;
import world.evgereo.articles.models.Role;
import world.evgereo.articles.models.User;
import world.evgereo.articles.repositories.UserRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;


@Service
public class UserService implements UserDetailsService {
    private final UserRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper mapper = new ModelMapper();

    public UserService(UserRepository usersRepository, @Lazy PasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
        mapper.getConfiguration().setSkipNullEnabled(true);
    }

    public List<User> getUsers() {
        return usersRepository.findAll();
    }

    public User getUserById(int id) {
        Optional<User> user = usersRepository.findById(id);
        return user.orElse(null);
    }

    public User getUserByEmail(String email) {
        Optional<User> user = usersRepository.findUsersByEmail(email);
        return user.orElse(null);
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
        User user = getUserById(id);
        if (user != null) {
            mapper.map(updateUser, user);
            usersRepository.save(user);
            return user;
        }
        throw new UsernameNotFoundException("User with id " + id + " not found");
    }

    public void deleteUser(int id) {
        usersRepository.deleteById(id);
    } // delete refresh token

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
        User user = this.getUserByEmail(email);
        if (user != null) {
            return user;
        } else {
            throw new UsernameNotFoundException("User with " + email + " not found");
        }
    }
}