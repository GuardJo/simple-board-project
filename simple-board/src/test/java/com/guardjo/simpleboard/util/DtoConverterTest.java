package com.guardjo.simpleboard.util;

import com.guardjo.simpleboard.domain.Article;
import com.guardjo.simpleboard.domain.Comment;
import com.guardjo.simpleboard.domain.Hashtag;
import com.guardjo.simpleboard.domain.Member;
import com.guardjo.simpleboard.dto.ArticleDto;
import com.guardjo.simpleboard.dto.CommentDto;
import com.guardjo.simpleboard.dto.HashtagDto;
import com.guardjo.simpleboard.dto.MemberDto;
import com.guardjo.simpleboard.generator.TestDataGenerator;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class DtoConverterTest {
    private DtoConverter dtoConverter = new DtoConverter();
    private TestDataGenerator testDataGenerator = new TestDataGenerator();

    @DisplayName("Article -> ArticleDto 테스트")
    @Test
    void testConvertArticleToArticleDto() {
        Article article = testDataGenerator.generateArticle("test");
        article.addHashtag(Hashtag.of("testHahtag"));

        ArticleDto articleDto = dtoConverter.from(article);

        assertThat(articleDto.creator()).isEqualTo(article.getCreator());
        assertThat(articleDto.createTime()).isEqualTo(article.getCreateTime());
        assertThat(articleDto.title()).isEqualTo(article.getTitle());
        assertThat(articleDto.content()).isEqualTo(article.getContent());
        assertThat(isEqualBetweenHashtags(article.getHashtags(), articleDto.hashtags())).isTrue();
    }

    @DisplayName("Comment -> CommentDto 테스트")
    @Test
    void testConvertCommentToCommentDto() {
        Comment comment = testDataGenerator.generateComment("test", 1L);
        CommentDto commentDto = dtoConverter.from(comment);

        assertThat(commentDto.creator()).isEqualTo(comment.getCreator());
        assertThat(commentDto.createTime()).isEqualTo(comment.getCreateTime());
        assertThat(commentDto.content()).isEqualTo(comment.getContent());
    }

    @DisplayName("CommentWithSubComments -> CommentDto 테스트")
    @Test
    void testConvertCommentToCommentDtoWithSubComments() {
        Comment comment = testDataGenerator.generateComment("test", 1L);
        Comment subComment = testDataGenerator.generateComment("test2", 1L);
        comment.addChildComment(subComment);

        CommentDto commentDto = dtoConverter.from(comment);

        assertThat(commentDto.creator()).isEqualTo(comment.getCreator());
        assertThat(commentDto.createTime()).isEqualTo(comment.getCreateTime());
        assertThat(commentDto.content()).isEqualTo(comment.getContent());

        assertThat(commentDto).hasFieldOrPropertyWithValue("parentCommentId", null)
                .extracting("childComments", InstanceOfAssertFactories.COLLECTION)
                .hasSize(1);
    }

    @DisplayName("Member -> MemberDto 테스트")
    @Test
    void testConvertMemberToMemberDto() {
        Member member = testDataGenerator.generateMember();
        MemberDto memberDto = dtoConverter.from(member);

        assertThat(memberDto.email()).isEqualTo(member.getEmail());
        assertThat(memberDto.name()).isEqualTo(member.getName());
        assertThat(memberDto.password()).isEqualTo(member.getPassword());
    }

    private boolean isEqualBetweenHashtags(Set<Hashtag> hashtags, Set<HashtagDto> hashtagDtos) {
        return hashtags.stream()
                .allMatch(hashtag -> {
                    return hashtagDtos.stream()
                            .anyMatch(hashtagDto -> hashtagDto.hashtagName().equals(hashtag.getHashtagName()));
                });
    }
}