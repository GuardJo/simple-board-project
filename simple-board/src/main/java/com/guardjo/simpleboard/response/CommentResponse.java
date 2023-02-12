package com.guardjo.simpleboard.response;

import com.guardjo.simpleboard.dto.CommentDto;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

public record CommentResponse(Long id, LocalDateTime createTime, String creator, String content, Long parentCommentId, Set<CommentResponse> childComments)  {
    public static CommentResponse of(Long id, LocalDateTime createTime, String creator, String content, Long parentCommentId, Set<CommentResponse> childComments) {
        return new CommentResponse(id, createTime, creator, content, parentCommentId, childComments);
    }

    public static CommentResponse from(CommentDto commentDto) {
        CommentResponse commentResponse = CommentResponse.of(
                commentDto.id(),
                commentDto.createTime(),
                commentDto.creator(),
                commentDto.content(),
                commentDto.parentCommentId(),
                CommentUtil.sortComments(commentDto.childComments().stream()
                        .map(CommentResponse::from)
                        .collect(Collectors.toSet()))
        );

        return commentResponse;
    }
}
