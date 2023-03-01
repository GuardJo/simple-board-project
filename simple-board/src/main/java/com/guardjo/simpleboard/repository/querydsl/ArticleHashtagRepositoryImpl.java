package com.guardjo.simpleboard.repository.querydsl;

import com.guardjo.simpleboard.domain.Article;
import com.guardjo.simpleboard.domain.Hashtag;
import com.guardjo.simpleboard.domain.QArticle;
import com.guardjo.simpleboard.domain.QHashtag;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class ArticleHashtagRepositoryImpl extends QuerydslRepositorySupport implements ArticleHashtagRepository {
    /**
     * Creates a new {@link QuerydslRepositorySupport} instance for the given domain type.
     */
    public ArticleHashtagRepositoryImpl() {
        super(Hashtag.class);
    }

    @Override
    public List<String> findAllDistinctHashtags() {
        QHashtag qHashtag = QHashtag.hashtag;

        JPQLQuery<String> query = from(qHashtag)
                .distinct()
                .select(qHashtag.hashtagName)
                .where(qHashtag.isNotNull());

        return query.fetch();
    }

    @Override
    public Page<Article> findByHashtagName(String hashtagName, Pageable pageable) {
        QArticle article = QArticle.article;
        QHashtag hashtag = QHashtag.hashtag;

        JPQLQuery<Article> query = from(article)
                .innerJoin(article.hashtags, hashtag)
                .where(hashtag.hashtagName.in(hashtagName));

        List<Article> articles = getQuerydsl().applyPagination(pageable, query).fetch();

        return new PageImpl<>(articles, pageable, query.fetchCount());
    }
}
