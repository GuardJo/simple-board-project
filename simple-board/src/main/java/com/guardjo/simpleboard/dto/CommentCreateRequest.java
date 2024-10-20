package com.guardjo.simpleboard.dto;

public record CommentCreateRequest(
        long articleId,
        Long parentCommentId,
        String content
) {
}
