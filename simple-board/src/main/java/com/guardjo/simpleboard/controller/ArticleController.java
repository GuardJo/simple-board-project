package com.guardjo.simpleboard.controller;

import com.guardjo.simpleboard.domain.ArticleSearchType;
import com.guardjo.simpleboard.dto.ArticleDto;
import com.guardjo.simpleboard.response.ArticleResponse;
import com.guardjo.simpleboard.response.ArticleWithCommentResponse;
import com.guardjo.simpleboard.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/article")
public class ArticleController {
    private final ArticleService articleService;

    public ArticleController(@Autowired ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping
    public String findAllArticles(@RequestParam(required = false) ArticleSearchType articleSearchType,
                                  @RequestParam(required = false) String searchValue,
                                  @PageableDefault(size = 10, sort = "createTime", direction = Sort.Direction.DESC) Pageable pageable,
                                  ModelMap modelMap) {
        Page<ArticleResponse> articleResponseList = articleService.findArticles(articleSearchType, searchValue, pageable).map(ArticleResponse::from);

        modelMap.addAttribute("articles", articleResponseList);
        return "article/index";
    }

    @GetMapping("/{articleId}")
    public String findArticle(@PathVariable Long articleId, ModelMap modelMap) {
        ArticleWithCommentResponse article = ArticleWithCommentResponse.from(articleService.findArticle(articleId));
        modelMap.addAttribute("article", article);
        modelMap.addAttribute("comments", article.commentResponses());

        return "article/detail";
    }
}
