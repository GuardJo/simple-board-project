package com.guardjo.simpleboard.dto;

import java.time.LocalDateTime;

/**
 * A DTO for the {@link com.guardjo.simpleboard.domain.Comment} entity
 */
public record CommentDto(Long id, String creator, LocalDateTime createTime, String content, String hashtag) {
    public static CommentDto of(Long id, String creator, LocalDateTime createTime, String content, String hashtag) {
        return new CommentDto(id, creator, createTime, content, hashtag);
    }
}