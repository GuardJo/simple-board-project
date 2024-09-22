package com.guardjo.simpleboard.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.guardjo.simpleboard.config.TestSecurityConfig;
import com.guardjo.simpleboard.domain.Article;
import com.guardjo.simpleboard.domain.ArticleSearchType;
import com.guardjo.simpleboard.dto.ArticleCreateRequest;
import com.guardjo.simpleboard.dto.ArticleDetailInfo;
import com.guardjo.simpleboard.dto.ArticleDto;
import com.guardjo.simpleboard.dto.ArticlePageDto;
import com.guardjo.simpleboard.generator.TestDataGenerator;
import com.guardjo.simpleboard.service.ArticleService;
import com.guardjo.simpleboard.service.HashtagService;
import com.guardjo.simpleboard.util.DtoConverter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(TestSecurityConfig.class)
@WebMvcTest(controllers = ArticleRestController.class)
class ArticleRestControllerTest {
    private final static List<Article> ARTICLE_TEST_DATA = new ArrayList<>();
    private final static int TEST_DATA_SZIE = 10;
    private final static String TEST_USER_MAIL = "test@mail.com";

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final TestDataGenerator testDataGenerator = new TestDataGenerator();

    @MockBean
    private ArticleService articleService;

    @MockBean
    private HashtagService hashtagService;

    @Autowired
    public ArticleRestControllerTest(MockMvc mockMvc, ObjectMapper mapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = mapper;
    }

    @BeforeEach
    void setUp() {
        for (int i = 0; i < TEST_DATA_SZIE; i++) {
            ARTICLE_TEST_DATA.add(testDataGenerator.generateArticle("Test_" + i));
        }
    }

    @AfterEach
    void tearDown() {
        ARTICLE_TEST_DATA.clear();
    }

    @DisplayName("GET : " + UrlContext.ARTICLES_URL)
    @Test
    void getArticlePage() throws Exception {
        String searchValue = "";
        List<ArticleDto> expected = ARTICLE_TEST_DATA.stream()
                .map(DtoConverter::from)
                .toList();

        given(articleService.findArticles(eq(ArticleSearchType.TITLE), eq(searchValue), any(Pageable.class)))
                .willReturn(new PageImpl<>(expected));

        String response = mockMvc.perform(get(UrlContext.ARTICLES_URL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        ArticlePageDto actual = objectMapper.readValue(response, ArticlePageDto.class);

        assertThat(actual).isNotNull();
        assertThat(actual.articles()).isEqualTo(expected);

        then(articleService).should().findArticles(eq(ArticleSearchType.TITLE), eq(searchValue), any(Pageable.class));
    }

    @DisplayName("GET : " + UrlContext.ARTICLES_URL + "/{articleId}")
    @Test
    void test_getArticleDetailInfo() throws Exception {
        long articleId = 99L;
        Article article = ARTICLE_TEST_DATA.get(0);
        ArticleDetailInfo expected = DtoConverter.toArticleDetailInfo(article, "");

        given(articleService.findArticleDetailInfo(eq(articleId), anyString())).willReturn(expected);

        String response = mockMvc.perform(get(UrlContext.ARTICLES_URL + "/" + articleId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        ArticleDetailInfo actual = objectMapper.readValue(response, ArticleDetailInfo.class);

        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo(expected);

        then(articleService).should().findArticleDetailInfo(eq(articleId), anyString());
    }

    @WithUserDetails(value = TEST_USER_MAIL, setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("GET : " + UrlContext.ARTICLES_URL + "/{articleId} : afterLogin")
    @Test
    void test_getArticleDetailInfo_myArticle() throws Exception {
        long articleId = 99L;
        Article article = ARTICLE_TEST_DATA.get(0);
        ArticleDetailInfo expected = DtoConverter.toArticleDetailInfo(article, TEST_USER_MAIL);

        given(articleService.findArticleDetailInfo(eq(articleId), eq(TEST_USER_MAIL))).willReturn(expected);

        String response = mockMvc.perform(get(UrlContext.ARTICLES_URL + "/" + articleId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        ArticleDetailInfo actual = objectMapper.readValue(response, ArticleDetailInfo.class);

        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo(expected);
        assertThat(actual.isOwner()).isEqualTo(article.getMember().getEmail().equals(TEST_USER_MAIL));

        then(articleService).should().findArticleDetailInfo(eq(articleId), anyString());
    }

    @WithUserDetails(value = TEST_USER_MAIL, setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("POST : " + UrlContext.ARTICLES_URL)
    @Test
    void test_createNewArticle() throws Exception {
        ArticleCreateRequest createRequest = new ArticleCreateRequest("Test Title", "test content");
        String request = objectMapper.writeValueAsString(createRequest);

        given(hashtagService.parseHashtagsInContent(eq(createRequest.content()))).willReturn(Set.of());
        willDoNothing().given(articleService).saveArticle(eq(createRequest), eq(TEST_USER_MAIL), anySet());

        mockMvc.perform(post(UrlContext.ARTICLES_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .content(request))
                .andDo(print())
                .andExpect(status().isOk());

        then(hashtagService).should().parseHashtagsInContent(eq(createRequest.content()));
        then(articleService).should().saveArticle(eq(createRequest), eq(TEST_USER_MAIL), anySet());
    }
}