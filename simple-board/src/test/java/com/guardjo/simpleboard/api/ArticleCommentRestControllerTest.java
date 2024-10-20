package com.guardjo.simpleboard.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.guardjo.simpleboard.config.TestSecurityConfig;
import com.guardjo.simpleboard.dto.CommentCreateRequest;
import com.guardjo.simpleboard.service.CommentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(TestSecurityConfig.class)
@WebMvcTest(ArticleCommentRestController.class)
class ArticleCommentRestControllerTest {
    private final static String TEST_USER_MAIL = "test@mail.com";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CommentService commentService;

    @DisplayName("POST : " + UrlContext.COMMENTS_URL)
    @WithUserDetails(value = TEST_USER_MAIL, setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    void test_createArticleComment() throws Exception {
        long articleId = 99L;
        String content = "test";
        CommentCreateRequest createRequest = new CommentCreateRequest(articleId, content);
        String request = objectMapper.writeValueAsString(createRequest);

        willDoNothing().given(commentService).createComment(eq(articleId), eq(content), eq(TEST_USER_MAIL));

        mockMvc.perform(post(UrlContext.COMMENTS_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andDo(print())
                .andExpect(status().isOk());

        then(commentService).should().createComment(eq(articleId), eq(content), eq(TEST_USER_MAIL));
    }
}