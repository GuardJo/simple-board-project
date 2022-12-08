package com.guardjo.simpleboard.service;

import com.guardjo.simpleboard.domain.Article;
import com.guardjo.simpleboard.domain.ArticleSearchType;
import com.guardjo.simpleboard.domain.Member;
import com.guardjo.simpleboard.dto.ArticleDto;
import com.guardjo.simpleboard.dto.ArticleUpdateDto;
import com.guardjo.simpleboard.dto.ArticleWithCommentDto;
import com.guardjo.simpleboard.repository.ArticleRepository;
import com.guardjo.simpleboard.util.DtoConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Slf4j
@Transactional
@Service
public class ArticleService {
    @Autowired
    private ArticleRepository articleRepository;

    private DtoConverter dtoConverter = new DtoConverter();

    @Transactional(readOnly = true)
    public Page<ArticleDto> findArticles(ArticleSearchType searchType, @Nullable String searchValue, Pageable pageable) {
        if (searchValue == null || searchValue.isEmpty() || searchValue.isBlank()) {
            return articleRepository.findAll(pageable).map(DtoConverter::from);
        }

        Page<ArticleDto> articleDtoPage = switch (searchType) {
            case TITLE -> articleRepository.findByTitleContaining(searchValue, pageable).map(DtoConverter::from);
            case CONTENT -> articleRepository.findByContentContaining(searchValue, pageable).map(DtoConverter::from);
            case CREATOR -> articleRepository.findByCreatorContaining(searchValue, pageable).map(DtoConverter::from);
            case HASHTAG -> articleRepository.findByHashtag(searchValue, pageable).map(DtoConverter::from);
            case CREATETIME -> articleRepository.findByCreateTimeEquals(searchValue, pageable).map(DtoConverter::from);
        };

        return articleDtoPage;
    }

    @Transactional(readOnly = true)
    public Page<ArticleDto> sortArticles(ArticleSearchType searchType, Sort.Direction direction, int pageNumber, int pageSize) {
        Sort sort = switch (direction) {
            case ASC -> Sort.by(searchType.name()).ascending();
            case DESC -> Sort.by(searchType.name()).descending();
        };

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        Page<ArticleDto> articleDtoPage = articleRepository.findAll(pageable).map(DtoConverter::from);

        return articleDtoPage;
    }

    @Transactional(readOnly = true)
    public ArticleWithCommentDto findArticle(long id) {
        return articleRepository.findById(id)
                .map(DtoConverter::fromArticleWithComment)
                .orElseThrow(() -> new EntityNotFoundException("Not Found Article : " + id));
    }

    public void updateArticle(ArticleUpdateDto updateDto) {
        Article article = articleRepository.getReferenceById(updateDto.id());

        if (article == null) {
            throw new EntityNotFoundException("Not Found Article " + updateDto.id());
        }

        article.setTitle(updateDto.title());
        article.setContent(updateDto.content());
        article.setHashtag(updateDto.hashtag());
    }

    public void saveArticle(ArticleDto articleDto, Member member) {
        Article article = ArticleDto.toEntity(articleDto, member);
        articleRepository.save(article);
    }

    public void deleteArticle(long articleId) {
        articleRepository.deleteById(articleId);
    }
}