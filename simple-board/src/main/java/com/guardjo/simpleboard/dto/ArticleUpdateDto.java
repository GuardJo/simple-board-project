package com.guardjo.simpleboard.dto;

/**
 * A DTO for the {@link com.guardjo.simpleboard.domain.Article} entity
 */
public record ArticleUpdateDto(Long id, String title, String content) {
    public static ArticleUpdateDto of(Long id, String title, String content) {
        return new ArticleUpdateDto(id, title, content);
    }
}