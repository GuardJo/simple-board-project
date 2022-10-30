package com.guardjo.simpleboard.repository;

import com.guardjo.simpleboard.config.JpaConfig;
import com.guardjo.simpleboard.domain.Article;
import com.guardjo.simpleboard.domain.Comment;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@Import(JpaConfig.class)
class JpaRepositoryTest {
    private final int ARTICLE_TEST_DATA_SIZE = 100;
    private final int COMMENT_TEST_DATA_SIZE = 500;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private CommentRepository commentRepository;

    @DisplayName("게시글 저장 테스트")
    @Test
    void testSaveArticle() {
        articleRepository.save(Article.of("title", "content", "#hashtag"));

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
        String updateHashtag = "#Update";
        article.setHashtag(updateHashtag);

        // @DataJpaTest를 사용함으로써 메소드들이 트랜잭션 간 rollback이 일어나 변경 서항이 저장되지 않기에 saveAndFlush()를 사용해서 저장
        Article updateArticle = articleRepository.saveAndFlush(article);
        
        assertThat(updateHashtag).isEqualTo(articleRepository.findById(1L).get().getHashtag());
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

        commentRepository.save(Comment.of(article, "content", "#hashtag"));

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
        String updateHashtag = "updateTag";

        comment.setHashtag(updateHashtag);

        commentRepository.saveAndFlush(comment);

        assertThat(updateHashtag).isEqualTo(commentRepository.findById(1L).get().getHashtag());
    }

    @DisplayName("댓글 삭제 테스트")
    @Test
    void testDeleteComment() {
        commentRepository.deleteById(1L);

        assertThat(commentRepository.count()).isEqualTo(COMMENT_TEST_DATA_SIZE - 1);
    }
}