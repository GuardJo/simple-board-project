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
                articleDtoPage.getNumber(),
                articleDtoPage.getTotalPages(),
                articleDtoPage.getContent()
        );
    }
}
