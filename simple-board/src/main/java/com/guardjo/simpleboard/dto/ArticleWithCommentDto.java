package com.guardjo.simpleboard.dto;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * A DTO for the {@link com.guardjo.simpleboard.domain.Article} entity
 */
public record ArticleWithCommentDto(Long id, String creator, LocalDateTime createTime, String modifier,
                                    LocalDateTime modifiedTime, String title, String content, String hashtag,
                                    MemberDto member, Set<CommentDto> commentDtos) {
    public static ArticleWithCommentDto of(Long id, String creator, LocalDateTime createTime, String modifier,
                                 LocalDateTime modifiedTime, String title, String content,
                                 String hashtag, MemberDto member, Set<CommentDto> commentDtos) {
        return new ArticleWithCommentDto(id, creator, createTime, modifier, modifiedTime, title, content, hashtag, member, commentDtos);
    }
}