package com.guardjo.simpleboard.generator;

import com.guardjo.simpleboard.domain.Article;
import com.guardjo.simpleboard.domain.Comment;
import com.guardjo.simpleboard.domain.Member;
import com.guardjo.simpleboard.dto.ArticleDto;
import com.guardjo.simpleboard.dto.ArticleUpdateDto;
import com.guardjo.simpleboard.dto.CommentDto;
import com.guardjo.simpleboard.util.DtoConverter;

import java.time.LocalDateTime;

public class TestDataGenerator {
    public Member generateMember() {
        return Member.of("test@mail.com", "tester", "1234");
    }
    public Article generateArticle(String title) {
        return Article.of(generateMember(), title, "content");
    }

    public ArticleDto convertArticleDto(Article article) {
        return ArticleDto.of(
                article.getId(),
                article.getCreator(),
                article.getCreateTime(),
                article.getTitle(),
                article.getContent(),
                DtoConverter.from(article.getHashtags())
        );
    }

    public ArticleUpdateDto generateArticleUpdateDto(Long id, String changeContent) {
        return ArticleUpdateDto.of(id, "title", changeContent);
    }

    public Comment generateComment(String content, Long articleId) {
        return Comment.of(generateMember(), generateArticle("test"), content);
    }

    public Comment generateSubComment(String content, Long articleId, Long parentCommentId) {
        return Comment.of(generateMember(), generateArticle("test"), content, parentCommentId);
    }

    public CommentDto generateCommentDto(Long id, Long articleId, Long parentCommentId, String content) {
        return CommentDto.of(id, articleId, parentCommentId, "tester", LocalDateTime.now(), content);
    }

    public CommentDto convertCommentDto(Comment comment) {
        return CommentDto.of(
                comment.getId(),
                comment.getArticle().getId(),
                comment.getParentCommentId(),
                comment.getCreator(),
                comment.getCreateTime(),
                comment.getContent()
        );
    }
}
