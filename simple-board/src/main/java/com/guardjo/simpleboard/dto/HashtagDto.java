package com.guardjo.simpleboard.dto;

import com.guardjo.simpleboard.domain.Hashtag;
import com.guardjo.simpleboard.domain.Member;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A DTO for the {@link com.guardjo.simpleboard.domain.Hashtag} entity
 */
public record HashtagDto(String creator, LocalDateTime createTime, String modifier, LocalDateTime modifiedTime, Long id,
                         String name, Set<ArticleDto> articles) implements Serializable {
    public static HashtagDto of(String creator, LocalDateTime createTime, String modifier, LocalDateTime modifiedTime, Long id, String name, Set<ArticleDto> articles) {
        return new HashtagDto(creator, createTime, modifier, modifiedTime, id, name, articles);
    }

    public static Hashtag toEntity(HashtagDto hashtagDto, Member member) {
        return Hashtag.of(
                hashtagDto.name
        );
    }

    public static Set<Hashtag> toEntity(Set<HashtagDto> hashtagDtos, Member member) {
        return hashtagDtos.stream().map(hashtagDto -> HashtagDto.toEntity(hashtagDto, member)).collect(Collectors.toSet());
    }
}