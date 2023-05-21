package com.guardjo.simpleboard.domain.projection;

import com.guardjo.simpleboard.domain.Comment;
import com.guardjo.simpleboard.domain.Member;
import org.springframework.data.rest.core.config.Projection;

import java.time.LocalDateTime;

@Projection(name = "externalComment", types = Comment.class)
public interface CommentProjection {
    String getCreator();
    LocalDateTime getCreateTime();
    String getModifier();
    LocalDateTime getModifiedTime();
    long getId();
    long getParentCommentId();
    String getContent();
    Member getMember();
}
