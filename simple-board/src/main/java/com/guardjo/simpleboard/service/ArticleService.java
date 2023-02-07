package com.guardjo.simpleboard.service;

import com.guardjo.simpleboard.domain.Article;
import com.guardjo.simpleboard.domain.ArticleSearchType;
import com.guardjo.simpleboard.domain.Hashtag;
import com.guardjo.simpleboard.domain.Member;
import com.guardjo.simpleboard.dto.ArticleDto;
import com.guardjo.simpleboard.dto.ArticleUpdateDto;
import com.guardjo.simpleboard.dto.ArticleWithCommentDto;
import com.guardjo.simpleboard.repository.ArticleRepository;
import com.guardjo.simpleboard.repository.HashtagRepository;
import com.guardjo.simpleboard.repository.MemberRepository;
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
import java.util.Optional;
import java.util.Set;

@Slf4j
@Transactional
@Service
public class ArticleService {
    @Autowired
    private MemberRepository memberRepository;
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
            case HASHTAG -> articleRepository.findByHashtagName(searchValue, pageable).map(DtoConverter::from);
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

    public void updateArticle(ArticleUpdateDto updateDto, String userMail) {
        log.info("[Test] Request Update Article, id = {}", updateDto.id());

        Article article = articleRepository.getReferenceById(updateDto.id());
        Optional<Member> member = memberRepository.findByEmail(userMail);

        if (article == null) {
            throw new EntityNotFoundException("Not Found Article " + updateDto.id());
        }

        if (member.isEmpty()) {
            throw new EntityNotFoundException("Not Found User " + userMail);
        } else if (!member.get().getEmail().equals(article.getCreator())) {
            throw new EntityNotFoundException("This User is No forbidden Update Article, " + userMail);
        } else {
            article.setTitle(updateDto.title());
            article.setContent(updateDto.content());
        }
    }

    public void saveArticle(ArticleDto articleDto, String memberMail, Set<Hashtag> hashtags) {
        log.info("[Test] Save Article, title = {}", articleDto.title());
        Member member = memberRepository.findByEmail(memberMail).get();
        Article article = ArticleDto.toEntity(articleDto, member);
        article.addHashtags(hashtags);

        articleRepository.save(article);
    }

    public void deleteArticle(long articleId, String userId) {
        log.info("[Test] Delete Article, id = {}", articleId);

        articleRepository.deleteByIdAndMember_Email(articleId, userId);
    }

    @Transactional(readOnly = true)
    public List<String> findAllHashtags() {
        return articleRepository.findAllDistinctHashtags();
    }

    @Transactional(readOnly = true)
    public Page<ArticleDto> findArticlesViaHashtag(String searchValue, Pageable pageable) {
        log.info("[Test] Find Articles With Hashtag = {}", searchValue);

        if (searchValue == null || searchValue.isEmpty() || searchValue.isBlank()) {
            log.warn("[Test] Search Params is Null");
            return Page.empty(pageable);
        }

        return articleRepository.findByHashtagName(searchValue, pageable).map(DtoConverter::from);
    }
}
