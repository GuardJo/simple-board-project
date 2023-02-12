package com.guardjo.simpleboard.response;

import com.guardjo.simpleboard.dto.CommentDto;
import com.guardjo.simpleboard.generator.TestDataGenerator;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class CommentResponseTest {
    private final TestDataGenerator testDataGenerator = new TestDataGenerator();
    private final long ARTICLE_ID = 1L;
    private final String PARENT_COMMENT_ID_FIELD = "parentCommentId";
    private final String CHILD_COMMENTS_FIELD = "childComments";

    @DisplayName("하위 댓글이 없는 댓글 -> CommentResponse 변환 테스트")
    @Test
    void testConvertCommentResponse() {
        CommentDto commentDto = testDataGenerator.generateCommentDto(1L, ARTICLE_ID, null, "test");

        CommentResponse commentResponse = CommentResponse.from(commentDto);

        assertThat(commentResponse).hasFieldOrPropertyWithValue(PARENT_COMMENT_ID_FIELD, null)
                .extracting(CHILD_COMMENTS_FIELD, InstanceOfAssertFactories.COLLECTION)
                .isEmpty();
    }

    @DisplayName("1차 대댓글 -> CommentResponse 변환 테스트")
    @Test
    void testConvert_1_sub_CommentResponse() {
        long parentCommentId = 1L;
        CommentDto parentCommentDto = testDataGenerator.generateCommentDto(parentCommentId, ARTICLE_ID, null, "test");
        CommentDto childCommentDto = testDataGenerator.generateCommentDto(2L, ARTICLE_ID, parentCommentId, "test2");

        parentCommentDto.addAllChildComments(Set.of(childCommentDto));

        CommentResponse commentResponse = CommentResponse.from(parentCommentDto);

        assertThat(commentResponse).hasFieldOrPropertyWithValue(PARENT_COMMENT_ID_FIELD, null)
                .extracting(CHILD_COMMENTS_FIELD, InstanceOfAssertFactories.COLLECTION)
                .hasSize(1)
                .first()
                .hasFieldOrPropertyWithValue(PARENT_COMMENT_ID_FIELD, parentCommentId)
                .extracting(CHILD_COMMENTS_FIELD, InstanceOfAssertFactories.COLLECTION)
                .isEmpty();
        
    }

    /**
     * n차 대댓글 관련 view는 제거되었으나 기능은 남겨둠
     */
    @DisplayName("2차 대댓글 -> CommentResponse 변환 테스트")
    @Test
    void testConvert_2_sub_CommentResponse() {
        long parentCommentId = 1L;
        long childCommentId = 2L;
        CommentDto parentCommentDto = testDataGenerator.generateCommentDto(parentCommentId, ARTICLE_ID, null, "test");
        CommentDto childCommentDto = testDataGenerator.generateCommentDto(childCommentId, ARTICLE_ID, parentCommentId, "test2");
        CommentDto childChildCommentDto = testDataGenerator.generateCommentDto(3L, ARTICLE_ID, childCommentId, "test3");

        childCommentDto.addAllChildComments(Set.of(childChildCommentDto));
        parentCommentDto.addAllChildComments(Set.of(childCommentDto));

        CommentResponse commentResponse = CommentResponse.from(parentCommentDto);

        assertThat(commentResponse).hasFieldOrPropertyWithValue(PARENT_COMMENT_ID_FIELD, null)
                .extracting(CHILD_COMMENTS_FIELD, InstanceOfAssertFactories.COLLECTION)
                .hasSize(1)
                .first()
                .hasFieldOrPropertyWithValue(PARENT_COMMENT_ID_FIELD, parentCommentId)
                .extracting(CHILD_COMMENTS_FIELD, InstanceOfAssertFactories.COLLECTION)
                .hasSize(1)
                .first()
                .hasFieldOrPropertyWithValue(PARENT_COMMENT_ID_FIELD, childCommentId)
                .extracting(CHILD_COMMENTS_FIELD, InstanceOfAssertFactories.COLLECTION)
                .isEmpty();
    }
}