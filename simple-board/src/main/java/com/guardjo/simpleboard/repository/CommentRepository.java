package com.guardjo.simpleboard.repository;

import com.guardjo.simpleboard.domain.Comment;
import com.guardjo.simpleboard.domain.QComment;
import com.querydsl.core.types.dsl.DateTimeExpression;
import com.querydsl.core.types.dsl.StringExpression;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface CommentRepository extends JpaRepository<Comment, Long>,
        QuerydslPredicateExecutor<Comment>, QuerydslBinderCustomizer<QComment> {

    void deleteByIdAndMember_Email(Long id, String memberMail);
    @Override
    default void customize(QuerydslBindings bindings, QComment root) {
        bindings.excludeUnlistedProperties(true);
        bindings.including(root.content, root.creator, root.createTime);

        bindings.bind(root.content).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.creator).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.createTime).first(DateTimeExpression::eq);
    }
}
