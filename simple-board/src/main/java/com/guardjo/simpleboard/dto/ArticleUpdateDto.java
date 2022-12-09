package com.guardjo.simpleboard.dto;

/**
 * A DTO for the {@link com.guardjo.simpleboard.domain.Article} entity
 */
public record ArticleUpdateDto(Long id, String title, String content, String hashtag) {
    public static ArticleUpdateDto of(Long id, String title, String content, String hashtag) {
        return new ArticleUpdateDto(id, title, content, hashtag);
    }
}