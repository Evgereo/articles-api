package world.evgereo.articles.services;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
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
import world.evgereo.articles.models.RefreshToken;
import world.evgereo.articles.models.User;
import world.evgereo.articles.repositories.RefreshJwtRepository;
import world.evgereo.articles.utils.JwtTokenUtils;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService usersService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtils jwtTokenUtils;
    private final RefreshJwtRepository refreshJwtRepository;

    public AuthResponseDTO createAuthTokens(AuthRequestDTO authRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Incorrect email or password has been entered");
        }
        User user = usersService.loadUserByEmail(authRequest.getEmail());
        String accessToken = jwtTokenUtils.generateAccessToken(user);
        String refreshToken = jwtTokenUtils.generateRefreshToken(user);
        setToken(user.getEmail(), refreshToken);
        return new AuthResponseDTO(accessToken, refreshToken);
    }

    public AuthResponseDTO updateAuthTokens(RefreshRequestDTO refreshRequest) {
        String email;
        try {
            email = jwtTokenUtils.getRefreshEmail(refreshRequest.getRefreshToken());
        } catch (ExpiredJwtException | SignatureException| MalformedJwtException ex) {
            throw new AuthException(ex.getMessage());
        }
        String saveRefreshToken = getTokenByEmail(email);
        if(saveRefreshToken != null && saveRefreshToken.equals(refreshRequest.getRefreshToken())) {
            User user = usersService.loadUserByEmail(email);
            String accessToken = jwtTokenUtils.generateAccessToken(user);
            String refreshToken = jwtTokenUtils.generateRefreshToken(user);
            setToken(user.getEmail(), refreshToken);
            return new AuthResponseDTO(accessToken, refreshToken);
        } else if(saveRefreshToken != null) {
            deleteToken(email);
            throw new AuthException("The token is authentic, but a new one was received");
        }
        throw new AuthException("Provided token is incorrect");
    }

    private void setToken(String email, String refreshToken) {
        refreshJwtRepository.save(new RefreshToken(email, refreshToken));
    }

    private String getTokenByEmail(String email) {
        RefreshToken tokenEntity = refreshJwtRepository.findById(email).orElse(null);
        return tokenEntity != null ? tokenEntity.getRefreshToken() : null;
    }

    private void deleteToken(String email) {
        refreshJwtRepository.deleteById(email);
    }
}