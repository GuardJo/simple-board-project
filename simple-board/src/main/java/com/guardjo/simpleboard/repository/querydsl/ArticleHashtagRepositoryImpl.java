package com.guardjo.simpleboard.repository.querydsl;

import com.guardjo.simpleboard.domain.Article;
import com.guardjo.simpleboard.domain.QArticle;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class ArticleHashtagRepositoryImpl extends QuerydslRepositorySupport implements ArticleHashtagRepository {
    /**
     * Creates a new {@link QuerydslRepositorySupport} instance for the given domain type.
     *
     */
    public ArticleHashtagRepositoryImpl() {
        super(Article.class);
    }

    @Override
    public List<String> findAllDistinctHashtags() {
        QArticle qArticle = QArticle.article;

        JPQLQuery<String> query = from(qArticle)
                .distinct()
                .select(qArticle.hashtag)
                .where(qArticle.hashtag.isNotNull());

        return query.fetch();
    }
}
