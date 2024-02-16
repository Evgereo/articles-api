package world.evgereo.articles.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import world.evgereo.articles.models.RefreshToken;
import world.evgereo.articles.repositories.RefreshJwtRepository;

@Service
@RequiredArgsConstructor
public class JwtTokenService {
    private final RefreshJwtRepository refreshJwtRepository;

    public void setToken(String email, String refreshToken) {
        refreshJwtRepository.save(new RefreshToken(email, refreshToken));
    }

    public String getTokenByEmail(String email) {
        RefreshToken tokenEntity = refreshJwtRepository.findById(email).orElse(null);
        return tokenEntity != null ? tokenEntity.getRefreshToken() : null;
    }

    public void deleteToken(String email) {
        refreshJwtRepository.deleteById(email);
    }
}
