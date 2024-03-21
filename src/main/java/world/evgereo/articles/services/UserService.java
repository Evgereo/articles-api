package world.evgereo.articles.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import world.evgereo.articles.dtos.PasswordUserDto;
import world.evgereo.articles.dtos.RegistrationUserDto;
import world.evgereo.articles.dtos.UpdateUserDto;
import world.evgereo.articles.errors.exceptions.BadRequestException;
import world.evgereo.articles.errors.exceptions.DuplicateUserException;
import world.evgereo.articles.errors.exceptions.NotFoundException;
import world.evgereo.articles.errors.exceptions.PasswordMismatchException;
import world.evgereo.articles.models.Role;
import world.evgereo.articles.models.User;
import world.evgereo.articles.repositories.UserRepository;
import world.evgereo.articles.utils.MapperUtils;

import java.util.Collections;
import java.util.List;


@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final JwtTokenService jwtTokenService;
    private final PasswordEncoder passwordEncoder;
    private final MapperUtils mapper;

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public Page<User> getPaginatedUsers(int page, int size) {
        return userRepository.findAll(PageRequest.of(page, size));
    }

    public User loadUserById(int id) {
        User user = getUserById(id);
        if (user != null) return user;
        else throw new NotFoundException("User with id " + id + " not found");
    }

    private User getUserById(int id) {
        return id != 0 ? userRepository.findById(id).orElse(null) : null;
    }

    public User loadUserByEmail(String email) {
        User user = getUserByEmail(email);
        if (user != null) return user;
        else throw new UsernameNotFoundException("User with " + email + " not found");
    }

    private User getUserByEmail(String email) {
        return !email.isEmpty() ? userRepository.findUserByEmail(email).orElse(null) : null;
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        return loadUserByEmail(email);
    }

    public User createUser(RegistrationUserDto dto) {
        if (getUserByEmail(dto.getEmail()) != null)
            throw new DuplicateUserException("A user with the email " + dto.getEmail() + " already exists");
        if (!dto.getPassword().equals(dto.getPasswordConfirm()))
            throw new PasswordMismatchException("Entered passwords don't match");
        User user = new User();
        mapper.map(dto, user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Collections.singleton(new Role(1, "ROLE_USER")));
        return userRepository.save(user);
    }

    @Transactional
    public User updateUser(UpdateUserDto dto, int id) {
        User user = getUserById(id);
        if (user != null) {
            mapper.map(dto, user);
            return userRepository.save(user);
        } else throw new BadRequestException("User with id " + id + "to update has been not found");
    }

    @Transactional
    public User updatePassword(PasswordUserDto dto, int id) {
        User user = getUserById(id);
        if (user != null) {
            if (!dto.getPassword().equals(dto.getPasswordConfirm()))
                throw new PasswordMismatchException("Entered passwords don't match");
            mapper.map(dto, user);
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
            return userRepository.save(user);
        } else throw new BadRequestException("User with id " + id + "to update has been not found");
    }

    @Transactional
    public void deleteUser(int id) {
        jwtTokenService.deleteToken(loadUserById(id).getEmail());
        userRepository.updateArticlesUserToNull(id);
        userRepository.deleteById(id);
    }
}
