package world.evgereo.articles.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import world.evgereo.articles.models.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {}
