package com.guardjo.simpleboard.api;

import com.guardjo.simpleboard.domain.ArticleSearchType;
import com.guardjo.simpleboard.dto.ArticleDto;
import com.guardjo.simpleboard.dto.ArticlePageDto;
import com.guardjo.simpleboard.service.ArticleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ArticleRestController {
    private final ArticleService articleService;

    @GetMapping(UrlContext.ARTICLES_URL)
    public ArticlePageDto getArticlePage(@PageableDefault(sort = "createTime", direction = Sort.Direction.DESC) Pageable pageable,
                                         @RequestParam(required = false, defaultValue = "TITLE") ArticleSearchType searchType,
                                         @RequestParam(required = false, defaultValue = "") String searchValue) {
        log.info("Get All Feeds, searchType = {}, searchValue = {}", searchType, searchValue);

        Page<ArticleDto> articleDtoPage = articleService.findArticles(searchType, searchValue, pageable);

        return ArticlePageDto.from(articleDtoPage);
    }
}
