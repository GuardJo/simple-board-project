package com.guardjo.simpleboard.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest
public class RestDataTest {
    private final MockMvc mockMvc;

    public RestDataTest(@Autowired MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @DisplayName("article 목록 반환 테스트")
    @Test
    void testGetArticles() throws Exception {
        mockMvc.perform(get("/api/articles"))
                .andExpect(status().isOk());
    }

    @DisplayName("article 단건 반환 테스트")
    @Test
    void testGetArticle() throws Exception {
        mockMvc.perform(get("/api/articles/1"))
                .andExpect(status().isOk());
    }

    @DisplayName("특정 article에 포함된 comment 목록 출력 테스트")
    @Test
    void testGetCommentsInArticle() throws Exception {
        mockMvc.perform(get("/api/articles/1/comments"))
                .andExpect(status().isOk());
    }

    @DisplayName("comment 목록 출력 테스트")
    @Test
    void testGetComments() throws Exception {
        mockMvc.perform(get("/api/comments"))
                .andExpect(status().isOk());
    }

    @DisplayName("comment 단건 출력 테스트")
    @Test
    void testGetComment() throws Exception {
        mockMvc.perform(get("/api/comments/1"))
                .andExpect(status().isOk());
    }
}
