package com.guardjo.simpleboard.response;

import com.guardjo.simpleboard.dto.ArticleDto;
import com.guardjo.simpleboard.dto.HashtagDto;

import java.time.LocalDateTime;
import java.util.Set;

public record ArticleResponse(Long id, String title, String content, String hastags, String creator, LocalDateTime createTime) {
    public static ArticleResponse of(Long id, String title, String content, String hashtags, String creator, LocalDateTime createTime) {
        return new ArticleResponse(id, title, content, hashtags, creator, createTime);
    }

    public static ArticleResponse from(ArticleDto articleDto) {
        return ArticleResponse.of(
                articleDto.id(),
                articleDto.title(),
                articleDto.content(),
                flatHashtags(articleDto.hashtagDtos()),
                articleDto.creator(),
                articleDto.createTime());
    }

    protected static String flatHashtags(Set<HashtagDto> hashtagDtoSet) {
        StringBuilder stringBuilder = new StringBuilder();

        hashtagDtoSet.forEach(hashtagDto -> stringBuilder.append(hashtagDto.name() + " "));

        return stringBuilder.toString();
    }
}
