package com.guardjo.simpleboard.generator;

import com.guardjo.simpleboard.domain.Article;
import com.guardjo.simpleboard.domain.Comment;
import com.guardjo.simpleboard.dto.ArticleDto;
import com.guardjo.simpleboard.dto.ArticleUpdateDto;
import com.guardjo.simpleboard.dto.CommentDto;

public class TestDataGenerator {
    public Article generateArticle(String title) {
        return Article.of(title, "content", "#hashtag");
    }

    public ArticleDto convertArticleDto(Article article) {
        return ArticleDto.of(
                article.getCreator(),
                article.getCreateTime(),
                article.getTitle(),
                article.getContent(),
                article.getHashtag()
        );
    }

    public ArticleUpdateDto generateArticleUpdateDto(String changeContent) {
        return ArticleUpdateDto.of("title", changeContent, "#update");
    }

    public Comment generateComment(String content) {
        return Comment.of(generateArticle("test"), content, "#commment");
    }

    public CommentDto convertCommentDto(Comment comment) {
        return CommentDto.of(
                comment.getCreator(),
                comment.getCreateTime(),
                comment.getContent(),
                comment.getHashtag()
        );
    }
}
