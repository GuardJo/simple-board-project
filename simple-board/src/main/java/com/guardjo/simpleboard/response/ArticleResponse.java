package com.guardjo.simpleboard.response;

import com.guardjo.simpleboard.dto.ArticleDto;

import java.time.LocalDateTime;

public record ArticleResponse(Long id, String title, String content, String hashtag, String creator, LocalDateTime createTime) {
    public static ArticleResponse of(Long id, String title, String content, String hashtag, String creator, LocalDateTime createTime) {
        return new ArticleResponse(id, title, content, hashtag, creator, createTime);
    }

    public static ArticleResponse from(ArticleDto articleDto) {
        return ArticleResponse.of(
                articleDto.id(),
                articleDto.title(),
                articleDto.content(),
                articleDto.hashtag(),
                articleDto.creator(),
                articleDto.createTime());
    }
}
