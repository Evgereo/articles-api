package world.evgereo.articles.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import world.evgereo.articles.models.Articles;

import java.util.List;

@Repository
public interface ArticlesRepository extends CrudRepository<Articles, Integer> {
    List<Articles> findByAuthorId(int id);


}
