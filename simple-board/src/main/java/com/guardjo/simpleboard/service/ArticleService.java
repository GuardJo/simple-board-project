package com.guardjo.simpleboard.service;

import com.guardjo.simpleboard.domain.ArticleSearchType;
import com.guardjo.simpleboard.dto.ArticleDto;
import com.guardjo.simpleboard.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ArticleService {
    @Autowired
    private ArticleRepository articleRepository;

    public Page<ArticleDto> findArticles(ArticleSearchType searchType, String searchValue) {
        return null;
    }

    public Page<ArticleDto> sortArticles(ArticleSearchType searchType, Sort.Direction direction) {
        return null;
    }

    public ArticleDto findArticle(long id) {
        return null;
    }
}
