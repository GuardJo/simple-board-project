package com.guardjo.simpleboard.dto;

public record CommentCreateRequest(
        long articleId,
        String content
) {
}
