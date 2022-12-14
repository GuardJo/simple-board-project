package com.guardjo.simpleboard.generator;

import com.guardjo.simpleboard.domain.Article;
import com.guardjo.simpleboard.domain.Comment;
import com.guardjo.simpleboard.domain.Member;
import com.guardjo.simpleboard.dto.ArticleDto;
import com.guardjo.simpleboard.dto.ArticleUpdateDto;
import com.guardjo.simpleboard.dto.CommentDto;

public class TestDataGenerator {
    public Member generateMember() {
        return Member.of("test@mail.com", "tester", "1234");
    }
    public Article generateArticle(String title) {
        Article article = Article.of(generateMember(), title, "content", "#hashtag");
        return Article.of(generateMember(), title, "content", "#hashtag");
    }

    public Article generateArticle(String title, Long id) {
        Article article = Article.of(id, generateMember(), title, "content", "#hashtag");
        return article;
    }

    public ArticleDto convertArticleDto(Article article) {
        return ArticleDto.of(
                article.getId(),
                article.getCreator(),
                article.getCreateTime(),
                article.getTitle(),
                article.getContent(),
                article.getHashtag()
        );
    }

    public ArticleUpdateDto generateArticleUpdateDto(Long id, String changeContent) {
        return ArticleUpdateDto.of(id, "title", changeContent, "#update");
    }

    public Comment generateComment(String content, Long articleId) {
        return Comment.of(generateMember(), generateArticle("test", articleId), content, "#commment");
    }

    public CommentDto convertCommentDto(Comment comment) {
        return CommentDto.of(
                comment.getId(),
                comment.getArticle().getId(),
                comment.getCreator(),
                comment.getCreateTime(),
                comment.getContent(),
                comment.getHashtag()
        );
    }
}
