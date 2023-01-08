package com.guardjo.simpleboard.service;

import com.guardjo.simpleboard.domain.Article;
import com.guardjo.simpleboard.domain.Comment;
import com.guardjo.simpleboard.domain.Member;
import com.guardjo.simpleboard.dto.ArticleDto;
import com.guardjo.simpleboard.dto.CommentDto;
import com.guardjo.simpleboard.dto.MemberDto;
import com.guardjo.simpleboard.repository.ArticleRepository;
import com.guardjo.simpleboard.repository.CommentRepository;
import com.guardjo.simpleboard.util.DtoConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ArticleRepository articleRepository;

    @Transactional(readOnly = true)
    public List<CommentDto> findComments(long articleId) {
        Article article = articleRepository.getReferenceById(articleId);

        Set<CommentDto> commentDtos = DtoConverter.from(article.getComments());

        return commentDtos.stream().toList();
    }

    public void deleteComment(long commentId) {
        commentRepository.deleteById(commentId);
    }

    public void saveComment(CommentDto commentDto) {
        Article article = articleRepository.getReferenceById(commentDto.articleId());

        commentRepository.save(CommentDto.toEntity(commentDto, Member.of("test@mail.com", "teser", "1234"), article));
    }
}
