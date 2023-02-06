package com.guardjo.simpleboard.response;

import com.guardjo.simpleboard.dto.ArticleWithCommentDto;

import java.util.Set;
import java.util.stream.Collectors;

public record ArticleWithCommentResponse(Long id, ArticleResponse articleResponse, String content, Set<CommentResponse> commentResponses) {
    public static ArticleWithCommentResponse of(Long id, ArticleResponse articleResponse, String content, Set<CommentResponse> commentResponses) {
        return new ArticleWithCommentResponse(id, articleResponse, content, commentResponses);
    }

    public static ArticleWithCommentResponse from(ArticleWithCommentDto articleWithCommentDto) {
        return ArticleWithCommentResponse.of(
                articleWithCommentDto.id(),
                ArticleResponse.of(
                        articleWithCommentDto.id(),
                        articleWithCommentDto.title(),
                        articleWithCommentDto.content(),
                        articleWithCommentDto.hashtagDtos(),
                        articleWithCommentDto.creator(),
                        articleWithCommentDto.createTime()
                ),
                articleWithCommentDto.content(),
                articleWithCommentDto.commentDtos().stream().map(CommentResponse::from).collect(Collectors.toSet())
        );
    }
}
