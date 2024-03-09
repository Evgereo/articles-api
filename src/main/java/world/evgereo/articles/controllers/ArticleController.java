package world.evgereo.articles.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import world.evgereo.articles.dtos.CreateUpdateArticleDto;
import world.evgereo.articles.models.Article;
import world.evgereo.articles.services.ArticleService;
import world.evgereo.articles.utils.UriPageBuilder;

import java.util.List;

import static world.evgereo.articles.utils.UriPageBuilder.buildPageUri;

@RestController
@RequiredArgsConstructor
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "http://localhost:8080")
public class ArticleController {
    private final ArticleService articleService;

    @GetMapping("/articles")
    public ResponseEntity<?> getArticles() {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>(1);
        map.add("Location", buildPageUri(0, 10));
        return new ResponseEntity<>(map, HttpStatus.MOVED_PERMANENTLY);
    }

    @GetMapping(value = "/articles", params = {"page", "size"})
    public ResponseEntity<List<Article>> getPaginatedArticles(@RequestParam(defaultValue = "0") int page,
                                                              @RequestParam(defaultValue = "10") int size) {
        Page<Article> paginatedArticles = articleService.getPaginatedArticles(page, size);
        if (paginatedArticles.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(paginatedArticles.getContent(), new UriPageBuilder(paginatedArticles).getAllPagesUri(), HttpStatus.OK);
    }

    @GetMapping(value = "/users/{userId}/articles", params = {"page", "size"})
    public ResponseEntity<List<Article>> getArticlesOfUser(@PathVariable("userId") int id,
                                                           @RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "10") int size) {
        Page<Article> paginatedArticles = articleService.getPaginatedArticlesByAuthorId(id, page, size);
        if (paginatedArticles.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(paginatedArticles.getContent(), new UriPageBuilder(paginatedArticles).getAllPagesUri(), HttpStatus.OK);
    }

    @GetMapping("/articles/{id}")
    public ResponseEntity<Article> getArticle(@PathVariable("id") int id) {
        return new ResponseEntity<>(articleService.loadArticleById(id), HttpStatus.OK);
    }

    @PostMapping("/articles/new")
    public ResponseEntity<Article> createArticle(@RequestBody @Valid CreateUpdateArticleDto article) {
        return new ResponseEntity<>(articleService.createArticle(article), HttpStatus.CREATED);
    }

    @PatchMapping(value = "/articles/{id}", params = "edit", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Article> editArticle(@RequestBody @Valid CreateUpdateArticleDto article, @PathVariable("id") int id) {
        return new ResponseEntity<>(articleService.updateArticle(article, id), HttpStatus.OK);
    }

    @DeleteMapping(value = "/articles/{id}", params = "delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteArticle(@PathVariable("id") int id) {
        articleService.deleteArticle(id);
    }
}
