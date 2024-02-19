package world.evgereo.articles.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import world.evgereo.articles.DTOs.AuthRequestDTO;
import world.evgereo.articles.DTOs.AuthResponseDTO;
import world.evgereo.articles.DTOs.RefreshRequestDTO;
import world.evgereo.articles.errors.exceptions.AuthException;
import world.evgereo.articles.models.User;
import world.evgereo.articles.security.utils.JwtTokenUtils;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final UserService userService;
    private final JwtTokenService jwtTokenService;
    private final JwtTokenUtils jwtTokenUtils;
    private final AuthenticationManager authenticationManager;

    public AuthResponseDTO createAuthTokens(AuthRequestDTO authRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
        } catch (RuntimeException ex) {
            log.debug(ex.getMessage());
            throw new BadCredentialsException("Incorrect email or password has been entered");
        }
        return generateTokens(authRequest.getEmail());
    }

    public AuthResponseDTO updateAuthTokens(RefreshRequestDTO refreshRequest) {
        String email;
        try {
            email = jwtTokenUtils.getRefreshEmail(refreshRequest.getRefreshToken());
        } catch (RuntimeException ex) {
            throw new AuthException(ex.getMessage());
        }
        String saveRefreshToken = jwtTokenService.getTokenByEmail(email);
        if(saveRefreshToken != null && saveRefreshToken.equals(refreshRequest.getRefreshToken())) {
            return generateTokens(email);
        } else if(saveRefreshToken != null) {
            jwtTokenService.deleteToken(email);
            throw new AuthException("The token is authentic, but a new one was received");
        }
        throw new AuthException("Provided token is incorrect");
    }

    private AuthResponseDTO generateTokens(String email) {
        User user = userService.loadUserByEmail(email);
        String accessToken = jwtTokenUtils.generateAccessToken(user);
        String refreshToken = jwtTokenUtils.generateRefreshToken(user);
        jwtTokenService.setToken(user.getEmail(), refreshToken);
        return new AuthResponseDTO(accessToken, refreshToken);
    }
}