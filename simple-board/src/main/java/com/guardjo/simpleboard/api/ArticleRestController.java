package com.guardjo.simpleboard.api;

import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.guardjo.simpleboard.domain.ArticleSearchType;
import com.guardjo.simpleboard.domain.Hashtag;
import com.guardjo.simpleboard.dto.ArticleCreateRequest;
import com.guardjo.simpleboard.dto.ArticleDetailInfo;
import com.guardjo.simpleboard.dto.ArticleDto;
import com.guardjo.simpleboard.dto.ArticlePageDto;
import com.guardjo.simpleboard.dto.security.SimpleBoardPrincipal;
import com.guardjo.simpleboard.service.ArticleService;
import com.guardjo.simpleboard.service.HashtagService;
import com.guardjo.simpleboard.service.MemberService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ArticleRestController {
	private final ArticleService articleService;
	private final HashtagService hashtagService;
	private final MemberService memberService;

	@GetMapping(UrlContext.ARTICLES_URL)
	public ArticlePageDto getArticlePage(@PageableDefault(sort = "createTime", direction = Sort.Direction.DESC) Pageable pageable,
		@RequestParam(required = false, defaultValue = "TITLE") ArticleSearchType searchType,
		@RequestParam(required = false, defaultValue = "") String searchValue) {
		log.info("Get All Feeds, searchType = {}, searchValue = {}", searchType, searchValue);

		Page<ArticleDto> articleDtoPage = articleService.findArticles(searchType, searchValue, pageable);

		return ArticlePageDto.from(articleDtoPage);
	}

	@GetMapping(UrlContext.ARTICLES_URL + "/{articleId}")
	public ArticleDetailInfo getArticleDetailInfo(@PathVariable("articleId") Long articleId) {
		log.info("Get ArticleDetailInfo, articleId = {}", articleId);

		return articleService.findArticleDetailInfo(articleId);
	}

	@PostMapping(UrlContext.ARTICLES_URL)
	public void createNewArticle(@RequestBody ArticleCreateRequest createRequest, @AuthenticationPrincipal SimpleBoardPrincipal principal) {
		log.info("Request POST : {}", UrlContext.ARTICLES_URL);

		Set<Hashtag> newHashtags = hashtagService.parseHashtagsInContent(createRequest.content());
		articleService.saveArticle(createRequest, principal.getUsername(), newHashtags);
	}
}
