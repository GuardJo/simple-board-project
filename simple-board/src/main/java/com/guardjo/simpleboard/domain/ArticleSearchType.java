package com.guardjo.simpleboard.domain;

import lombok.Getter;

public enum ArticleSearchType {
    TITLE("제목"),
    CONTENT("내용"),
    HASHTAG("해시태그"),
    CREATOR("작성자");

    @Getter
    private final String description;

    ArticleSearchType(String description) {
        this.description = description;
    }
}
