package com.guardjo.simpleboard.service;

import com.guardjo.simpleboard.domain.Article;
import com.guardjo.simpleboard.domain.Comment;
import com.guardjo.simpleboard.domain.Member;
import com.guardjo.simpleboard.dto.ArticleDto;
import com.guardjo.simpleboard.dto.CommentDto;
import com.guardjo.simpleboard.dto.MemberDto;
import com.guardjo.simpleboard.dto.security.SimpleBoardPrincipal;
import com.guardjo.simpleboard.repository.ArticleRepository;
import com.guardjo.simpleboard.repository.CommentRepository;
import com.guardjo.simpleboard.repository.MemberRepository;
import com.guardjo.simpleboard.util.DtoConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private MemberRepository memberRepository;

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
        Member member = memberRepository.findByEmail(memberMail).get();

        commentRepository.save(CommentDto.toEntity(commentDto, member, article, null));
    }
}
