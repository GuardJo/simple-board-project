package com.guardjo.simpleboard.response;

import com.guardjo.simpleboard.dto.CommentDto;

import java.time.LocalDateTime;

public record CommentResponse(Long id, LocalDateTime createTime, String creator, String content) {
    public static CommentResponse of(Long id, LocalDateTime createTime, String creator, String content) {
        return new CommentResponse(id, createTime, creator, content);
    }

    public static CommentResponse from(CommentDto commentDto) {
        return CommentResponse.of(
                commentDto.id(),
                commentDto.createTime(),
                commentDto.creator(),
                commentDto.content()
        );
    }
}
