package com.guardjo.simpleboard.response;

import com.guardjo.simpleboard.dto.CommentDto;

import java.time.LocalDateTime;

public record CommentResponse(Long id, LocalDateTime createTime, String creator, String content, String hashtag) {
    public static CommentResponse of(Long id, LocalDateTime createTime, String creator, String content, String hashtag) {
        return new CommentResponse(id, createTime, creator, content, hashtag);
    }

    public static CommentResponse from(CommentDto commentDto) {
        return CommentResponse.of(
                commentDto.id(),
                commentDto.createTime(),
                commentDto.creator(),
                commentDto.content(),
                commentDto.hashtag()
        );
    }
}
