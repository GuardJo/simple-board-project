package com.guardjo.simpleboard.repository.querydsl;

import com.guardjo.simpleboard.domain.Hashtag;
import com.guardjo.simpleboard.domain.QHashtag;
import com.querydsl.jpa.JPQLQuery;
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
                .select(qHashtag.name)
                .where(qHashtag.isNotNull());

        return query.fetch();
    }
}
