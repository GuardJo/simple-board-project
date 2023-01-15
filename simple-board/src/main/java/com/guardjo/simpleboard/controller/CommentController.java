package com.guardjo.simpleboard.controller;

import com.guardjo.simpleboard.dto.CommentDto;
import com.guardjo.simpleboard.dto.security.SimpleBoardPrincipal;
import com.guardjo.simpleboard.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/comment")
public class CommentController {
    private final CommentService commentService;

    public CommentController(@Autowired CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public String saveComment(CommentDto commentDto, @AuthenticationPrincipal SimpleBoardPrincipal principal) {
        log.info("[Test] request save comment : {}", commentDto.content());
        Long articleId = commentDto.articleId();

        commentService.saveComment(commentDto, principal.getUsername());

        return "redirect:/article/" + articleId;
    }

    @DeleteMapping("/{commentId}")
    public String deleteComment(@PathVariable Long commentId, Long articleId, @AuthenticationPrincipal SimpleBoardPrincipal principal) {
        log.info("[Test] request delete comment : {}", commentId);

        commentService.deleteComment(commentId, principal.email());

        return "redirect:/article/" + articleId;
    }
}
