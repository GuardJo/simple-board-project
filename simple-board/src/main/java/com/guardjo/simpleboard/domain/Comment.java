package com.guardjo.simpleboard.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@ToString
@Table(indexes = {
        @Index(columnList = "content"),
        @Index(columnList = "creator"),
        @Index(columnList = "createTime")
})
@Entity
public class Comment extends MetaInfoData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @ManyToOne(optional = false)
    private Article article;

    @Setter
    @ManyToOne(optional = false)
    @JoinColumn(name = "memberId")
    private Member member;

    @Setter
    @Column(nullable = false, length = 500)
    private String content;

    @Setter
    @Column(updatable = false)
    private Long parentCommentId;

    @ToString.Exclude
    @OneToMany(mappedBy = "parentCommentId", cascade = CascadeType.ALL)
    @OrderBy("createTime ASC")
    private Set<Comment> childComments = new LinkedHashSet<>();

    protected Comment() {

    }

    private Comment(Member member, Article article, String content, Long parentCommentId) {
        this.member = member;
        this.article = article;
        this.content = content;
        this.parentCommentId = parentCommentId;
    }

    public static Comment of(Member member, Article article, String content) {
        return Comment.of(member, article, content, null);
    }

    public static Comment of(Member member, Article article, String content, Long parentCommentId) {
        return new Comment(member, article, content, parentCommentId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Comment)) return false;
        Comment comment = (Comment) o;
        return id != null && Objects.equals(id, comment.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public void addChildComment(Comment childComment) {
        childComment.setParentCommentId(this.getId());
        this.getChildComments().add(childComment);
    }

    public void addAllChildComment(Set<Comment> comments) {
        childComments.forEach(
                comment -> {
                    comment.setParentCommentId(this.id);
                    this.addChildComment(comment);
                }
        );
    }

    public boolean hasChildComments() {
        return this.childComments.size() == 0 ? false : true;
    }
}
