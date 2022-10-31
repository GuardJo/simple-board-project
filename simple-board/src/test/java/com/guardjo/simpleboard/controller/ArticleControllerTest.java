package com.guardjo.simpleboard.controller;

import com.guardjo.simpleboard.domain.Article;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ArticleController.class)
class ArticleControllerTest {
    private final MockMvc mockMvc;

    ArticleControllerTest(@Autowired  MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @DisplayName("Article 목록 페이지 반환 테스트")
    @Test
    void testGetArticleView() throws Exception {
        mockMvc.perform(get("/article"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(model().attributeExists("articles"));
    }

    @DisplayName("특정 Artile 페이지 반환 테스트")
    @Test
    void testGetArticleDetailView() throws Exception {
        mockMvc.perform(get("/article/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(model().attributeExists("article"))
                .andExpect(model().attributeExists("comments"));
    }

    @DisplayName("검색 페이지(임시) 반환 테스트")
    @Test
    void testGetArticleSearchView() throws Exception {
        mockMvc.perform(get("/article/search"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML));
    }

    @DisplayName("Hashtag 검색 페이지(임시) 반환 테스트")
    @Test
    void testGetArticleHashtagSearchView() throws Exception {
        mockMvc.perform(get("/article/search-hashtag"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML));
    }
}