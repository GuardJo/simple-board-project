package com.guardjo.simpleboard.dto;

import com.guardjo.simpleboard.domain.Article;
import com.guardjo.simpleboard.domain.Comment;
import com.guardjo.simpleboard.domain.Member;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * A DTO for the {@link com.guardjo.simpleboard.domain.Comment} entity
 */
public record CommentDto(Long id, Long articleId, Long parentCommentId, Set<CommentDto> childComments, String creator, LocalDateTime createTime, String content) {
    public static CommentDto of(Long id, Long articleId, String creator, LocalDateTime createTime, String content) {
        return new CommentDto(id, articleId, null, new HashSet<>(), creator, createTime, content);
    }

    public static CommentDto of(Long id, Long articleId, Long parentCommentId, String creator,LocalDateTime createTime, String content) {
        return new CommentDto(id, articleId, parentCommentId, new HashSet<>(), creator, createTime, content);
    }

    public static Comment toEntity(CommentDto commentDto, Member member, Article article) {
        Comment comment = Comment.of(
                member,
                article,
                commentDto.content(),
                commentDto.parentCommentId
        );

        return comment;
    }

    public void addAllChildComments(Set<CommentDto> commentDtos) {
        childComments.addAll(commentDtos);
    }
}