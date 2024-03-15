package world.evgereo.articles.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@RedisHash
public class RefreshToken {
    @Id
    private String email;
    private String refreshToken;
    @TimeToLive
    private long expirationSeconds;
}
