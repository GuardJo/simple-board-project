package com.guardjo.simpleboard.dto;

public record CommentUpdateRequest(
        Long commentId,
        String content
) {
}
