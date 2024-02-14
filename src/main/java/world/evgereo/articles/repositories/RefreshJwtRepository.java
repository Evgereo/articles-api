package world.evgereo.articles.repositories;

import org.springframework.data.keyvalue.repository.KeyValueRepository;
import org.springframework.stereotype.Repository;
import world.evgereo.articles.models.RefreshToken;

@Repository
public interface RefreshJwtRepository extends KeyValueRepository<RefreshToken, String> {
}