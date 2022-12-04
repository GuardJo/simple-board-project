package com.guardjo.simpleboard.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * A DTO for the {@link com.guardjo.simpleboard.domain.Article} entity
 */
public record ArticleDto(String creator, LocalDateTime createTime, String title, String content,
                         String hashtag) implements Serializable {
}