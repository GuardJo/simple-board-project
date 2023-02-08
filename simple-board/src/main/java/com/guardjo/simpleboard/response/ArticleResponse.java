package com.guardjo.simpleboard.response;

import com.guardjo.simpleboard.domain.Hashtag;
import com.guardjo.simpleboard.dto.ArticleDto;
import com.guardjo.simpleboard.dto.HashtagDto;

import java.time.LocalDateTime;
import java.util.Set;

public record ArticleResponse(Long id, String title, String content, Set<HashtagDto> hashtagDtos, String creator, LocalDateTime createTime) {
    public static ArticleResponse of(Long id, String title, String content, Set<HashtagDto> hashtagDtos, String creator, LocalDateTime createTime) {
        return new ArticleResponse(id, title, content, hashtagDtos, creator, createTime);
    }

    public static ArticleResponse from(ArticleDto articleDto) {
        return ArticleResponse.of(
                articleDto.id(),
                articleDto.title(),
                articleDto.content(),
                articleDto.hashtagDtos(),
                articleDto.creator(),
                articleDto.createTime());
    }
}
