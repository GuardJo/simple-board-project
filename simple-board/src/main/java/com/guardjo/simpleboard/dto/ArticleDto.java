package com.guardjo.simpleboard.dto;

import com.guardjo.simpleboard.domain.Article;
import com.guardjo.simpleboard.domain.Member;

import java.time.LocalDateTime;

/**
 * A DTO for the {@link com.guardjo.simpleboard.domain.Article} entity
 */
public record ArticleDto(Long id, String creator, LocalDateTime createTime, String title, String content,
                         String hashtag) {
    public static ArticleDto of(Long id, String creator, LocalDateTime createTime, String title, String content, String hashtag) {
        return new ArticleDto(id, creator, createTime, title, content, hashtag);
    }

    public static Article toEntity(ArticleDto articleDto, MemberDto memberDto) {
        return Article.of(MemberDto.toEntity(memberDto), articleDto.title(), articleDto.content(), articleDto.hashtag());
    }
}