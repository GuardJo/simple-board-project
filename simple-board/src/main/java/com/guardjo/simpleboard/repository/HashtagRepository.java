package com.guardjo.simpleboard.repository;

import com.guardjo.simpleboard.domain.Hashtag;
import com.guardjo.simpleboard.domain.QHashtag;
import com.querydsl.core.types.dsl.DateTimeExpression;
import com.querydsl.core.types.dsl.StringExpression;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;

import java.util.List;

public interface HashtagRepository extends JpaRepository<Hashtag, Long>,
        QuerydslBinderCustomizer<QHashtag>, QuerydslPredicateExecutor<Hashtag> {
    boolean existsByName(String name);

    @Override
    default void customize(QuerydslBindings bindings, QHashtag root) {
        bindings.excludeUnlistedProperties(true);
        bindings.including(root.name);

        bindings.bind(root.name).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.createTime).first(DateTimeExpression::eq);
    }
}
