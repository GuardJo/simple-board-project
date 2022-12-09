package com.guardjo.simpleboard.util;

import com.guardjo.simpleboard.domain.Article;
import com.guardjo.simpleboard.domain.Comment;
import com.guardjo.simpleboard.domain.Member;
import com.guardjo.simpleboard.dto.ArticleDto;
import com.guardjo.simpleboard.dto.ArticleWithCommentDto;
import com.guardjo.simpleboard.dto.CommentDto;
import com.guardjo.simpleboard.dto.MemberDto;

import java.util.HashSet;
import java.util.Set;

public class DtoConverter {
    public static ArticleDto from(Article article) {
        return ArticleDto.of(
                article.getId(),
                article.getCreator(),
                article.getCreateTime(),
                article.getTitle(),
                article.getContent(),
                article.getHashtag()
        );
    }

    public static CommentDto from(Comment comment) {
        return CommentDto.of(
                comment.getId(),
                comment.getCreator(),
                comment.getCreateTime(),
                comment.getContent(),
                comment.getHashtag()
        );
    }

    public static Set<CommentDto> from(Set<Comment> comments) {
        Set<CommentDto> commentDtos = new HashSet<>();

        comments.forEach((comment -> {
            commentDtos.add(from(comment));
        }));

        return commentDtos;
    }

    public static MemberDto from(Member member) {
        return MemberDto.of(
                member.getMemberId(),
                member.getCreator(),
                member.getCreateTime(),
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
                article.getHashtag(),
                from(article.getMember()),
                from(article.getComments())
        );
    }
}
