package com.guardjo.simpleboard.util;

import com.guardjo.simpleboard.domain.Article;
import com.guardjo.simpleboard.domain.Comment;
import com.guardjo.simpleboard.domain.Member;
import com.guardjo.simpleboard.dto.ArticleDto;
import com.guardjo.simpleboard.dto.CommentDto;
import com.guardjo.simpleboard.dto.MemberDto;

public class DtoConverter {
    public ArticleDto from(Article article) {
        return ArticleDto.of(article.getCreator(),
                article.getCreateTime(),
                article.getTitle(),
                article.getContent(),
                article.getHashtag()
        );
    }

    public CommentDto from(Comment comment) {
        return CommentDto.of(
                comment.getCreator(),
                comment.getCreateTime(),
                comment.getContent(),
                comment.getHashtag()
        );
    }

    public MemberDto from(Member member) {
        return MemberDto.of(
                member.getCreator(),
                member.getCreateTime(),
                member.getEmail(),
                member.getName(),
                member.getPassword()
        );
    }
}
