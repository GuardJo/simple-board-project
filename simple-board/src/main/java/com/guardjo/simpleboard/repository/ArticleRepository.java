package com.guardjo.simpleboard.repository;

import com.guardjo.simpleboard.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {
}
