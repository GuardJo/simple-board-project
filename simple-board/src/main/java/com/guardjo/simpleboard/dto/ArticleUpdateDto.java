package com.guardjo.simpleboard.dto;

/**
 * A DTO for the {@link com.guardjo.simpleboard.domain.Article} entity
 */
public record ArticleUpdateDto(String title, String content, String hashtag) {
    public static ArticleUpdateDto of(String title, String content, String hashtag) {
        return new ArticleUpdateDto(title, content, hashtag);
    }
}