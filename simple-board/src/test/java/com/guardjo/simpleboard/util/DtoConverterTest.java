package com.guardjo.simpleboard.util;

import com.guardjo.simpleboard.domain.Article;
import com.guardjo.simpleboard.domain.Comment;
import com.guardjo.simpleboard.domain.Member;
import com.guardjo.simpleboard.dto.ArticleDto;
import com.guardjo.simpleboard.dto.CommentDto;
import com.guardjo.simpleboard.dto.MemberDto;
import com.guardjo.simpleboard.generator.TestDataGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DtoConverterTest {
    private DtoConverter dtoConverter = new DtoConverter();
    private TestDataGenerator testDataGenerator = new TestDataGenerator();

    @DisplayName("Article -> ArticleDto 테스트")
    @Test
    void testConvertArticleToArticleDto() {
        Article article = testDataGenerator.generateArticle("test");
        ArticleDto articleDto = dtoConverter.from(article);

        assertThat(articleDto.creator()).isEqualTo(article.getCreator());
        assertThat(articleDto.createTime()).isEqualTo(article.getCreateTime());
        assertThat(articleDto.title()).isEqualTo(article.getTitle());
        assertThat(articleDto.content()).isEqualTo(article.getContent());
        assertThat(articleDto.hashtag()).isEqualTo(article.getHashtag());
    }

    @DisplayName("Comment -> CommentDto 테스트")
    @Test
    void testConvertCommentToCommentDto() {
        Comment comment = testDataGenerator.generateComment("test", 1L);
        CommentDto commentDto = dtoConverter.from(comment);

        assertThat(commentDto.creator()).isEqualTo(comment.getCreator());
        assertThat(commentDto.createTime()).isEqualTo(comment.getCreateTime());
        assertThat(commentDto.content()).isEqualTo(comment.getContent());
        assertThat(commentDto.hashtag()).isEqualTo(comment.getHashtag());
    }

    @DisplayName("Member -> MemberDto 테스트")
    @Test
    void testConvertMemberToMemberDto() {
        Member member = testDataGenerator.generateMember();
        MemberDto memberDto = dtoConverter.from(member);

        assertThat(memberDto.creator()).isEqualTo(member.getCreator());
        assertThat(memberDto.createTime()).isEqualTo(member.getCreateTime());
        assertThat(memberDto.email()).isEqualTo(member.getEmail());
        assertThat(memberDto.name()).isEqualTo(member.getName());
        assertThat(memberDto.password()).isEqualTo(member.getPassword());
    }
}