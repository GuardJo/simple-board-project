package com.guardjo.simpleboard.dto;

import com.guardjo.simpleboard.domain.Article;
import com.guardjo.simpleboard.domain.Hashtag;
import com.guardjo.simpleboard.domain.Member;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A DTO for the {@link com.guardjo.simpleboard.domain.Article} entity
 */
public record ArticleDto(Long id, String creator, LocalDateTime createTime, String title, String content,
                         Set<HashtagDto> hashtagDtos) {
    public static ArticleDto of(Long id, String creator, LocalDateTime createTime, String title, String content, Set<HashtagDto> hashtagDtos) {
        return new ArticleDto(id, creator, createTime, title, content, hashtagDtos);
    }

    public static Article toEntity(ArticleDto articleDto, Member member) {
        return Article.of(member, articleDto.title(), articleDto.content());
    }

    public static Article toEntity(ArticleDto articleDto, Member member, Set<HashtagDto> hashtagDtos) {
        Article article = toEntity(articleDto, member);
        Set<Hashtag> hashtags = hashtagDtos.stream().map(hashtagDto -> HashtagDto.toEntity(hashtagDto)).collect(Collectors.toSet());
        article.addHashtags(hashtags);

        return article;
    }
}