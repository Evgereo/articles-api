package world.evgereo.articles.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import world.evgereo.articles.models.Roles;

@Repository
public interface RolesRepository extends JpaRepository<Roles, Integer> {}
