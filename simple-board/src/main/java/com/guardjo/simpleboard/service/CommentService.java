package com.guardjo.simpleboard.service;

import com.guardjo.simpleboard.domain.Article;
import com.guardjo.simpleboard.domain.Comment;
import com.guardjo.simpleboard.domain.Member;
import com.guardjo.simpleboard.dto.CommentDto;
import com.guardjo.simpleboard.repository.ArticleRepository;
import com.guardjo.simpleboard.repository.CommentRepository;
import com.guardjo.simpleboard.repository.MemberRepository;
import com.guardjo.simpleboard.util.DtoConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Set;

@Service
@Transactional
@Slf4j
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private MemberRepository memberRepository;

    /**
     * 주어진 article에 댓글을 저장한다.
     *
     * @param articleId   article 식별키
     * @param content     댓글 내용
     * @param creatorMail 작성자 메일
     */
    public void createComment(long articleId, String content, String creatorMail) {
        log.debug("Create New Comment, articleId = {}, content = {}, creatorMail = {}", articleId, content, creatorMail);

        Member member = memberRepository.findByEmail(creatorMail).orElseThrow(EntityNotFoundException::new);
        Article article = articleRepository.getReferenceById(articleId);

        Comment newComment = commentRepository.save(Comment.of(member, article, content));

        log.info("Created New Content, commentId = {}, articleId = {}", newComment.getId(), articleId);
    }

    @Transactional(readOnly = true)
    public List<CommentDto> findComments(long articleId) {
        Article article = articleRepository.getReferenceById(articleId);

        Set<CommentDto> commentDtos = DtoConverter.from(article.getComments());

        return commentDtos.stream().toList();
    }

    public void deleteComment(long commentId, String memberMail) {
        commentRepository.deleteByIdAndMember_Email(commentId, memberMail);
    }

    public void saveComment(CommentDto commentDto, String memberMail) {
        Article article = articleRepository.getReferenceById(commentDto.articleId());
        Member member = memberRepository.findByEmail(memberMail)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Not Founnd MEMBER entity, email = %s", memberMail)));

        commentRepository.save(CommentDto.toEntity(commentDto, member, article));
    }
}
