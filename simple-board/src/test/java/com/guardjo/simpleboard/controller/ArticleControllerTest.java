package com.guardjo.simpleboard.controller;

import com.guardjo.simpleboard.config.SecurityConfig;
import com.guardjo.simpleboard.generator.TestDataGenerator;
import com.guardjo.simpleboard.service.ArticleService;
import com.guardjo.simpleboard.util.DtoConverter;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(SecurityConfig.class)
@WebMvcTest(ArticleController.class)
class ArticleControllerTest {

    @MockBean
    private ArticleService articleService;
    private final MockMvc mockMvc;
    private final TestDataGenerator testDataGenerator;

    ArticleControllerTest(@Autowired  MockMvc mockMvc) {
        this.mockMvc = mockMvc;
        this.testDataGenerator = new TestDataGenerator();
    }

    @DisplayName("Article 목록 페이지 반환 테스트")
    @Test
    void testGetArticleView() throws Exception {
        given(articleService.findArticles(eq(null), eq(null), any(Pageable.class))).willReturn(Page.empty());

        mockMvc.perform(get("/article"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(model().attributeExists("articles"));

        then(articleService).should().findArticles(eq(null), eq(null), any(Pageable.class));
    }

    @DisplayName("특정 Artile 페이지 반환 테스트")
    @Test
    void testGetArticleDetailView() throws Exception {
        Long articleId = 1L;
        given(articleService.findArticle(1L)).willReturn(
                DtoConverter.fromArticleWithComment(testDataGenerator.generateArticle("test")));

        mockMvc.perform(get("/article/" + articleId))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(model().attributeExists("article"))
                .andExpect(model().attributeExists("comments"));

        then(articleService).should().findArticle(1L);
    }

    @Disabled
    @DisplayName("검색 페이지(임시) 반환 테스트")
    @Test
    void testGetArticleSearchView() throws Exception {
        mockMvc.perform(get("/article/search"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML));
    }

    @Disabled
    @DisplayName("Hashtag 검색 페이지(임시) 반환 테스트")
    @Test
    void testGetArticleHashtagSearchView() throws Exception {
        mockMvc.perform(get("/article/search-hashtag"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML));
    }
}