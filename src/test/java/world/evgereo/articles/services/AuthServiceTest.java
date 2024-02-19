package world.evgereo.articles.services;

import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import world.evgereo.articles.DTOs.RefreshRequestDTO;
import world.evgereo.articles.errors.exceptions.AuthException;
import world.evgereo.articles.models.User;
import world.evgereo.articles.security.utils.JwtTokenUtils;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static world.evgereo.articles.mockfactories.AuthMockFactory.*;
import static world.evgereo.articles.mockfactories.UserMockFactory.getFirstUser;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
    @Mock
    private UserService userService;
    @Mock
    private JwtTokenService jwtTokenService;
    @Mock
    private JwtTokenUtils jwtTokenUtils;
    @Mock
    private AuthenticationManager authenticationManager;
    @InjectMocks
    private AuthService authService;


    @Test
    void createAuthTokens_withCorrectData_getAuthResponse() {
        when(userService.loadUserByEmail(getFirstUser().getEmail())).thenReturn(getFirstUser());
        when(jwtTokenUtils.generateAccessToken(any(User.class))).thenReturn(getAccessToken());
        when(jwtTokenUtils.generateRefreshToken(any(User.class))).thenReturn(getRefreshToken());
        authService.createAuthTokens(getAuthRequestDTO());
        verify(jwtTokenService, times(1)).setToken(any(String.class), eq(getRefreshToken()));
    }

    @Test
    void createAuthTokens_withBadCredentials_throwsException() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new RuntimeException("Test. Bad credentials or any else runtime exception"));
        assertThrows(BadCredentialsException.class, () -> authService.createAuthTokens(getAuthRequestDTO()));
        verify(jwtTokenService, times(0)).setToken(any(String.class), any(String.class));
    }

    @Test
    void updateAuthTokens_withCorrectToken_getAuthResponse() {
        when(jwtTokenUtils.getRefreshEmail(getRefreshToken())).thenReturn(getFirstUser().getEmail());
        when(jwtTokenService.getTokenByEmail(getFirstUser().getEmail())).thenReturn(getRefreshToken());
        when(userService.loadUserByEmail(getFirstUser().getEmail())).thenReturn(getFirstUser());
        when(jwtTokenUtils.generateAccessToken(any(User.class))).thenReturn(getAccessToken());
        when(jwtTokenUtils.generateRefreshToken(any(User.class))).thenReturn(getRefreshToken());
        authService.updateAuthTokens(new RefreshRequestDTO(getRefreshToken()));
        verify(jwtTokenService, times(1)).setToken(any(String.class), eq(getRefreshToken()));
    }

    @Test
    void updateAuthTokens_withIncorrectToken_throwsException() {
        when(jwtTokenUtils.getRefreshEmail(getRefreshToken())).thenThrow(JwtException.class);
        assertThrows(AuthException.class, () -> authService.updateAuthTokens(new RefreshRequestDTO(getRefreshToken())));
        verify(jwtTokenService, times(0)).getTokenByEmail(any(String.class));
    }

    @Test
    void updateAuthTokens_withCorrectNotExistingToken_throwsException() {
        when(jwtTokenUtils.getRefreshEmail(getRefreshToken())).thenReturn(getFirstUser().getEmail());
        when(jwtTokenService.getTokenByEmail(getFirstUser().getEmail())).thenReturn(getRefreshToken() + "incorrect");
        assertThrows(AuthException.class, () -> authService.updateAuthTokens(new RefreshRequestDTO(getRefreshToken())));
        verify(userService, times(0)).loadUserByEmail(any(String.class));
    }
}