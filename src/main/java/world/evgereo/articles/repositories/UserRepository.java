package world.evgereo.articles.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import world.evgereo.articles.models.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findUserByEmail(String email);

    @Modifying
    @Query("UPDATE Article a SET a.author = NULL WHERE a.author.userId = :id")
    void updateArticlesUserToNull(int id);
}
