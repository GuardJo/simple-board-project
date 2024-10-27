package com.guardjo.simpleboard.util;

import com.guardjo.simpleboard.domain.Article;
import com.guardjo.simpleboard.domain.Comment;
import com.guardjo.simpleboard.domain.Hashtag;
import com.guardjo.simpleboard.domain.Member;
import com.guardjo.simpleboard.dto.*;
import com.guardjo.simpleboard.dto.security.SimpleBoardPrincipal;

import java.util.Comparator;
import java.util.Objects;
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
                hashtag.getHashtagName()
        );
    }

    public static ArticleDetailInfo toArticleDetailInfo(Article article, String userMail) {
        return new ArticleDetailInfo(
                DtoConverter.from(article),
                article.getComments().stream()
                        .map(comment -> from(comment, userMail))
                        .filter(commentInfo -> Objects.isNull(commentInfo.parentCommentId()))
                        .toList(),
                article.getMember().getEmail().equals(userMail)
        );
    }

    public static CommentInfo from(Comment comment, String userMail) {
        return new CommentInfo(
                comment.getId(),
                comment.getCreator(),
                comment.getContent(),
                comment.getCreateTime(),
                comment.getParentCommentId(),
                comment.getChildComments().stream()
                        .map(childComment -> DtoConverter.from(childComment, userMail))
                        .sorted(Comparator.comparing(CommentInfo::createTime).reversed())
                        .toList(),
                comment.getMember().getEmail().equals(userMail)
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
