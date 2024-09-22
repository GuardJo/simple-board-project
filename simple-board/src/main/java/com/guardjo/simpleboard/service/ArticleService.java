package com.guardjo.simpleboard.service;

import com.guardjo.simpleboard.domain.Article;
import com.guardjo.simpleboard.domain.ArticleSearchType;
import com.guardjo.simpleboard.domain.Hashtag;
import com.guardjo.simpleboard.domain.Member;
import com.guardjo.simpleboard.dto.*;
import com.guardjo.simpleboard.repository.ArticleRepository;
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

    @Transactional(readOnly = true)
    public Page<ArticleDto> findArticles(@Nullable ArticleSearchType searchType, @Nullable String searchValue, Pageable pageable) {
        log.info("[Test] Request findArtciles searchType = {}, seachValue = {}", searchType, searchValue);

        if (searchValue == null || searchValue.isEmpty() || searchValue.isBlank()) {
            log.warn("[Test] Search Params is Null");
            return articleRepository.findAll(pageable).map(DtoConverter::from);
        }

        return switch (searchType) {
            case CONTENT -> articleRepository.findByContentContaining(searchValue, pageable).map(DtoConverter::from);
            case CREATOR -> articleRepository.findByCreatorContaining(searchValue, pageable).map(DtoConverter::from);
            case HASHTAG -> articleRepository.findByHashtagName(searchValue, pageable).map(DtoConverter::from);
            default -> articleRepository.findByTitleContaining(searchValue, pageable).map(DtoConverter::from);
        };
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

    /**
     * 주어진 식별키에 해당하는 게시글 상세 정보를 반환받는다.
     *
     * @param articleId 게시글 식별키
     * @param userMail  조회 요청자 메일 (로그인한 계정이 아닐 경우 빈 문자열이 입력된다)
     * @return 게시글 정보 및 관련 댓글 목록
     */
    @Transactional(readOnly = true)
    public ArticleDetailInfo findArticleDetailInfo(long articleId, String userMail) {
        log.debug("Find ArticleDetailInfo, articleId = {}", articleId);

        ArticleDetailInfo articleDetailInfo = articleRepository.findById(articleId)
                .map(article -> DtoConverter.toArticleDetailInfo(article, userMail))
                .orElseThrow(() -> new EntityNotFoundException("Not Found Article : " + articleId));

        log.info("Found ArticleDetailInfo, articleId = {}", articleDetailInfo.article().id());

        return articleDetailInfo;
    }

    public void updateArticle(ArticleUpdateDto updateDto, String userMail, Set<Hashtag> hashtags) {
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
            article.clearHashtags();
            article.addHashtags(hashtags);
        }
    }

    /**
     * 신규 게시글을 저장한다.
     *
     * @param createRequest 저장할 게시글 정보 (제목, 본문)
     * @param authorMail    직성자 메일
     * @param hashtags      함께 저장할 게시글 내 해시태그 목록
     */
    public void saveArticle(ArticleCreateRequest createRequest, String authorMail, Set<Hashtag> hashtags) {
        log.info("[Test] Save Article, title = {}", createRequest.title());
        Member member = memberRepository.findByEmail(authorMail)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Not Found Member, email = %s", authorMail)));
        Article article = Article.of(member, createRequest.title(), createRequest.content());
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
