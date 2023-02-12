package com.guardjo.simpleboard.util;

import com.guardjo.simpleboard.domain.Article;
import com.guardjo.simpleboard.domain.Comment;
import com.guardjo.simpleboard.domain.Hashtag;
import com.guardjo.simpleboard.domain.Member;
import com.guardjo.simpleboard.dto.*;
import com.guardjo.simpleboard.dto.security.SimpleBoardPrincipal;
import org.springframework.boot.autoconfigure.web.servlet.DefaultJerseyApplicationPath;

import java.util.Set;
import java.util.stream.Collectors;

public class DtoConverter {
    public static ArticleDto from(Article article) {
        return ArticleDto.of(
                article.getId(),
                article.getCreator(),
                article.getCreateTime(),
                article.getTitle(),
                article.getContent(),
                DtoConverter.from(article.getHashtags())
        );
    }

    public static CommentDto from(Comment comment) {
        CommentDto commentDto = CommentDto.of(
                comment.getId(),
                comment.getArticle().getId(),
                comment.getParentCommentId(),
                comment.getCreator(),
                comment.getCreateTime(),
                comment.getContent()
        );

        if (comment.hasChildComments()) {
            commentDto.addAllChildComments(comment.getChildComments().stream()
                    .map(DtoConverter::from).collect(Collectors.toSet()));
        }

        return commentDto;
    }

    public static MemberDto from(Member member) {
        return MemberDto.of(
                member.getEmail(),
                member.getName(),
                member.getPassword()
        );
    }

    public static ArticleWithCommentDto fromArticleWithComment(Article article) {
        return ArticleWithCommentDto.of(
                article.getId(),
                article.getCreator(),
                article.getCreateTime(),
                article.getModifier(),
                article.getModifiedTime(),
                article.getTitle(),
                article.getContent(),
                from(article.getHashtags()),
                from(article.getMember()),
                from(excludeChildComment(article.getComments()))
        );
    }

    public static MemberDto from(SimpleBoardPrincipal principal) {
        return MemberDto.of(principal.email(), principal.name(), principal.password());
    }

    public static HashtagDto from(Hashtag hashtag) {
        return HashtagDto.of(
                hashtag.getId(),
                hashtag.getName()
        );
    }

    public static Set from(Set set) {
        return (Set) set.stream()
                .map((t) -> {
                    if (t instanceof Hashtag) {
                        return from((Hashtag) t);
                    } else {
                        return from((Comment) t);
                    }
                })
                .collect(Collectors.toSet());
    }

    private static Set<Comment> excludeChildComment(Set<Comment> comments) {
        return comments.stream()
                .filter(comment -> comment.getParentCommentId() == null)
                .collect(Collectors.toSet());
    }
}
