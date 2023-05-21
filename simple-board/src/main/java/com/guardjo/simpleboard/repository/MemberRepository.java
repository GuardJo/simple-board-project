package com.guardjo.simpleboard.repository;

import com.guardjo.simpleboard.domain.Member;
import com.guardjo.simpleboard.domain.QMember;
import com.guardjo.simpleboard.domain.projection.MemberProjection;
import com.querydsl.core.types.dsl.DateTimeExpression;
import com.querydsl.core.types.dsl.StringExpression;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource(excerptProjection = MemberProjection.class)
public interface MemberRepository extends JpaRepository<Member, Long>,
        QuerydslPredicateExecutor<Member>, QuerydslBinderCustomizer<QMember> {
    Optional<Member> findByEmail(String email);
    @Override
    default void customize(QuerydslBindings bindings, QMember root) {
        bindings.excludeUnlistedProperties(true);
        bindings.including(root.email, root.name, root.creator, root.createTime);

        bindings.bind(root.email).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.name).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.creator).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.createTime).first(DateTimeExpression::eq);
    }
}
