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
    public Page<ArticleDto> findArticles(@Nullable ArticleSearchType searchType, @Nullable String searchValue, Pageable pageable) {
        log.info("[Test] Request findArtciles searchType = {}, seachValue = {}", searchType, searchValue);

        if (searchValue == null || searchValue.isEmpty() || searchValue.isBlank()) {
            log.warn("[Test] Search Params is Null");
            return articleRepository.findAll(pageable).map(DtoConverter::from);
        }

        Page<ArticleDto> articleDtoPage = switch (searchType) {
            case TITLE -> articleRepository.findByTitleContaining(searchValue, pageable).map(DtoConverter::from);
            case CONTENT -> articleRepository.findByContentContaining(searchValue, pageable).map(DtoConverter::from);
            case CREATOR -> articleRepository.findByCreatorContaining(searchValue, pageable).map(DtoConverter::from);
            case HASHTAG -> articleRepository.findByHashtag(searchValue, pageable).map(DtoConverter::from);
        };

        return articleDtoPage;
    }

    @Transactional(readOnly = true)
    public Page<ArticleDto> sortArticles(ArticleSearchType searchType, Sort.Direction direction, int pageNumber, int pageSize) {
        log.info("[Test] Request sortArticles");

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
        log.info("[Test] Request findArticle, id = {}", id);

        return articleRepository.findById(id)
                .map(DtoConverter::fromArticleWithComment)
                .orElseThrow(() -> new EntityNotFoundException("Not Found Article : " + id));
    }

    public void updateArticle(ArticleUpdateDto updateDto) {
        log.info("[Test] Request Update Article, id = {}", updateDto.id());

        Article article = articleRepository.getReferenceById(updateDto.id());

        if (article == null) {
            throw new EntityNotFoundException("Not Found Article " + updateDto.id());
        }

        article.setTitle(updateDto.title());
        article.setContent(updateDto.content());
        article.setHashtag(updateDto.hashtag());
    }

    public void saveArticle(ArticleDto articleDto, Member member) {
        log.info("[Test] Save Article, title = {}", articleDto.title());

        Article article = ArticleDto.toEntity(articleDto, member);
        articleRepository.save(article);
    }

    public void deleteArticle(long articleId) {
        log.info("[Test] Delete Article, id = {}", articleId);

        articleRepository.deleteById(articleId);
    }

    public List<String> findAllHashtags() {
        return null;
    }

    public Page<ArticleDto> findArticlesViaHashtag(String searchValue, Pageable pageable) {
        return Page.empty(pageable);
    }
}
