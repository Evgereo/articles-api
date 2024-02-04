package world.evgereo.articles.services;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import world.evgereo.articles.DTO.AuthRequestDTO;
import world.evgereo.articles.DTO.AuthResponseDTO;
import world.evgereo.articles.DTO.RefreshRequestDTO;
import world.evgereo.articles.errors.exceptions.AuthException;
import world.evgereo.articles.models.User;
import world.evgereo.articles.utils.JwtTokenUtils;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService usersService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtils jwtTokenUtils;
    private final Map<String, String> map = new HashMap<>();

    public AuthResponseDTO createAuthTokens(AuthRequestDTO authRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("You have entered an incorrect email or password");
        }
        User user = usersService.loadUserByEmail(authRequest.getEmail());
        String accessToken = jwtTokenUtils.generateAccessToken(user);
        String refreshToken = jwtTokenUtils.generateRefreshToken(user);
        map.put(user.getEmail(), refreshToken);
        return new AuthResponseDTO(accessToken, refreshToken);
    }

    public AuthResponseDTO updateAuthTokens(RefreshRequestDTO refreshRequest) {
        String email;
        try {
            email = jwtTokenUtils.getRefreshEmail(refreshRequest.getRefreshToken());
        } catch (ExpiredJwtException ex) {
            throw new AuthException("The refresh token has expired");
        } catch (SignatureException ex) {
            throw new AuthException("The signature of the provided token is incorrect");
        }
        String saveRefreshToken = map.get(email);
        if(saveRefreshToken != null && saveRefreshToken.equals(refreshRequest.getRefreshToken())) {
            User user = usersService.getUserByEmail(email);
            String accessToken = jwtTokenUtils.generateAccessToken(user);
            String refreshToken = jwtTokenUtils.generateRefreshToken(user);
            map.put(user.getEmail(), refreshToken);
            return new AuthResponseDTO(accessToken, refreshToken);
        }
        throw new AuthException("Provided token is incorrect");
    }
}