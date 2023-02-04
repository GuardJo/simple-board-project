package com.guardjo.simpleboard.dto;

import com.guardjo.simpleboard.domain.Article;
import com.guardjo.simpleboard.domain.Comment;
import com.guardjo.simpleboard.domain.Member;

import java.time.LocalDateTime;

/**
 * A DTO for the {@link com.guardjo.simpleboard.domain.Comment} entity
 */
public record CommentDto(Long id, Long articleId, Long parentCommentId, String creator, LocalDateTime createTime, String content) {
    public static CommentDto of(Long id, Long articleId, Long parentCommentId, String creator, LocalDateTime createTime, String content) {
        return new CommentDto(id, articleId, parentCommentId, creator, createTime, content);
    }

    public static Comment toEntity(CommentDto commentDto, Member member, Article article, Comment parentComment) {
        Comment comment = Comment.of(
                member,
                article,
                commentDto.content()
        );

        if (parentComment != null) {
            comment.setParentComment(parentComment);
        }

        return comment;
    }
}