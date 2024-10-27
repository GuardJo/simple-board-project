package com.guardjo.simpleboard.service;

import com.guardjo.simpleboard.domain.Article;
import com.guardjo.simpleboard.domain.Comment;
import com.guardjo.simpleboard.domain.Member;
import com.guardjo.simpleboard.dto.CommentCreateRequest;
import com.guardjo.simpleboard.dto.CommentDto;
import com.guardjo.simpleboard.dto.CommentUpdateRequest;
import com.guardjo.simpleboard.generator.TestDataGenerator;
import com.guardjo.simpleboard.repository.ArticleRepository;
import com.guardjo.simpleboard.repository.CommentRepository;
import com.guardjo.simpleboard.repository.MemberRepository;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AuthorizationServiceException;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {
    private final static TestDataGenerator TEST_DATA_GENERATOR = new TestDataGenerator();
    private final static Member TEST_MEMBER = Member.of("test@mail.com", "tester", "1234");

    @InjectMocks
    private CommentService commentService;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private ArticleRepository articleRepository;
    @Mock
    private MemberRepository memberRepository;

    @DisplayName("게시글 id를 통해 해당 게시글의 댓글 리스트 반환 테스트")
    @Test
    void testFindComments() {
        Article article = TEST_DATA_GENERATOR.generateArticle("test");

        article.getComments().add(Comment.of(TEST_MEMBER, article, "test comtent"));

        given(articleRepository.getReferenceById(any(Long.class))).willReturn(article);

        List<CommentDto> commentDtoList = commentService.findComments(1L);

        assertThat(commentDtoList.size()).isEqualTo(1);
        assertThat(commentDtoList.get(0).content()).isEqualTo("test comtent");
    }

    @DisplayName("게시글 id를 통해 해당 게시글의 댓글 및 대댓글 리스트 반환 테스트")
    @Test
    void testFindCommentsWithSubComments() {
        Article article = TEST_DATA_GENERATOR.generateArticle("test");
        Comment parentComment = Comment.of(TEST_MEMBER, article, "test comtent");
        Comment childComment = Comment.of(TEST_MEMBER, article, "test sub comment");
        parentComment.addChildComment(childComment);

        article.getComments().add(parentComment);

        given(articleRepository.getReferenceById(any(Long.class))).willReturn(article);

        List<CommentDto> commentDtoList = commentService.findComments(1L);

        assertThat(commentDtoList.size()).isEqualTo(1);
        assertThat(commentDtoList.get(0).content()).isEqualTo("test comtent");
        assertThat(commentDtoList.get(0)).hasFieldOrPropertyWithValue("parentCommentId", null)
                .extracting("childComments", InstanceOfAssertFactories.COLLECTION)
                .hasSize(1);
    }

    @DisplayName("특정 댓글 삭제 테스트")
    @Test
    void testDeleteComment() {
        String memberMail = "test@mail.com";
        willDoNothing().given(commentRepository).deleteByIdAndMember_Email(anyLong(), anyString());
        commentService.deleteComment(1L, memberMail);

        then(commentRepository).should().deleteByIdAndMember_Email(anyLong(), anyString());
    }

    @DisplayName("특정 댓글 저장 테스트")
    @Test
    void testSaveComment() {
        Article article = Article.of(TEST_MEMBER, "title", "content");
        CommentDto commentDto = TEST_DATA_GENERATOR.convertCommentDto(TEST_DATA_GENERATOR.generateComment("test content", 1L));
        String memberId = TEST_MEMBER.getEmail();

        given(articleRepository.getReferenceById(any())).willReturn(article);
        given(commentRepository.save(any(Comment.class))).willReturn(CommentDto.toEntity(commentDto, TEST_MEMBER, article));
        given(memberRepository.findByEmail(anyString())).willReturn(Optional.of(TEST_MEMBER));

        commentService.saveComment(commentDto, memberId);

        then(articleRepository).should().getReferenceById(any());
        then(commentRepository).should().save(any(Comment.class));
        then(memberRepository).should().findByEmail(anyString());
    }

    @DisplayName("신규 댓글 저장 테스트")
    @Test
    void test_createComment() {
        String memberMail = TEST_MEMBER.getEmail();
        String content = "test content";
        Long articleId = 999L;
        Article article = Article.of(TEST_MEMBER, "title", "content");
        Comment expected = Comment.of(TEST_MEMBER, article, content);
        CommentCreateRequest createRequest = new CommentCreateRequest(articleId, null, content);

        ArgumentCaptor<Comment> commentArgumentCaptor = ArgumentCaptor.forClass(Comment.class);

        given(memberRepository.findByEmail(eq(memberMail))).willReturn(Optional.of(TEST_MEMBER));
        given(articleRepository.getReferenceById(eq(articleId))).willReturn(article);
        given(commentRepository.save(commentArgumentCaptor.capture())).willReturn(expected);

        assertThatCode(() -> commentService.createComment(createRequest, memberMail)).doesNotThrowAnyException();
        Comment actual = commentArgumentCaptor.getValue();

        assertThat(actual).isNotNull();
        assertThat(actual.getContent()).isEqualTo(expected.getContent());

        then(memberRepository).should().findByEmail(eq(memberMail));
        then(articleRepository).should().getReferenceById(eq(articleId));
        then(commentRepository).should().save(any(Comment.class));
    }

    @DisplayName("신규 댓글 저장 테스트 : 대댓글")
    @Test
    void test_createComment_childComment() {
        String memberMail = TEST_MEMBER.getEmail();
        String content = "test content";
        Long articleId = 999L;
        Long parentCommentId = 111L;
        Article article = Article.of(TEST_MEMBER, "title", "content");
        Comment expected = Comment.of(TEST_MEMBER, article, content, parentCommentId);
        CommentCreateRequest createRequest = new CommentCreateRequest(articleId, parentCommentId, content);

        ArgumentCaptor<Comment> commentArgumentCaptor = ArgumentCaptor.forClass(Comment.class);

        given(memberRepository.findByEmail(eq(memberMail))).willReturn(Optional.of(TEST_MEMBER));
        given(articleRepository.getReferenceById(eq(articleId))).willReturn(article);
        given(commentRepository.save(commentArgumentCaptor.capture())).willReturn(expected);

        assertThatCode(() -> commentService.createComment(createRequest, memberMail)).doesNotThrowAnyException();
        Comment actual = commentArgumentCaptor.getValue();

        assertThat(actual).isNotNull();
        assertThat(actual.getContent()).isEqualTo(expected.getContent());
        assertThat(actual.getParentCommentId()).isEqualTo(parentCommentId);

        then(memberRepository).should().findByEmail(eq(memberMail));
        then(articleRepository).should().getReferenceById(eq(articleId));
        then(commentRepository).should().save(any(Comment.class));
    }

    @DisplayName("특정 댓글 본문 수정 테스트")
    @Test
    void test_updateComment() {
        Comment comment = TEST_DATA_GENERATOR.generateComment("test", 1L);
        String requestMail = comment.getMember().getEmail();
        CommentUpdateRequest updateRequest = new CommentUpdateRequest(comment.getId(), "Update Comment!");

        given(commentRepository.findById(eq(comment.getId()))).willReturn(Optional.of(comment));

        assertThatCode(() -> commentService.updateComment(updateRequest, requestMail)).doesNotThrowAnyException();

        then(commentRepository).should().findById(eq(comment.getId()));
    }

    @DisplayName("특정 댓글 본문 수정 테스트 : 권한 없는 요청자인 경우")
    @Test
    void test_updateComment_forbiddenUser() {
        Comment comment = TEST_DATA_GENERATOR.generateComment("test", 1L);
        String anotherUserMail = "tttt@mail.com";
        CommentUpdateRequest updateRequest = new CommentUpdateRequest(comment.getId(), "Update Comment!");

        given(commentRepository.findById(eq(comment.getId()))).willReturn(Optional.of(comment));

        assertThatCode(() -> commentService.updateComment(updateRequest, anotherUserMail))
                .isInstanceOf(AuthorizationServiceException.class);

        then(commentRepository).should().findById(eq(comment.getId()));
    }

    @DisplayName("특정 댓글 본문 수정 테스트 : 댓글을 찾을 수 없는 경우")
    @Test
    void test_updateComment_notFoundEntity() {
        String requestMail = TEST_MEMBER.getEmail();
        Long commentId = 999L;
        CommentUpdateRequest updateRequest = new CommentUpdateRequest(commentId, "Update Comment!");

        given(commentRepository.findById(eq(commentId))).willReturn(Optional.empty());

        assertThatCode(() -> commentService.updateComment(updateRequest, requestMail))
                .isInstanceOf(EntityNotFoundException.class);

        then(commentRepository).should().findById(eq(commentId));
    }
}