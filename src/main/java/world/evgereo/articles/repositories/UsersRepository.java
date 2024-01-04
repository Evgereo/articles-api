package world.evgereo.articles.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import world.evgereo.articles.models.Users;

@Repository
public interface UsersRepository extends CrudRepository<Users, Integer> {
}
