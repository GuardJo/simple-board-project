package com.guardjo.simpleboard.service;

import com.guardjo.simpleboard.domain.Comment;
import com.guardjo.simpleboard.dto.CommentDto;
import com.guardjo.simpleboard.repository.ArticleRepository;
import com.guardjo.simpleboard.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ArticleRepository articleRepository;

    public List<CommentDto> findComments(long articleId) {
        return null;
    }

    public void deleteComment(long commentId) {

    }

    public void saveComment(Comment comment) {
    }
}
