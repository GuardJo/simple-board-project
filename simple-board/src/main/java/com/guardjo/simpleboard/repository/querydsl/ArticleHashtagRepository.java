package com.guardjo.simpleboard.repository.querydsl;

import com.guardjo.simpleboard.domain.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ArticleHashtagRepository {
    List<String> findAllDistinctHashtags();
    Page<Article> findByHashtagName(String hashtagName, Pageable pageable);
}
