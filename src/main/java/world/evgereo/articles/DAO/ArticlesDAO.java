package world.evgereo.articles.DAO;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import world.evgereo.articles.models.Articles;

import java.util.List;

@Component
public class ArticlesDAO {
    private final JdbcTemplate jdbcTemplate;

    public ArticlesDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Articles> getArticles() {
        return jdbcTemplate.query("SELECT * FROM articles ",
                new BeanPropertyRowMapper<>(Articles.class));
    }

    public List<Articles> getUserArticles(int id){
        return jdbcTemplate.query("SELECT * FROM articles WHERE author_id = ?", new BeanPropertyRowMapper<>(Articles.class), id);
    }
}