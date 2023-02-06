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
public record HashtagDto(Long id, String name) implements Serializable {
    public static HashtagDto of(Long id, String name) {
        return new HashtagDto(id, name);
    }

    public static Hashtag toEntity(HashtagDto hashtagDto) {
        return Hashtag.of(
                hashtagDto.name
        );
    }

    public static Set<Hashtag> toEntity(Set<HashtagDto> hashtagDtos) {
        return hashtagDtos.stream().map(hashtagDto -> HashtagDto.toEntity(hashtagDto)).collect(Collectors.toSet());
    }
}