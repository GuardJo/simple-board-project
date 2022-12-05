package com.guardjo.simpleboard.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Objects;

@Getter
@ToString
@Table(indexes = {
        @Index(columnList = "content"),
        @Index(columnList = "hashtag"),
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
    private String hashtag;

    public void setMember(Member member) {
        this.member = member;
    }

    protected Comment() {

    }

    private Comment(Member member, Article article, String content, String hashtag) {
        this.member = member;
        this.article = article;
        this.content = content;
        this.hashtag = hashtag;
    }

    public static Comment of(Member member, Article article, String content, String hashtag) {
        return new Comment(member, article, content, hashtag);
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
}
