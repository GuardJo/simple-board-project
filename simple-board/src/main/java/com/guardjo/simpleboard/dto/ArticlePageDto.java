package com.guardjo.simpleboard.dto;

import org.springframework.data.domain.Page;

import java.util.List;

public record ArticlePageDto(
        int number,
        int totalPage,
        List<ArticleDto> articles
) {
    public static ArticlePageDto from(Page<ArticleDto> articleDtoPage) {
        return new ArticlePageDto(
                articleDtoPage.getNumber() + 1, // 화면 내 직관성을 위해 시작 페이지를 1부터 시작하도록 함
                articleDtoPage.getTotalPages(),
                articleDtoPage.getContent()
        );
    }
}
