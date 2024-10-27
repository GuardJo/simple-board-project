package com.guardjo.simpleboard.api;

import com.guardjo.simpleboard.dto.CommentCreateRequest;
import com.guardjo.simpleboard.dto.security.SimpleBoardPrincipal;
import com.guardjo.simpleboard.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
public class ArticleCommentRestController {
    private final CommentService commentService;

    @PostMapping(UrlContext.COMMENTS_URL)
    public void createArticleComment(@RequestBody CommentCreateRequest createRequest, @AuthenticationPrincipal SimpleBoardPrincipal principal) {
        log.info("POST : " + UrlContext.COMMENTS_URL);

        commentService.createComment(createRequest, principal.email());
    }

    @DeleteMapping(UrlContext.COMMENTS_URL + "/{commentId}")
    public void removeArticleComment(@PathVariable Long commentId, @AuthenticationPrincipal SimpleBoardPrincipal principal) {
        log.info("DELETE : " + UrlContext.COMMENTS_URL + "/" + commentId);
        
        commentService.deleteComment(commentId, principal.email());
    }
}
