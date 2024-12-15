package com.guardjo.simpleboard.repository;

import com.guardjo.simpleboard.domain.Hashtag;
import com.guardjo.simpleboard.domain.QHashtag;
import com.querydsl.core.types.dsl.DateTimeExpression;
import com.querydsl.core.types.dsl.StringExpression;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource
public interface HashtagRepository extends JpaRepository<Hashtag, Long>,
        QuerydslBinderCustomizer<QHashtag>, QuerydslPredicateExecutor<Hashtag> {
    Optional<Hashtag> findByHashtagName(String hashtagName);

    @Override
    default void customize(QuerydslBindings bindings, QHashtag root) {
        bindings.excludeUnlistedProperties(true);
        bindings.including(root.hashtagName);

        bindings.bind(root.hashtagName).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.createTime).first(DateTimeExpression::eq);
    }
}
