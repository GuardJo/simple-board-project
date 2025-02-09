package com.guardjo.simpleboard.service;

import com.guardjo.simpleboard.domain.Article;
import com.guardjo.simpleboard.domain.ArticleSearchType;
import com.guardjo.simpleboard.domain.Hashtag;
import com.guardjo.simpleboard.domain.Member;
import com.guardjo.simpleboard.dto.*;
import com.guardjo.simpleboard.generator.TestDataGenerator;
import com.guardjo.simpleboard.repository.ArticleRepository;
import com.guardjo.simpleboard.repository.HashtagRepository;
import com.guardjo.simpleboard.repository.MemberRepository;
import com.guardjo.simpleboard.util.DtoConverter;
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
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {
    @InjectMocks
    private ArticleService articleService;
    @Mock
    private ArticleRepository articleRepository;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private HashtagRepository hashtagRepository;

    private final TestDataGenerator testDataGenerator = new TestDataGenerator();
    private final int PAGE_SIZE = 10;

    @DisplayName("제목, 본문, 해시태그 등 특정 검색 타입으로 게시글 검색 테스트")
    @ParameterizedTest
    @MethodSource("searchParams")
    void testSearchArticles(ArticleSearchType searchType, String searchValue) {
        Pageable pageable = Pageable.ofSize(PAGE_SIZE);

        switch (searchType) {
            case HASHTAG ->
                    given(articleRepository.findByHashtagName(searchValue, pageable)).willReturn(Page.empty(pageable));
            case CREATOR ->
                    given(articleRepository.findByCreatorContaining(searchValue, pageable)).willReturn(Page.empty(pageable));
            case CONTENT ->
                    given(articleRepository.findByContentContaining(searchValue, pageable)).willReturn(Page.empty(pageable));
            case TITLE ->
                    given(articleRepository.findByTitleContaining(searchValue, pageable)).willReturn(Page.empty(pageable));
        }

        Page<ArticleDto> articleDtoList = articleService.findArticles(searchType, searchValue, Pageable.ofSize(PAGE_SIZE));

        assertThat(articleDtoList).isNotNull();
    }

    @DisplayName("검색어를 입력하지 않고 검색 테스트")
    @ParameterizedTest
    @MethodSource("searchParams")
    void testSearchArticlesWithOutSearchValue(ArticleSearchType searchType) {
        Pageable pageable = Pageable.ofSize(PAGE_SIZE);

        given(articleRepository.findAll(pageable)).willReturn(Page.empty(pageable));

        Page<ArticleDto> articleDtoList = articleService.findArticles(searchType, null, Pageable.ofSize(PAGE_SIZE));

        assertThat(articleDtoList).isEmpty();
    }

    @DisplayName("해시태그 검색 시 검색 요청 값이 없을 경우 빈 페이지 반환 테스트")
    @Test
    void testSearchHashTagWithOutSearchValue() {
        Pageable pageable = Pageable.ofSize(PAGE_SIZE);
        ArticleSearchType searchType = ArticleSearchType.HASHTAG;

        Page<ArticleDto> articleDtos = articleService.findArticlesViaHashtag(null, pageable);

        // 검색 값이 없을 경우 빈 페이지를 반환하도록 한다
        assertThat(articleDtos).isEqualTo(Page.empty(pageable));
        then(articleRepository).shouldHaveNoInteractions();
    }

    @DisplayName("해시태그 검색 시 검색 요청 값이 존재할 경우 테스트")
    @Test
    void testSearchHashTagWithSearchValue() {
        Pageable pageable = Pageable.ofSize(PAGE_SIZE);
        String searchValue = "test";

        given(articleRepository.findByHashtagName(searchValue, pageable)).willReturn(Page.empty(pageable));

        Page<ArticleDto> articleDtos = articleService.findArticlesViaHashtag(searchValue, pageable);

        assertThat(articleDtos).isEqualTo(Page.empty(pageable));
        then(articleRepository).should().findByHashtagName(searchValue, pageable);
    }

    @DisplayName("전체 게시글들의 해시태그 목록 반환 테스트")
    @Test
    void testFindDistinctHashtagsInAllOfArticles() {
        List<String> hashtagList = List.of("hashtag1", "hashtag2", "hashtag3");

        given(articleRepository.findAllDistinctHashtags()).willReturn(hashtagList);

        List<String> actual = articleService.findAllHashtags();

        assertThat(actual).isEqualTo(hashtagList);
        then(articleRepository).should().findAllDistinctHashtags();
    }

    @DisplayName("제목, 해시태그, 작성자, 작성일시 순 게시글 정렬(ASC) 기능 테스트")
    @ParameterizedTest
    @EnumSource(ArticleSearchType.class)
    void testSortArticlesToASC(ArticleSearchType articleSearchType) {
        // 본문은 정렬 대상이 아님
        if (articleSearchType != ArticleSearchType.CONTENT) {
            given(articleRepository.findAll(any(Pageable.class))).willReturn(Page.empty(Pageable.ofSize(PAGE_SIZE)));
            Page<ArticleDto> articleDtoPage = articleService.sortArticles(articleSearchType, Sort.Direction.ASC, 1, PAGE_SIZE);

            assertThat(articleDtoPage).isNotNull();
        }
    }

    @DisplayName("제목, 해시태그, 작성자, 작성일시 순 게시글 정렬(DESC) 기능 테스트")
    @ParameterizedTest
    @EnumSource(ArticleSearchType.class)
    void testSortArticlesToDESC(ArticleSearchType articleSearchType) {
        // 본문은 정렬 대상이 아님
        if (articleSearchType != ArticleSearchType.CONTENT) {
            given(articleRepository.findAll(any(Pageable.class))).willReturn(Page.empty(Pageable.ofSize(PAGE_SIZE)));
            Page<ArticleDto> articleDtoPage = articleService.sortArticles(articleSearchType, Sort.Direction.DESC, 1, PAGE_SIZE);

            assertThat(articleDtoPage).isNotNull();
        }
    }

    @DisplayName("게시글 목록 반환 시 페이징 기능 테스트")
    @ParameterizedTest
    @MethodSource("searchParams")
    void testPaginationArticles(ArticleSearchType searchType, String searchValue) {
        Pageable pageable = Pageable.ofSize(PAGE_SIZE);

        switch (searchType) {
            case HASHTAG ->
                    given(articleRepository.findByHashtagName(searchValue, pageable)).willReturn(Page.empty(pageable));
            case CREATOR ->
                    given(articleRepository.findByCreatorContaining(searchValue, pageable)).willReturn(Page.empty(pageable));
            case CONTENT ->
                    given(articleRepository.findByContentContaining(searchValue, pageable)).willReturn(Page.empty(pageable));
            case TITLE ->
                    given(articleRepository.findByTitleContaining(searchValue, pageable)).willReturn(Page.empty(pageable));
        }

        Page<ArticleDto> articleDtoPage = articleService.findArticles(searchType, searchValue, Pageable.ofSize(PAGE_SIZE));

        assertThat(articleDtoPage.getSize()).isNotEqualTo(0);
    }

    @DisplayName("특정 게시글 클릭 시 해당 게시글 정보 반환 테스트")
    @Test
    void testSelectArticle() {
        given(articleRepository.findById(any(Long.class)))
                .willReturn(Optional.ofNullable(testDataGenerator.generateArticle("test")));
        ArticleWithCommentDto articleWithCommentDto = articleService.findArticle(1L);

        assertThat(articleWithCommentDto).isNotNull();
    }

    @DisplayName("특정 게시글 클릭 시 해당 게시글 정보가 없을 시 예외 처리 테스트")
    @Test
    void testNotFoundArticle() {
        given(articleRepository.findById(0L)).willReturn(Optional.empty());
        Throwable t = catchThrowable(() -> articleService.findArticle(0L));

        assertThat(t).isInstanceOf(EntityNotFoundException.class);
    }

    @DisplayName("게시글의 제목, 본문, 해시태그 수정 테스트")
    @Test
    void testUpdateArticle() {
        String memberMail = "test@mail.com";
        ArticleUpdateDto updateDto = testDataGenerator.generateArticleUpdateDto(1L, "changeContent");
        Article article = testDataGenerator.generateArticle("test");
        article.setCreator(memberMail);
        Set<String> hashtagNames = Set.of("test");

        given(articleRepository.getReferenceById(1L)).willReturn(article);
        given(memberRepository.findByEmail(anyString())).willReturn(Optional.of(Member.of(memberMail, "tester", "pwd")));

        for (String hashtagName : hashtagNames) {
            given(hashtagRepository.findByHashtagName(eq(hashtagName))).willReturn(Optional.of(Hashtag.of(hashtagName)));
        }

        articleService.updateArticle(updateDto, memberMail, hashtagNames);

        then(articleRepository).should().getReferenceById(1L);
        then(memberRepository).should().findByEmail(anyString());
        then(hashtagRepository).should().findByHashtagName(anyString());
    }

    @DisplayName("게시글의 제목, 본문, 해시태그 수정 시 기존 값이 없을 경우 테스트")
    @Test
    void testUpdateArticleButNull() {
        ArticleUpdateDto updateDto = testDataGenerator.generateArticleUpdateDto(0L, "changeContent");
        String memberMail = "test@mail.com";

        given(articleRepository.getReferenceById(0L)).willReturn(null);

        assertThatCode(() -> articleService.updateArticle(updateDto, memberMail, Set.of(("test"))))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @DisplayName("게시글 저장 테스트")
    @Test
    void testSaveArticle() {
        Article article = testDataGenerator.generateArticle("test");
        String testUserMail = "test@mail.com";
        ArticleCreateRequest createRequest = new ArticleCreateRequest(article.getTitle(), article.getContent());

        given(articleRepository.save(any(Article.class))).willReturn(article);
        given(memberRepository.findByEmail(eq(testUserMail))).willReturn(Optional.of(Member.of(testUserMail, "tester", "pwd")));
        articleService.saveArticle(createRequest, testUserMail, Set.of());

        then(articleRepository).should().save(any(Article.class));
        then(memberRepository).should().findByEmail(eq(testUserMail));
    }

    @DisplayName("게시글 삭제 테스트")
    @Test
    void testDeleteArticle() {
        willDoNothing().given(articleRepository).deleteByIdAndMember_Email(anyLong(), anyString());

        articleService.deleteArticle(1L, "test@mail.com");

        then(articleRepository).should().deleteByIdAndMember_Email(anyLong(), anyString());
    }

    @DisplayName("게시글 상세 정보 조회")
    @Test
    void test_findArticleDetailInfo() {
        Article article = testDataGenerator.generateArticle("test");
        ArticleDetailInfo expected = DtoConverter.toArticleDetailInfo(article, "");
        long articleId = 99L;

        given(articleRepository.findById(eq(articleId))).willReturn(Optional.of(article));

        ArticleDetailInfo actual = articleService.findArticleDetailInfo(articleId, "");

        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo(expected);

        then(articleRepository).should().findById(eq(articleId));
    }

    @DisplayName("본인 게시글 상세 정보 조회")
    @Test
    void test_findArticleDetailInfo_myArticle() {
        Article article = testDataGenerator.generateArticle("test");
        String userMail = article.getMember().getEmail();
        ArticleDetailInfo expected = DtoConverter.toArticleDetailInfo(article, userMail);
        long articleId = 99L;

        given(articleRepository.findById(eq(articleId))).willReturn(Optional.of(article));

        ArticleDetailInfo actual = articleService.findArticleDetailInfo(articleId, userMail);

        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo(expected);
        assertThat(actual.isOwner()).isTrue();

        then(articleRepository).should().findById(eq(articleId));
    }

    private static Stream<Arguments> searchParams() {
        return Stream.of(
                Arguments.of(ArticleSearchType.TITLE, "title"),
                Arguments.of(ArticleSearchType.CONTENT, "Content"),
                Arguments.of(ArticleSearchType.HASHTAG, "Hashtag"),
                Arguments.of(ArticleSearchType.CREATOR, "Creator")
        );
    }
}