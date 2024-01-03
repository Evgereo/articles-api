package world.evgereo.articles.DAO;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import world.evgereo.articles.models.Articles;

import java.util.List;

@Component
public class ArticlesDAO {
    private final JdbcTemplate jdbcTemplate;
    // next line can be deleted, spring injects the jdbcTemplate by itself
    public ArticlesDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Articles> getArticles() {
        return jdbcTemplate.query("SELECT * FROM articles ",
                new BeanPropertyRowMapper<>(Articles.class));
    }

    public List<Articles> getUserArticles(int id){
        return jdbcTemplate.query("SELECT * FROM articles WHERE author_id = ? ORDER BY article_id", new BeanPropertyRowMapper<>(Articles.class), id);
    }

    public Articles getArticle(int id) {
        return jdbcTemplate.queryForObject("SELECT * FROM articles WHERE article_id = ?",
                new BeanPropertyRowMapper<>(Articles.class), id);
    }

    public void patchArticle(Articles article, int id) {
        jdbcTemplate.update("UPDATE articles SET article_name = ?, article_text = ? WHERE article_id = ?",
                article.getArticleName(), article.getArticleText(), id);
    }
}