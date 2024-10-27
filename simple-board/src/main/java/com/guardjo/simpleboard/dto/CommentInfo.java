package com.guardjo.simpleboard.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.List;

public record CommentInfo(
        Long id,
        String creator,
        String content,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime createTime,
        Long parentCommentId,
        List<CommentInfo> childComments,
        boolean isOwner
) {
}
