package world.evgereo.articles.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import world.evgereo.articles.models.Users;

@Repository
public interface UsersRepository extends JpaRepository<Users, Integer> {

}
