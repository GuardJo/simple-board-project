package com.guardjo.simpleboard.repository;

import com.guardjo.simpleboard.config.JpaConfig;
import com.guardjo.simpleboard.config.TestJpaConfig;
import com.guardjo.simpleboard.domain.Article;
import com.guardjo.simpleboard.domain.Comment;
import com.guardjo.simpleboard.domain.Hashtag;
import com.guardjo.simpleboard.domain.Member;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@Import(TestJpaConfig.class)
class JpaRepositoryTest {
    private final int MEMBER_TEST_DATA_SIZE = 50;
    private final int ARTICLE_TEST_DATA_SIZE = 100;
    private final int COMMENT_TEST_DATA_SIZE = 505;
    private final int SUB_COMMENT_TEST_DATA_SIZE = 5;
    private final int HASHTAG_TEST_DATA_SIZE = 50;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private HashtagRepository hashtagRepository;

    @DisplayName("게시글 저장 테스트")
    @Test
    void testSaveArticle() {
        Member member = memberRepository.findById(1L).orElseThrow();

        articleRepository.save(Article.of(member,
                "title", "content"));

        assertThat(articleRepository.count()).isEqualTo(101);
    }

    @DisplayName("게시글 읽기 테스트")
    @Test
    void testReadArticle() {
        List<Article> articleList = articleRepository.findAll();

        assertThat(articleList.size()).isEqualTo(ARTICLE_TEST_DATA_SIZE);
    }

    @DisplayName("게시글 수정 테스트")
    @Test
    void testUpdateArticle() {
        Article article = articleRepository.findById(1L).orElseThrow();
        String updateHashtagName = "#Update";
        Hashtag updateHashtag = Hashtag.of(updateHashtagName);
        article.addHashtag(updateHashtag);

        // @DataJpaTest를 사용함으로써 메소드들이 트랜잭션 간 rollback이 일어나 변경 서항이 저장되지 않기에 saveAndFlush()를 사용해서 저장
        Article updateArticle = articleRepository.saveAndFlush(article);
        
        assertThat(articleRepository.findById(1L).get().getHashtags().contains(updateHashtag)).isTrue();
    }

    @DisplayName("게시글 삭제 테스트")
    @Test
    void testDeleteArticle() {
        Article deleteArticle = articleRepository.findById(1L).orElseThrow();
        int deleteArticleCommentCount = deleteArticle.getComments().size();

        articleRepository.delete(deleteArticle);

        assertThat(articleRepository.count()).isEqualTo(ARTICLE_TEST_DATA_SIZE - 1);
        // comment는 article과의 연관 관계 설정 간 cascade정책으로 인해 해당 article이 제거되면 포함하는 comment들도 제거되야함
        assertThat(commentRepository.count()).isEqualTo(COMMENT_TEST_DATA_SIZE - deleteArticleCommentCount);
    }

    @DisplayName("댓글 저장 테스트")
    @Test
    void testSaveComment() {
        Article article = articleRepository.findById(1L).orElseThrow();
        int oldCount = article.getComments().size();
        Member member = memberRepository.findById(1L).orElseThrow();

        commentRepository.save(Comment.of(member, article, "content"));

        assertThat(commentRepository.count()).isEqualTo(COMMENT_TEST_DATA_SIZE + 1);
        assertThat(article.getComments().size()).isEqualTo(oldCount);
    }

    @DisplayName("댓글 읽기 테스트")
    @Test
    void testReadComment() {
        List<Comment> commentList = commentRepository.findAll();

        assertThat(commentList.size()).isEqualTo(COMMENT_TEST_DATA_SIZE);
    }

    @DisplayName("댓글 수정 테스트")
    @Test
    void testUpdateComment() {
        Comment comment = commentRepository.findById(1L).orElseThrow();
        String updateContent = "update test";
        comment.setContent(updateContent);

        commentRepository.saveAndFlush(comment);

        assertThat(updateContent).isEqualTo(commentRepository.findById(1L).get().getContent());
    }

    @DisplayName("댓글 삭제 테스트")
    @Test
    void testDeleteComment() {
        commentRepository.deleteById(2L);

        assertThat(commentRepository.count()).isEqualTo(COMMENT_TEST_DATA_SIZE - 1);
    }

    @DisplayName("특정 댓글에 대댓글 추가 테스트")
    @Test
    void testCreateSubComment() {
        Comment comment = commentRepository.findById(1L).orElseThrow();
        // data.sql 기반 내 5개의 대댓글을 지니고 있음
        int oldSubCommentsCount = comment.getChildComments().size();
        Comment subComment = Comment.of(comment.getMember(), comment.getArticle(), "test comment");
        comment.addChildComment(subComment);

        commentRepository.flush();

        assertThat(comment).hasFieldOrPropertyWithValue("parentCommentId", null)
                .extracting("childComments", InstanceOfAssertFactories.COLLECTION)
                .hasSize(SUB_COMMENT_TEST_DATA_SIZE + 1);
    }

    @DisplayName("특정 댓글 하위 댓글 반환 테스트")
    @Test
    void testReadSubComments() {
        Comment comment = commentRepository.findById(1L).orElseThrow();

        // id : 1 의 댓글 내에 모든 대댓글 테스트 데이터가 존재함 (5개)
        assertThat(comment).hasFieldOrPropertyWithValue("parentCommentId", null)
                .extracting("childComments", InstanceOfAssertFactories.COLLECTION)
                .hasSize(SUB_COMMENT_TEST_DATA_SIZE);
    }

    @DisplayName("하위 댓글 삭제 테스트")
    @Test
    void testDeleteSubComment() {
        Comment subComment = commentRepository.findById(501L).orElseThrow();
        long parentCommentId = subComment.getParentCommentId();

        commentRepository.deleteById(subComment.getId());

        Comment parentComment = commentRepository.findById(parentCommentId).orElseThrow();

        assertThat(commentRepository.count()).isEqualTo(COMMENT_TEST_DATA_SIZE - 1);
        assertThat(parentComment).hasFieldOrPropertyWithValue("parentCommentId", null)
                .extracting("childComments", InstanceOfAssertFactories.COLLECTION)
                .hasSize(SUB_COMMENT_TEST_DATA_SIZE - 1);
    }

    @DisplayName("특정 댓글 삭제 시 하위 댓글 삭제 여부 테스트")
    @Test
    void testDeleteCommentAndSubComment() {
        commentRepository.deleteById(1L);

        // 해당 Comment 및 하위 댓글 5개 제거
        assertThat(commentRepository.count()).isEqualTo(COMMENT_TEST_DATA_SIZE - 6);
    }

    @DisplayName("해당 댓글 작성자가 댓글 삭제시 하위 댓글 삭제 여부 테스트")
    @Test
    void testDeleteCommentAndSubCommentWithUserId() {
        commentRepository.deleteByIdAndMember_Email(1L, "test@mail.com");

        assertThat(commentRepository.count()).isEqualTo(COMMENT_TEST_DATA_SIZE - 6);
    }

    @DisplayName("회원 추가 테스트")
    @Test
    void testSaveMember() {
        memberRepository.save(Member.of("test@mail.com", "test", "1234"));

        assertThat(memberRepository.count()).isEqualTo(MEMBER_TEST_DATA_SIZE + 1);
    }

    @DisplayName("회원 목록 반환 테스트")
    @Test
    void testReadMembers() {
        List<Member> members = memberRepository.findAll();

        assertThat(members.size()).isEqualTo(MEMBER_TEST_DATA_SIZE);
    }

    @DisplayName("회원 비밀번호 변경 테스트")
    @Test
    void testUpdateMember() {
        Member member = memberRepository.findById(1L).orElseThrow();

        member.setPassword("0000");

        memberRepository.saveAndFlush(member);

        Member updateMemeber = memberRepository.findById(1L).orElseThrow();

        assertThat(updateMemeber.getPassword()).isEqualTo(member.getPassword());
    }

    @DisplayName("회원 삭제 테스트")
    @Test
    void testDeleteMember() {
        memberRepository.deleteById(4L);

        assertThat(memberRepository.count()).isEqualTo(MEMBER_TEST_DATA_SIZE - 1);
    }

    @DisplayName("해시태그 저장 테스트")
    @Test
    void testSaveHashtag() {
        Hashtag hashtag = Hashtag.of("test");

        hashtagRepository.save(hashtag);

        assertThat(hashtagRepository.count()).isEqualTo(HASHTAG_TEST_DATA_SIZE + 1);
    }
    
    @DisplayName("저장된 해시태그 전체 목록 반환")
    @Test
    void testReadHashtags() {
        List<Hashtag> hashtags = hashtagRepository.findAll();

        assertThat(hashtags.size()).isEqualTo(HASHTAG_TEST_DATA_SIZE);
    }
    
    @DisplayName("해시태그 이름 변경 테스트")
    @Test
    void testUpdateHashtagName() {
        Hashtag hashtag = hashtagRepository.findById(1L).get();
        String updateName = "updateName";
        hashtag.setName(updateName);

        hashtagRepository.flush();

        Hashtag updateHashtag = hashtagRepository.findById(1L).get();

        assertThat(updateHashtag.getName()).isEqualTo(updateName);
    }

    @DisplayName("특정 해시태그 삭제 테스트")
    @Test
    void testDeleteHashtag() {
        hashtagRepository.deleteById(1L);

        assertThat(hashtagRepository.count()).isEqualTo(HASHTAG_TEST_DATA_SIZE - 1);
    }
    
    @DisplayName("특정 해시태그명 존재 여부 테스트")
    @Test
    void testExistHashtagName() {
        // 현재 data.sql로 넣어주었던 데이터
        boolean actual = hashtagRepository.existsByName("1");

        assertThat(actual).isTrue();
    }
}