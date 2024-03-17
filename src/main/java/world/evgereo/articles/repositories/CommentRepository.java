package world.evgereo.articles.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import world.evgereo.articles.models.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    void deleteByParentId(int id);
}
