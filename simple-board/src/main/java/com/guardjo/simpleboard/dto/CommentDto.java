package com.guardjo.simpleboard.dto;

import java.time.LocalDateTime;

/**
 * A DTO for the {@link com.guardjo.simpleboard.domain.Comment} entity
 */
public record CommentDto(String creator, LocalDateTime createTime, String content, String hashtag) {
    public static CommentDto of(String creator, LocalDateTime createTime, String content, String hashtag) {
        return new CommentDto(creator, createTime, content, hashtag);
    }
}