package com.guardjo.simpleboard.service;

import com.guardjo.simpleboard.domain.Article;
import com.guardjo.simpleboard.domain.Comment;
import com.guardjo.simpleboard.dto.CommentDto;
import com.guardjo.simpleboard.repository.ArticleRepository;
import com.guardjo.simpleboard.repository.CommentRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {
    @InjectMocks
    private CommentService commentService;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private ArticleRepository articleRepository;

    @DisplayName("게시글 id를 통해 해당 게시글의 댓글 리스트 반환 테스트")
    @Test
    void testFindComments() {
        Article article = Article.of("title", "content", "#hashtag");
        given(articleRepository.findById(any(Long.class))).willReturn(Optional.of(article));

        List<CommentDto> commentDtoList = commentService.findComments(1L);

        assertThat(commentDtoList.isEmpty()).isFalse();
    }

    @DisplayName("특정 댓글 삭제 테스트")
    @Test
    void testDeleteComment() {
        willDoNothing().given(commentRepository).deleteById(any(Long.class));
        commentService.deleteComment(1L);

        then(commentRepository).should().deleteById(any(Long.class));
    }

    @DisplayName("특정 댓글 저장 테스트")
    @Test
    void testSaveComment() {
        given(commentRepository.save(any(Comment.class))).willReturn(any(Comment.class));
        commentService.saveComment(Comment.of(
                Article.of("title", "content", "#hashtag"),
                "content",
                "#hashtag"
        ));

        then(commentRepository).should().save(any(Comment.class));
    }
}