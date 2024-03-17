package world.evgereo.articles.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import world.evgereo.articles.models.User;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class JwtTokenUtils {
    @Value("${token.signing.key.access}")
    private String accessKey;

    @Value("${token.signing.key.refresh}")
    private String refreshKey;

    @Value("${token.signing.time.access}")
    private long accessTime;

    @Value("${token.signing.time.refresh}")
    private long refreshTime;

    private static SecretKey getSecretKey(String key) {
        byte[] keyBytes = Decoders.BASE64.decode(key);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private static String generateToken(Map<String, Object> extraClaims, User user, Long time, String key) {
        return Jwts.builder()
                .claims(extraClaims)
                .subject(user.getEmail())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + time))
                .signWith(JwtTokenUtils.getSecretKey(key)).compact();
    }

    private static Claims getAllClaims(String token, String key) {
        return Jwts.parser()
                .verifyWith(JwtTokenUtils.getSecretKey(key))
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String generateAccessToken(User user) {
        return JwtTokenUtils.generateToken(Map.of(
                        "username", user.getUsername(),
                        "roles", user.getAuthorities()
                                .stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList())),
                user, accessTime, accessKey);
    }

    public String generateRefreshToken(User user) {
        return JwtTokenUtils.generateToken(Map.of("username", user.getUsername()), user, refreshTime, refreshKey);
    }

    public String getAccessEmail(String token) {
        return JwtTokenUtils.getAllClaims(token, accessKey).getSubject();
    }

    public String getRefreshEmail(String token) {
        return JwtTokenUtils.getAllClaims(token, refreshKey).getSubject();
    }

    @SuppressWarnings("unchecked")
    public List<String> getRoles(String token) {
        return (List<String>) JwtTokenUtils.getAllClaims(token, accessKey).get("roles", List.class);
    }
}
