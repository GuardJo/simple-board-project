package com.guardjo.simpleboard.repository;

import com.guardjo.simpleboard.domain.Article;
import com.guardjo.simpleboard.domain.QArticle;
import com.guardjo.simpleboard.repository.querydsl.ArticleHashtagRepository;
import com.querydsl.core.types.dsl.DateTimeExpression;
import com.querydsl.core.types.dsl.StringExpression;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface ArticleRepository extends JpaRepository<Article, Long>, ArticleHashtagRepository,
        QuerydslPredicateExecutor<Article>, QuerydslBinderCustomizer<QArticle> {

    Page<Article> findByTitleContaining(String title, Pageable pageable);
    Page<Article> findByContentContaining(String content, Pageable pageable);
    Page<Article> findByCreatorContaining(String creator, Pageable pageable);
    Page<Article> findArticlesByHashtagsContainsIgnoreCase(String hashtagName, Pageable pageable);

    void deleteByIdAndMember_Email(Long articleId, String mail);

    @Override
    default void customize(QuerydslBindings bindings, QArticle root) {
        bindings.excludeUnlistedProperties(true);
        bindings.including(root.title, root.content, root.creator, root.createTime);

        bindings.bind(root.title).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.content).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.creator).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.createTime).first(DateTimeExpression::eq);
    }
}
