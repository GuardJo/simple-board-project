package com.guardjo.simpleboard.repository;

import com.guardjo.simpleboard.domain.Article;
import com.guardjo.simpleboard.domain.Hashtag;
import com.guardjo.simpleboard.domain.QArticle;
import com.guardjo.simpleboard.domain.QHashtag;
import com.guardjo.simpleboard.repository.querydsl.ArticleHashtagRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.CollectionExpression;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.DateTimeExpression;
import com.querydsl.core.types.dsl.ListExpression;
import com.querydsl.core.types.dsl.SetPath;
import com.querydsl.core.types.dsl.StringExpression;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.MultiValueBinding;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@RepositoryRestResource
public interface ArticleRepository extends JpaRepository<Article, Long>, ArticleHashtagRepository,
        QuerydslPredicateExecutor<Article>, QuerydslBinderCustomizer<QArticle> {

    Page<Article> findByTitleContaining(String title, Pageable pageable);
    Page<Article> findByContentContaining(String content, Pageable pageable);
    Page<Article> findByCreatorContaining(String creator, Pageable pageable);

    void deleteByIdAndMember_Email(Long articleId, String mail);

    @Override
    default void customize(QuerydslBindings bindings, QArticle root) {
        bindings.excludeUnlistedProperties(true);
        bindings.including(root.title, root.content, root.creator, root.createTime, root.hashtags);

        bindings.bind(root.title).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.content).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.creator).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.createTime).first(DateTimeExpression::eq);
        bindings.bind(root.hashtags.any().hashtagName).first(StringExpression::containsIgnoreCase);
    }
}
