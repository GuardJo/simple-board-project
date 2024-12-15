package com.guardjo.simpleboard.service;

import com.guardjo.simpleboard.domain.Hashtag;
import com.guardjo.simpleboard.dto.HashtagDto;
import com.guardjo.simpleboard.generator.TestDataGenerator;
import com.guardjo.simpleboard.repository.HashtagRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class HashtagServiceTest {
    private final TestDataGenerator testDataGenerator = new TestDataGenerator();

    @InjectMocks
    private HashtagService hashtagService;
    @Mock
    private HashtagRepository hashtagRepository;

    @DisplayName("주어진 게시글 본문에서 해시태그 추출 테스트")
    @ParameterizedTest
    @MethodSource("parsingContents")
    void testParsingHashtagInContent(String content, Set<String> hashtagNames) {

        Set<String> hashtags = hashtagService.parseHashtagsInContent(content);

        assertThat(hashtags).isEqualTo(hashtagNames);
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

    @DisplayName("해시태그 전체 목록 조회")
    @Test
    void test_findAllHashtags() {
        List<Hashtag> expectedList = List.of(
                Hashtag.of("Test1"),
                Hashtag.of("Test2"),
                Hashtag.of("Test3")
        );

        given(hashtagRepository.findAll()).willReturn(expectedList);

        List<HashtagDto> actualList = hashtagService.findAllHashtags();

        assertThat(actualList.size()).isEqualTo(expectedList.size());

        for (int i = 0; i < actualList.size(); i++) {
            assertThat(actualList.get(i).hashtagName()).isEqualTo(expectedList.get(i).getHashtagName());
        }

        then(hashtagRepository).should().findAll();
    }

    private static Stream<Arguments> parsingContents() {
        return Stream.of(
                arguments("#java", Set.of("java")),
                arguments("#test, test #test2", Set.of("test", "test2")),
                arguments("#test, test #test", Set.of("test")),
                arguments("#Test_test", Set.of("Test_test")),
                arguments("#Test-test", Set.of("Test")),
                arguments("", Set.of()),
                arguments(null, Set.of()),
                arguments("dasdajsdfkjalkjkfjlsfdjlk#test dasdasdada", Set.of("test")),
                arguments("              #test", Set.of("test")),
                arguments("#test                  ", Set.of("test")),
                arguments("##test", Set.of("test")),
                arguments("#_test", Set.of("_test"))
        );
    }
}