package com.guardjo.simpleboard.controller;

import com.guardjo.simpleboard.config.SecurityConfig;
import com.guardjo.simpleboard.domain.ArticleSearchType;
import com.guardjo.simpleboard.generator.TestDataGenerator;
import com.guardjo.simpleboard.service.ArticleService;
import com.guardjo.simpleboard.service.PaginationService;
import com.guardjo.simpleboard.util.DtoConverter;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(SecurityConfig.class)
@WebMvcTest(ArticleController.class)
class ArticleControllerTest {

    @MockBean
    private ArticleService articleService;
    @MockBean
    private PaginationService paginationService;
    private final MockMvc mockMvc;
    private final TestDataGenerator testDataGenerator;

    ArticleControllerTest(@Autowired MockMvc mockMvc) {
        this.mockMvc = mockMvc;
        this.testDataGenerator = new TestDataGenerator();
    }

    @DisplayName("Article 목록 페이지 반환 테스트")
    @Test
    void testGetArticleView() throws Exception {
        given(articleService.findArticles(eq(null), eq(null), any(Pageable.class))).willReturn(Page.empty());
        given(paginationService.getPaginationNumbers(anyInt(), anyInt())).willReturn(List.of(1, 2, 3, 4, 5));

        mockMvc.perform(get("/article"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(model().attributeExists("articles"))
                .andExpect(model().attributeExists("paginationNumbers"));

        then(articleService).should().findArticles(eq(null), eq(null), any(Pageable.class));
        then(paginationService).should().getPaginationNumbers(anyInt(), anyInt());
    }

    @DisplayName("Article 목록을 반환 (paging 및 sorting 파라미터 추가) 테스트")
    @Test
    void testGetArticlesWithPagingAndSorting() throws Exception {
        String sortField = "title";
        int pageNumber = 0;
        int pageSize = 5;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.Direction.DESC, sortField);
        List<Integer> paginationBarNumbers = List.of(0, 1, 2, 3, 4);

        given(articleService.findArticles(null, null, pageable)).willReturn(Page.empty(pageable));
        given(paginationService.getPaginationNumbers(anyInt(), anyInt())).willReturn(paginationBarNumbers);

        mockMvc.perform(get("/article")
                        .param("page", String.valueOf(pageNumber))
                        .param("size", String.valueOf(pageSize))
                        .param("sort", sortField + "," + Sort.Direction.DESC.name())
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(model().attributeExists("articles"))
                .andExpect(model().attributeExists("paginationNumbers"));

        then(articleService).should().findArticles(null, null, pageable);
        then(paginationService).should().getPaginationNumbers(pageNumber, Page.empty(pageable).getTotalPages());
    }

    @DisplayName("Article 목록을 반환 (articleSearchType 및  searchValue 포함) 테스트")
    @ParameterizedTest
    @EnumSource(ArticleSearchType.class)
    void testGetArticlesWithSearchValue(ArticleSearchType searchType) throws Exception {
        Pageable pageable = Pageable.ofSize(5);
        List<Integer> paginationBarNumbers = List.of(0, 1, 2, 3, 4);
        String searchValue = "test";

        given(articleService.findArticles(any(ArticleSearchType.class), any(String.class), any(Pageable.class))).willReturn(Page.empty(pageable));
        given(paginationService.getPaginationNumbers(anyInt(), anyInt())).willReturn(paginationBarNumbers);

        mockMvc.perform(get("/article")
                        .queryParam("articleSearchType", searchType.name())
                        .queryParam("searchValue", searchValue)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(model().attributeExists("articles"))
                .andExpect(model().attributeExists("paginationNumbers"))
                .andExpect(model().attributeExists("articleSearchTypes"));

        then(articleService).should().findArticles(any(ArticleSearchType.class), any(String.class), any(Pageable.class));
        then(paginationService).should().getPaginationNumbers(0, Page.empty(pageable).getTotalPages());
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

    @DisplayName("Hashtag 검색 페이지 반환 테스트")
    @Test
    void testGetArticleHashtagSearchView() throws Exception {
        String searchValue = "hashtag1";
        Pageable pageable = Pageable.ofSize(10);
        List<Integer> paginationBarNumbers = List.of(0, 1, 2, 3, 4);

        given(articleService.findArticlesViaHashtag(eq(searchValue), any(Pageable.class))).willReturn(Page.empty(pageable));
        given(paginationService.getPaginationNumbers(anyInt(), anyInt())).willReturn(paginationBarNumbers);

        mockMvc.perform(get("/article/search-hashtag")
                        .queryParam("searchValue", searchValue)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("article/search-hashtag"))
                .andExpect(model().attribute("articles", Page.empty(pageable)))
                .andExpect(model().attributeExists("paginationNumbers"))
                .andExpect(model().attributeExists("hashtags"))
                .andExpect(model().attribute("articleSearchType", ArticleSearchType.HASHTAG));

        then(articleService).should().findArticlesViaHashtag(eq(searchValue), any(Pageable.class));
    }
}