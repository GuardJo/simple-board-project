package com.guardjo.simpleboard.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/article")
public class ArticleController {
    @GetMapping
    public String findAllArticles(ModelMap modelMap) {
        modelMap.addAttribute("articles", List.of());
        return "article/index";
    }

    @GetMapping("/{articleId}")
    public String findArticle(@PathVariable Long articleId, ModelMap modelMap) {
        modelMap.addAttribute("article", "test");
        modelMap.addAttribute("comments", List.of());

        return "article/detail";
    }
}
