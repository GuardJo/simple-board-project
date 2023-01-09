package com.guardjo.simpleboard.controller;

import com.guardjo.simpleboard.config.SecurityConfig;
import com.guardjo.simpleboard.dto.CommentDto;
import com.guardjo.simpleboard.generator.TestDataGenerator;
import com.guardjo.simpleboard.service.CommentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(SecurityConfig.class)
@WebMvcTest(CommentController.class)
class CommentControllerTest {
    @MockBean
    private CommentService commentService;

    private final MockMvc mockMvc;
    private final TestDataGenerator testDataGenerator;

    public CommentControllerTest(@Autowired MockMvc mockMvc) {
        this.mockMvc = mockMvc;
        this.testDataGenerator = new TestDataGenerator();
    }

    @DisplayName("댓글 저장 요청 테스트")
    @Test
    void testSaveComment() throws Exception {
        Long articleId = 1L;
        CommentDto commentDto = testDataGenerator.convertCommentDto(testDataGenerator.generateComment("test content", articleId));

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("articleId", String.valueOf(commentDto.articleId()));
        params.add("content", commentDto.content());

        willDoNothing().given(commentService).saveComment(any(CommentDto.class));

        mockMvc.perform(post("/comment")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .queryParams(params)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/article/" + articleId))
                .andExpect(redirectedUrl("/article/" + articleId));

        then(commentService).should().saveComment(any(CommentDto.class));
    }

    @DisplayName("댓글 삭제 요청 테스트")
    @Test
    void testDeleteComment() throws Exception {
        Long articleId = 1L;
        Long commentId = 1L;

        willDoNothing().given(commentService).deleteComment(commentId);

        mockMvc.perform(delete("/comment/" + commentId)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .queryParam("articleId", String.valueOf(articleId))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/article/" + articleId))
                .andExpect(redirectedUrl("/article/" + articleId));

        then(commentService).should().deleteComment(commentId);
    }
}