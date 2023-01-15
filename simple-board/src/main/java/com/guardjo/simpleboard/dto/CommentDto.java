package com.guardjo.simpleboard.dto;

import com.guardjo.simpleboard.domain.Article;
import com.guardjo.simpleboard.domain.Comment;
import com.guardjo.simpleboard.domain.Member;

import java.time.LocalDateTime;

/**
 * A DTO for the {@link com.guardjo.simpleboard.domain.Comment} entity
 */
public record CommentDto(Long id, Long articleId, String creator, LocalDateTime createTime, String content,
                         String hashtag) {
    public static CommentDto of(Long id, Long articleId, String creator, LocalDateTime createTime, String content, String hashtag) {
        return new CommentDto(id, articleId, creator, createTime, content, hashtag);
    }

    public static Comment toEntity(CommentDto commentDto, Member member, Article article) {

        return Comment.of(
                member,
                article,
                commentDto.content,
                commentDto.hashtag
        );
    }
}