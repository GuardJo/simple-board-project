package com.guardjo.simpleboard.domain.projection;

import com.guardjo.simpleboard.domain.Article;
import com.guardjo.simpleboard.domain.Member;
import org.springframework.data.rest.core.config.Projection;

import java.time.LocalDateTime;

@Projection(name = "externalArticle", types = Article.class)
public interface ArticleProjection {
    String getCreator();
    LocalDateTime getCreateTime();
    String getModifier();
    LocalDateTime getModifiedTime();
    long getId();
    String getTitle();
    String getContent();
    Member getMember();
}
