package com.guardjo.simpleboard.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.guardjo.simpleboard.config.TestSecurityConfig;
import com.guardjo.simpleboard.dto.CommentCreateRequest;
import com.guardjo.simpleboard.dto.CommentUpdateRequest;
import com.guardjo.simpleboard.service.CommentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import javax.persistence.EntityNotFoundException;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
        CommentCreateRequest createRequest = new CommentCreateRequest(articleId, null, content);
        String request = objectMapper.writeValueAsString(createRequest);

        willDoNothing().given(commentService).createComment(eq(createRequest), eq(TEST_USER_MAIL));

        mockMvc.perform(post(UrlContext.COMMENTS_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andDo(print())
                .andExpect(status().isOk());

        then(commentService).should().createComment(eq(createRequest), eq(TEST_USER_MAIL));
    }

    @DisplayName("POST : " + UrlContext.COMMENTS_URL + " : Redirect Login")
    @Test
    void test_createArticleComment_redirect() throws Exception {
        long articleId = 99L;
        String content = "test";
        CommentCreateRequest createRequest = new CommentCreateRequest(articleId, null, content);
        String request = objectMapper.writeValueAsString(createRequest);

        mockMvc.perform(post(UrlContext.COMMENTS_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andDo(print())
                .andExpect(status().is3xxRedirection());
    }

    @DisplayName("DELETE : " + UrlContext.COMMENTS_URL)
    @WithUserDetails(value = TEST_USER_MAIL, setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    void test_removeArticleComment() throws Exception {
        long commentId = 99L;

        willDoNothing().given(commentService).deleteComment(eq(commentId), eq(TEST_USER_MAIL));

        mockMvc.perform(delete(UrlContext.COMMENTS_URL + "/" + commentId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        then(commentService).should().deleteComment(eq(commentId), eq(TEST_USER_MAIL));
    }

    @DisplayName("DELETE : " + UrlContext.COMMENTS_URL + " : Redirect Login Page")
    @Test
    void test_removeArticleComment_rediect() throws Exception {
        long commentId = 99L;

        mockMvc.perform(delete(UrlContext.COMMENTS_URL + "/" + commentId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is3xxRedirection());
    }

    @DisplayName("PATCH : " + UrlContext.COMMENTS_URL)
    @WithUserDetails(value = TEST_USER_MAIL, setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    void test_updateArticleComment() throws Exception {
        Long commentId = 1L;
        String updateContent = "Update Content";
        CommentUpdateRequest updateRequest = new CommentUpdateRequest(commentId, updateContent);
        String request = objectMapper.writeValueAsString(updateRequest);

        willDoNothing().given(commentService).updateComment(eq(updateRequest), eq(TEST_USER_MAIL));

        mockMvc.perform(patch(UrlContext.COMMENTS_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andDo(print())
                .andExpect(status().isOk());

        then(commentService).should().updateComment(eq(updateRequest), eq(TEST_USER_MAIL));
    }

    @DisplayName("PATCH : " + UrlContext.COMMENTS_URL + " : Redirect LoginPage")
    @Test
    void test_updateArticleComment_redirectLogin() throws Exception {
        Long commentId = 1L;
        String updateContent = "Update Content";
        CommentUpdateRequest updateRequest = new CommentUpdateRequest(commentId, updateContent);
        String request = objectMapper.writeValueAsString(updateRequest);

        mockMvc.perform(patch(UrlContext.COMMENTS_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andDo(print())
                .andExpect(status().is3xxRedirection());
    }

    @DisplayName("PATCH : " + UrlContext.COMMENTS_URL + " : Forbidden User")
    @WithUserDetails(value = TEST_USER_MAIL, setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    void test_updateArticleComment_forbiddenUser() throws Exception {
        Long commentId = 1L;
        String updateContent = "Update Content";
        CommentUpdateRequest updateRequest = new CommentUpdateRequest(commentId, updateContent);
        String request = objectMapper.writeValueAsString(updateRequest);

        willThrow(AuthorizationServiceException.class).given(commentService).updateComment(eq(updateRequest), eq(TEST_USER_MAIL));

        mockMvc.perform(patch(UrlContext.COMMENTS_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andDo(print())
                .andExpect(status().isForbidden());

        then(commentService).should().updateComment(eq(updateRequest), eq(TEST_USER_MAIL));
    }

    @DisplayName("PATCH : " + UrlContext.COMMENTS_URL + " : NotFound Comment")
    @WithUserDetails(value = TEST_USER_MAIL, setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    void test_updateArticleComment_notFoundComment() throws Exception {
        Long commentId = 1L;
        String updateContent = "Update Content";
        CommentUpdateRequest updateRequest = new CommentUpdateRequest(commentId, updateContent);
        String request = objectMapper.writeValueAsString(updateRequest);

        willThrow(EntityNotFoundException.class).given(commentService).updateComment(eq(updateRequest), eq(TEST_USER_MAIL));

        mockMvc.perform(patch(UrlContext.COMMENTS_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andDo(print())
                .andExpect(status().isBadRequest());

        then(commentService).should().updateComment(eq(updateRequest), eq(TEST_USER_MAIL));
    }
}