package world.evgereo.articles.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import world.evgereo.articles.errors.exceptions.BadRequestException;
import world.evgereo.articles.errors.exceptions.DuplicateUserException;
import world.evgereo.articles.errors.exceptions.NotFoundException;
import world.evgereo.articles.errors.exceptions.PasswordMismatchException;
import world.evgereo.articles.models.User;
import world.evgereo.articles.repositories.UserRepository;
import world.evgereo.articles.utils.MapperUtils;

import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static world.evgereo.articles.mockfactories.UserMockFactory.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private JwtTokenService jwtTokenService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private MapperUtils mapper;
    @InjectMocks
    private UserService userService;

    @Test
    void loadUserById_withExistingId_getUser() {
        when(userRepository.findById(1)).thenReturn(Optional.ofNullable(getFirstUser()));
        assertEquals(1, userService.loadUserById(1).getUserId());
        verify(userRepository, times(1)).findById(1);
    }

    @Test
    void loadUserById_withNotExistingId_throwsException() {
        when(userRepository.findById(5)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> userService.loadUserById(5));
        verify(userRepository, times(1)).findById(5);
    }

    @Test
    void loadUserByEmail_withExistingEmail_getUser() {
        when(userRepository.findUserByEmail("testfirst@gmail.com")).thenReturn(Optional.ofNullable(getFirstUser()));
        assertEquals(1, userService.loadUserByEmail("testfirst@gmail.com").getUserId());
        verify(userRepository, times(1)).findUserByEmail("testfirst@gmail.com");
    }

    @Test
    void loadUserByEmail_withNotExistingEmail_throwsException() {
        when(userRepository.findUserByEmail("some@gmail.com")).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByEmail("some@gmail.com"));
        verify(userRepository, times(1)).findUserByEmail("some@gmail.com");
    }

    @Test
    void loadUserByUsername_withExistingEmail_getUser() {
        loadUserByEmail_withExistingEmail_getUser();
    }

    @Test
    void loadUserByUsername_withNotExistingEmail_throwsException() {
        loadUserByEmail_withNotExistingEmail_throwsException();
    }

    @Test
    void createUser_withCorrectData_getUser() {
        when(userRepository.findUserByEmail(any(String.class))).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(getFirstUser());
        assertEquals(getFirstUser(), userService.createUser(getValidRegistrationUserDto()));
        verify(userRepository, times(1)).findUserByEmail(any(String.class));
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void createUser_withDuplicateUser_throwsDuplicateUserException() {
        when(userRepository.findUserByEmail(any(String.class))).thenReturn(Optional.ofNullable(getFirstUser()));
        assertThrows(DuplicateUserException.class, () -> userService.createUser(getValidRegistrationUserDto()));
        verify(userRepository, times(0)).save(any(User.class));
    }

    @Test
    void createUser_withMismatchPassword_throwsPasswordMismatchException() {
        when(userRepository.findUserByEmail(any(String.class))).thenReturn(Optional.empty());
        assertThrows(PasswordMismatchException.class, () -> userService.createUser(getInvalidRegistrationUserDto()));
        verify(userRepository, times(0)).save(any(User.class));
    }

    @Test
    void updateUser_withExistingId_getUser() {
        when(userRepository.findById(1)).thenReturn(Optional.ofNullable(getFirstUser()));
        when(userRepository.save(any(User.class))).thenReturn(getFirstUser());
        assertEquals(getFirstUser(), userService.updateUser(getUpdateUserDto(), 1));
        verify(userRepository, times(1)).findById(1);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void updateUser_withNotExistingId_getUser() {
        when(userRepository.findById(5)).thenReturn(Optional.empty());
        assertThrows(BadRequestException.class, () -> userService.updateUser(getUpdateUserDto(), 5));
        verify(userRepository, times(1)).findById(5);
        verify(userRepository, times(0)).save(any(User.class));
    }

    @Test
    void updatePassword_withCorrectData_getUser() {
        when(userRepository.findById(1)).thenReturn(Optional.ofNullable(getFirstUser()));
        when(userRepository.save(any(User.class))).thenReturn(getFirstUser());
        assertEquals(getFirstUser(), userService.updatePassword(getValidPasswordUserDto(), 1));
        verify(userRepository, times(1)).findById(1);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void updatePassword_withNotExistingId_throwsBadRequestException() {
        when(userRepository.findById(5)).thenReturn(Optional.empty());
        assertThrows(BadRequestException.class, () -> userService.updatePassword(getValidPasswordUserDto(), 5));
        verify(userRepository, times(1)).findById(5);
        verify(userRepository, times(0)).save(any(User.class));
    }

    @Test
    void updatePassword_withMismatchPassword_throwsPasswordMismatchException() {
        when(userRepository.findById(1)).thenReturn(Optional.ofNullable(getFirstUser()));
        assertThrows(PasswordMismatchException.class, () -> userService.updatePassword(getInvalidPasswordUserDto(), 1));
        verify(userRepository, times(1)).findById(1);
        verify(userRepository, times(0)).save(any(User.class));
    }

    @Test
    void deleteUser() {
        when(userRepository.findById(1)).thenReturn(Optional.ofNullable(getFirstUser()));
        userService.deleteUser(1);
        verify(jwtTokenService, times(1)).deleteToken(Objects.requireNonNull(getFirstUser()).getEmail());
        verify(userRepository, times(1)).updateArticlesUserToNull(1);
        verify(userRepository, times(1)).deleteById(1);
    }
}
