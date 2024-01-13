package world.evgereo.articles.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import world.evgereo.articles.models.Articles;

@Repository
public interface ArticlesRepository extends JpaRepository<Articles, Integer> {}
