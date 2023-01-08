package com.guardjo.simpleboard.controller;

import com.guardjo.simpleboard.domain.Article;
import com.guardjo.simpleboard.domain.ArticleSearchType;
import com.guardjo.simpleboard.domain.FormType;
import com.guardjo.simpleboard.domain.Member;
import com.guardjo.simpleboard.dto.ArticleDto;
import com.guardjo.simpleboard.dto.ArticleUpdateDto;
import com.guardjo.simpleboard.response.ArticleResponse;
import com.guardjo.simpleboard.response.ArticleWithCommentResponse;
import com.guardjo.simpleboard.service.ArticleService;
import com.guardjo.simpleboard.service.PaginationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/article")
public class ArticleController {
    private final ArticleService articleService;
    private final PaginationService paginationService;

    public ArticleController(@Autowired ArticleService articleService,
                             @Autowired PaginationService paginationService) {
        this.articleService = articleService;
        this.paginationService = paginationService;
    }

    @GetMapping
    public String findAllArticles(@RequestParam(required = false) ArticleSearchType articleSearchType,
                                  @RequestParam(required = false) String searchValue,
                                  @PageableDefault(size = 10, sort = "createTime", direction = Sort.Direction.DESC) Pageable pageable,
                                  ModelMap modelMap) {
        log.info("[Test] Requested /");

        Page<ArticleResponse> articleResponseList = articleService.findArticles(articleSearchType, searchValue, pageable).map(ArticleResponse::from);
        List<Integer> paginationNumbers = paginationService.getPaginationNumbers(pageable.getPageNumber(), articleResponseList.getTotalPages());

        modelMap.addAttribute("articles", articleResponseList);
        modelMap.addAttribute("paginationNumbers", paginationNumbers);
        modelMap.addAttribute("articleSearchTypes", ArticleSearchType.values());
        return "article/index";
    }

    @GetMapping("/{articleId}")
    public String findArticle(@PathVariable Long articleId, ModelMap modelMap) {
        log.info("[Test] Requested /{}", articleId);

        ArticleWithCommentResponse article = ArticleWithCommentResponse.from(articleService.findArticle(articleId));
        modelMap.addAttribute("article", article);
        modelMap.addAttribute("comments", article.commentResponses());

        return "article/detail";
    }

    @DeleteMapping("/{articleId}")
    public String deleteArticle(@PathVariable Long articleId) {
        log.info("Request Delete Article (id : {})", articleId);

        articleService.deleteArticle(articleId);

        return "redirect:/article";
    }

    @GetMapping("/search-hashtag")
    public String searchHashtag(@RequestParam(required = false) String searchValue,
                                @PageableDefault(size = 10, sort = "createTime", direction = Sort.Direction.DESC) Pageable pageable,
                                ModelMap modelMap) {

        Page<ArticleResponse> articleResponseList = articleService.findArticlesViaHashtag(searchValue, pageable).map(ArticleResponse::from);
        List<Integer> paginationNumbers = paginationService.getPaginationNumbers(pageable.getPageNumber(), articleResponseList.getTotalPages());
        List<String> hashtags = articleService.findAllHashtags();

        modelMap.addAttribute("articles", articleResponseList);
        modelMap.addAttribute("paginationNumbers", paginationNumbers);
        modelMap.addAttribute("hashtags", hashtags);
        modelMap.addAttribute("articleSearchType", ArticleSearchType.HASHTAG);

        return "article/search-hashtag";
    }

    @GetMapping("/update-view/{articleId}")
    public String updateArticleView(@PathVariable Long articleId, ModelMap modelMap) {
        log.info("[Test] Request /update-article/{}", articleId);

        ArticleWithCommentResponse article = ArticleWithCommentResponse.from(articleService.findArticle(articleId));
        modelMap.addAttribute("article", article);
        modelMap.addAttribute("formType", FormType.UPDATE);

        return "article/form";
    }

    @PostMapping("/update-view/{articleId}")
    public String updateArticle(@PathVariable Long articleId, ArticleUpdateDto articleUpdateDto) {
        log.info("Request Update Article : {}", articleUpdateDto.title());

        articleService.updateArticle(articleUpdateDto);

        return "redirect:/article/" + articleId;
    }

    @GetMapping("/create-view")
    public String createArticleView(ModelMap modelMap) {
        log.info("[Test] Request /create-view");

        modelMap.addAttribute("formType", FormType.CREATE);

        return "article/form";
    }

    @PostMapping("/create-view")
    public String createArticle(ArticleDto articleDto) {
        log.info("[Test] Request Create Article : {}", articleDto.title());

        articleService.saveArticle(articleDto, Member.of("test@mail.com", "tester", "pwd"));

        return "redirect:/article";
    }
}
