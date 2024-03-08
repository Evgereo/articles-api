package world.evgereo.articles.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import world.evgereo.articles.dtos.CreateUpdateArticleDto;
import world.evgereo.articles.services.ArticleService;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static world.evgereo.articles.mockfactories.ArticleMockFactory.*;

@ExtendWith(MockitoExtension.class)
class ArticleControllerTest {
    @Mock
    private ArticleService articleService;
    @InjectMocks
    private ArticleController articleController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(articleController)
                .build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void getArticles() throws Exception {
        when(articleService.getArticles()).thenReturn(getListOfTwoArticles());
        mockMvc.perform(get("/articles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
        verify(articleService, times(1)).getArticles();
    }

    @Test
    void getArticle() throws Exception {
        when(articleService.loadArticleById(1)).thenReturn(getFirstArticle());
        mockMvc.perform(get("/articles/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.articleId").value(1));
        verify(articleService, times(1)).loadArticleById(1);
    }

    @Test
    void createArticle() throws Exception {
        when(articleService.createArticle(any(CreateUpdateArticleDto.class))).thenReturn(getFirstArticle());
        String articleJson = objectMapper.writeValueAsString(getCreateUpdateArticleDTO());
        mockMvc.perform(post("/articles/new")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(articleJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.articleId").value(1));
        verify(articleService, times(1)).createArticle(any(CreateUpdateArticleDto.class));
    }

    @Test
    void editArticle() throws Exception {
        when(articleService.updateArticle(any(CreateUpdateArticleDto.class), eq(1))).thenReturn(getFirstArticle());
        String articleJson = objectMapper.writeValueAsString(getCreateUpdateArticleDTO());
        mockMvc.perform(patch("/articles/{id}?edit", 1)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(articleJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.articleId").value(1));
        verify(articleService, times(1)).updateArticle(any(CreateUpdateArticleDto.class), eq(1));
    }

    @Test
    void deleteArticle() throws Exception {
        mockMvc.perform(delete("/articles/{id}?delete", 1))
                .andExpect(status().isNoContent());
        verify(articleService, times(1)).deleteArticle(1);
    }
}
