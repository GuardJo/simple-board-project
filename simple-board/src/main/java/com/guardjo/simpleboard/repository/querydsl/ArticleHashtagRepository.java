package com.guardjo.simpleboard.repository.querydsl;

import java.util.List;

public interface ArticleHashtagRepository {
    List<String> findAllDistinctHashtags();
}
