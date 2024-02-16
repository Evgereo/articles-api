package world.evgereo.articles.services;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import world.evgereo.articles.DTOs.RegistrationUserDTO;
import world.evgereo.articles.DTOs.UpdateUserDTO;
import world.evgereo.articles.errors.exceptions.DuplicateUserException;
import world.evgereo.articles.errors.exceptions.NotFoundException;
import world.evgereo.articles.errors.exceptions.PasswordMismatchException;
import world.evgereo.articles.models.Role;
import world.evgereo.articles.models.User;
import world.evgereo.articles.repositories.UserRepository;

import java.util.Collections;
import java.util.List;


@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final JwtTokenService jwtTokenService;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper mapper;

    public List<User> getUsers() {
        return userRepository.findAll();
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
        return id != 0 ? userRepository.findById(id).orElse(null) : null;
    }

    private User getUserByEmail(String email) {
        return !email.isEmpty() ? userRepository.findUserByEmail(email).orElse(null) : null;
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
        userRepository.save(user);
        return user;
    }

    public User updateUser(UpdateUserDTO updateUser, int id) {
        User user = loadUserById(id);
        mapper.map(updateUser, user);
        userRepository.save(user);
        return user;
    }

    public void deleteUser(int id) {
        jwtTokenService.deleteToken(loadUserById(id).getEmail());
        userRepository.deleteById(id);
    }
}