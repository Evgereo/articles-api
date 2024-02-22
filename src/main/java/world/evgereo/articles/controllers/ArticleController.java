package world.evgereo.articles.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import world.evgereo.articles.DTOs.CreateUpdateArticleDTO;
import world.evgereo.articles.models.Article;
import world.evgereo.articles.services.ArticleService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/articles", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "http://localhost:8080")
public class ArticleController {
    private final ArticleService articleService;

    @GetMapping()
    public ResponseEntity<List<Article>> articles() {
        return new ResponseEntity<>(articleService.getArticles(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Article> article(@PathVariable("id") int id) {
        return new ResponseEntity<>(articleService.loadArticleById(id), HttpStatus.OK);
    }

    @PostMapping("/new")
    public ResponseEntity<Article> createArticle(@RequestBody @Valid CreateUpdateArticleDTO article) {
        return new ResponseEntity<>(articleService.createArticle(article), HttpStatus.CREATED);
    }

    @PatchMapping(value = "{id}", params = "edit", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Article> editArticle(@RequestBody @Valid CreateUpdateArticleDTO article, @PathVariable("id") int id) {
        return new ResponseEntity<>(articleService.updateArticle(article, id), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}", params = "delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable("id") int id) {
        articleService.deleteArticle(id);
    }
}
