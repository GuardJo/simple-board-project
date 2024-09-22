package com.guardjo.simpleboard.api;

import com.guardjo.simpleboard.domain.ArticleSearchType;
import com.guardjo.simpleboard.domain.Hashtag;
import com.guardjo.simpleboard.dto.ArticleCreateRequest;
import com.guardjo.simpleboard.dto.ArticleDetailInfo;
import com.guardjo.simpleboard.dto.ArticleDto;
import com.guardjo.simpleboard.dto.ArticlePageDto;
import com.guardjo.simpleboard.dto.security.SimpleBoardPrincipal;
import com.guardjo.simpleboard.service.ArticleService;
import com.guardjo.simpleboard.service.HashtagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@CrossOrigin(maxAge = 3600)
@Slf4j
public class ArticleRestController {
    private final ArticleService articleService;
    private final HashtagService hashtagService;

    @GetMapping(UrlContext.ARTICLES_URL)
    public ArticlePageDto getArticlePage(@PageableDefault(sort = "createTime", direction = Sort.Direction.DESC) Pageable pageable,
                                         @RequestParam(required = false, defaultValue = "TITLE") ArticleSearchType searchType,
                                         @RequestParam(required = false, defaultValue = "") String searchValue) {
        log.info("Get All Feeds, searchType = {}, searchValue = {}", searchType, searchValue);

        Page<ArticleDto> articleDtoPage = articleService.findArticles(searchType, searchValue, pageable);

        return ArticlePageDto.from(articleDtoPage);
    }

    @GetMapping(UrlContext.ARTICLES_URL + "/{articleId}")
    public ArticleDetailInfo getArticleDetailInfo(@PathVariable("articleId") Long articleId, @AuthenticationPrincipal SimpleBoardPrincipal principal) {
        log.info("Get ArticleDetailInfo, articleId = {}", articleId);

        String userMail = "";

        if (!Objects.isNull(principal)) {
            userMail = principal.getName();
        }

        return articleService.findArticleDetailInfo(articleId, userMail);
    }

    @PostMapping(UrlContext.ARTICLES_URL)
    public void createNewArticle(@RequestBody ArticleCreateRequest createRequest, @AuthenticationPrincipal SimpleBoardPrincipal principal) {
        log.info("Request POST : {}", UrlContext.ARTICLES_URL);

        Set<Hashtag> newHashtags = hashtagService.parseHashtagsInContent(createRequest.content());
        articleService.saveArticle(createRequest, principal.getUsername(), newHashtags);
    }
}
