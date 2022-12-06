package com.guardjo.simpleboard.service;

import com.guardjo.simpleboard.domain.Article;
import com.guardjo.simpleboard.domain.ArticleSearchType;
import com.guardjo.simpleboard.dto.ArticleDto;
import com.guardjo.simpleboard.dto.ArticleUpdateDto;
import com.guardjo.simpleboard.generator.TestDataGenerator;
import com.guardjo.simpleboard.repository.ArticleRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {
    @InjectMocks
    private ArticleService articleService;
    @Mock
    private ArticleRepository articleRepository;

    private TestDataGenerator testDataGenerator = new TestDataGenerator();

    @DisplayName("제목, 본문, 해시태그 등 특정 검색 타입으로 게시글 검색 테스트")
    @ParameterizedTest
    @MethodSource("searchParams")
    void testSearchArticles(ArticleSearchType searchType, String searchValue) {
        Page<ArticleDto> articleDtoList = articleService.findArticles(searchType, searchValue);

        assertThat(articleDtoList).isNotNull();
    }

    @DisplayName("제목, 해시태그, 작성자, 작성일시 순 게시글 정렬(ASC) 기능 테스트")
    @ParameterizedTest
    @EnumSource(ArticleSearchType.class)
    void testSortArticlesToASC(ArticleSearchType articleSearchType) {
        // 본문은 정렬 대상이 아님
        if (articleSearchType != ArticleSearchType.CONTENT) {
            Page<ArticleDto> articleDtoPage = articleService.sortArticles(articleSearchType, Sort.Direction.ASC);

            assertThat(articleDtoPage.iterator().next()).isNotNull();
        }
    }

    @DisplayName("제목, 해시태그, 작성자, 작성일시 순 게시글 정렬(DESC) 기능 테스트")
    @ParameterizedTest
    @EnumSource(ArticleSearchType.class)
    void testSortArticlesToDESC(ArticleSearchType articleSearchType) {
        // 본문은 정렬 대상이 아님
        if (articleSearchType != ArticleSearchType.CONTENT) {
            Page<ArticleDto> articleDtoPage = articleService.sortArticles(articleSearchType, Sort.Direction.DESC);

            assertThat(articleDtoPage.iterator().next()).isNotNull();
        }
    }

    @DisplayName("게시글 목록 반환 시 페이징 기능 테스트")
    @ParameterizedTest
    @MethodSource("searchParams")
    void testPaginationArticles(ArticleSearchType searchType, String searchValue) {
        Page<ArticleDto> articleDtoPage = articleService.findArticles(searchType, searchValue);

        assertThat(articleDtoPage.getTotalPages()).isNotEqualTo(0);
    }

    @DisplayName("특정 게시글 클릭 시 해당 게시글 정보 반환 테스트")
    @Test
    void testSelectArticle() {
        ArticleDto articleDto = articleService.findArticle(1L);

        assertThat(articleDto).isNotNull();
    }

    @DisplayName("게시글의 제목, 본문, 해시태그 수정 테스트")
    @Test
    void testUpdateArticle() {
        ArticleUpdateDto updateDto = testDataGenerator.generateArticleUpdateDto("changeContent");

        given(articleRepository.save(any(Article.class))).willReturn(null);

        articleService.updateArticle(1L, updateDto);

        then(articleRepository).should().save(any(Article.class));
    }

    @DisplayName("게시글 저장 테스트")
    @Test
    void testSaveArticle() {
        given(articleRepository.save(any(Article.class))).willReturn(any(Article.class));

        articleService.saveArticle(testDataGenerator.convertArticleDto(testDataGenerator.generateArticle("test")));

        then(articleRepository).should().save(any(Article.class));
    }

    @DisplayName("게시글 삭제 테스트")
    @Test
    void testDeleteArticle() {
        willDoNothing().given(articleRepository).deleteById(any(Long.class));

        articleService.deleteArticle(1L);

        then(articleRepository).should().deleteById(any(Long.class));
    }

    @DisplayName("현재 게시글의 이전 게시글 반환 테스트")
    @Test
    void testFindPreviousArticle() {
        given(articleRepository.findById(any(Long.class))).willReturn(Optional.of(testDataGenerator.generateArticle("prev")));
        ArticleDto articleDto = articleService.findPrevArticle(1L);

        assertThat(articleDto.title()).isEqualTo("prev");
    }

    @DisplayName("현재 게시글의 다음 게시글 반환 테스트")
    @Test
    void testFindNextArticle() {
        given(articleRepository.findById(any(Long.class))).willReturn(Optional.of(testDataGenerator.generateArticle("next")));
        ArticleDto articleDto = articleService.findPrevArticle(1L);

        assertThat(articleDto.title()).isEqualTo("next");
    }

    private static Stream<Arguments> searchParams() {
        return Stream.of(
                Arguments.of(ArticleSearchType.TITLE, "title"),
                Arguments.of(ArticleSearchType.CONTENT, "Content"),
                Arguments.of(ArticleSearchType.HASHTAG, "Hashtag"),
                Arguments.of(ArticleSearchType.CREATOR, "Creator"),
                Arguments.of(ArticleSearchType.CREATETIME, LocalDateTime.now().toString())
        );
    }
}