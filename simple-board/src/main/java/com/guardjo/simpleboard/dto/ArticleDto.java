package com.guardjo.simpleboard.dto;

import java.time.LocalDateTime;

/**
 * A DTO for the {@link com.guardjo.simpleboard.domain.Article} entity
 */
public record ArticleDto(String creator, LocalDateTime createTime, String title, String content,
                         String hashtag) {
    public static ArticleDto of(String creator, LocalDateTime createTime, String title, String content, String hashtag) {
        return new ArticleDto(creator, createTime, title, content, hashtag);
    }
}