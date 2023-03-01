package com.guardjo.simpleboard.service;

import com.guardjo.simpleboard.domain.Hashtag;
import com.guardjo.simpleboard.generator.TestDataGenerator;
import com.guardjo.simpleboard.repository.HashtagRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class HashtagServiceTest {
    @InjectMocks
    private HashtagService hashtagService;
    @Mock
    private HashtagRepository hashtagRepository;

    private TestDataGenerator testDataGenerator = new TestDataGenerator();

    @DisplayName("주어진 게시글 본문에서 해시태그 추출 테스트")
    @ParameterizedTest
    @MethodSource("parsingContents")
    void testParsingHashtagInContent(String content, Set<Hashtag> expectedHashtags) {
        Set<Hashtag> hashtags = hashtagService.parseHashtagsInContent(content);

        assertThat(hashtags).isEqualTo(expectedHashtags);
    }

    @DisplayName("신규 해시태그 저장 테스트")
    @Test
    void testSaveNewHashtags() {
        Set<Hashtag> hashtags = Set.of(Hashtag.of("test1"));

        given(hashtagRepository.save(any(Hashtag.class))).willReturn(any(Hashtag.class));

        hashtagService.saveHashtag(hashtags);

        then(hashtagRepository).should().save(any(Hashtag.class));
    }

    @DisplayName("기존 해시태그 저장 테스트")
    @Test
    void testSaveOldHashtags() {
        Set<Hashtag> hashtags = Set.of(Hashtag.of("test"));

        given(hashtagRepository.existsByHashtagName(anyString())).willReturn(true);

        hashtagService.saveHashtag(hashtags);

        then(hashtagRepository).shouldHaveNoMoreInteractions();
    }

    @DisplayName("게시글에 사용되지 않는 해시태그 삭제 테스트")
    @Test
    void testDeleteHashtagWithoutArticles() {
        long hashtagId = 1L;
        Hashtag hashtagWithoutArticles = Hashtag.of("test");

        willDoNothing().given(hashtagRepository).deleteById(anyLong());
        given(hashtagRepository.getReferenceById(anyLong())).willReturn(hashtagWithoutArticles);

        hashtagService.cleanHashtagWithoutArticles(hashtagId);

        then(hashtagRepository).should().deleteById(anyLong());
    }

    @DisplayName("게시글에 사용되고 있는 해시태그 삭제 테스트")
    @Test
    void testDeleteHashtagWituArticles() {
        long hashtagId = 1L;
        Hashtag hashtagWithArticles = Hashtag.of("test");
        hashtagWithArticles.getArticles().add(testDataGenerator.generateArticle("test"));

        given(hashtagRepository.getReferenceById(anyLong())).willReturn(hashtagWithArticles);

        hashtagService.cleanHashtagWithoutArticles(1L);

        then(hashtagRepository).shouldHaveNoMoreInteractions();
    }

    private static Stream parsingContents() {
        return Stream.of(
                arguments("#java", Set.of(Hashtag.of("java"))),
                arguments("#test, test #test2", Set.of(Hashtag.of("test"), Hashtag.of("test2"))),
                arguments("#test, test #test", Set.of(Hashtag.of("test"))),
                arguments("#Test_test", Set.of(Hashtag.of("Test_test"))),
                arguments("#Test-test", Set.of(Hashtag.of("Test"))),
                arguments("", Set.of()),
                arguments(null, Set.of()),
                arguments("dasdajsdfkjalkjkfjlsfdjlk#test dasdasdada", Set.of(Hashtag.of("test"))),
                arguments("              #test", Set.of(Hashtag.of("test"))),
                arguments("#test                  ", Set.of(Hashtag.of("test"))),
                arguments("##test", Set.of(Hashtag.of("test"))),
                arguments("#_test", Set.of(Hashtag.of("_test")))
        );
    }
}