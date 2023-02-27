package com.guardjo.simpleboard.service;

import com.guardjo.simpleboard.domain.Hashtag;
import com.guardjo.simpleboard.repository.HashtagRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
@Transactional
public class HashtagService {
    private final HashtagRepository hashtagRepository;

    public HashtagService(@Autowired HashtagRepository hashtagRepository) {
        this.hashtagRepository = hashtagRepository;
    }

    /**
     * 게시글 본문을 읽어 해시태그들을 찾아 반환한다.
     * @param content 게시글 본문
     * @return 해시태그 Set
     */
    public Set<Hashtag> parseHashtagsInContent(String content) {
        Set<Hashtag> hashtags = new LinkedHashSet<>();

        if (content == null) {
            return hashtags;
        }

        Pattern pattern = Pattern.compile("(#[\\d|A-Z|a-z|ㄱ-ㅎ|ㅏ-ㅣ|가-힣\\_]*)");
        Matcher matcher = pattern.matcher(content);

        while (matcher.find()) {
            String match = matcher.group().substring(1);

            if (!(match.isBlank() || match.isEmpty())) {
                hashtags.add(Hashtag.of(match));
            }
        }

        return hashtags;
    }

    /**
     * Hahstag들을 저장한다.
     * 이 때, 이미 저장되어 있는 경우에는 제외한다.
     * @param hashtags 저장할 hashtag set
     */
    public void saveHashtag(Set<Hashtag> hashtags) {
        hashtags.forEach(hashtag -> {
            if (!hashtagRepository.existsByHashtagName(hashtag.getHashtagName())) {
                hashtagRepository.save(hashtag);
            }
        });
    }

    /**
     * 해당하는 해시태그를 사용하는 게시글이 없을 경우, 해시태그를 삭제한다.
     * @param hashtagId 특정 해시태그 식별키값
     */
    public void cleanHashtagWithoutArticles(long hashtagId) {
        Hashtag hashtag = hashtagRepository.getReferenceById(hashtagId);

        if (hashtag != null && hashtag.getArticles().size() == 0) {
            hashtagRepository.deleteById(hashtagId);
        }
        else if (hashtag == null) {
            log.warn("[Test] Not Found Hashtag.");
        }
        else if (hashtag.getArticles().size() > 0) {
            log.warn("[Test] This Hashtag is using another Articles");
        }
        else {
            log.error("[Test] Failed Delete Hashtag");
        }
    }
}
