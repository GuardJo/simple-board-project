package com.guardjo.simpleboard.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
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
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Article article;

    @Setter
    @Column(nullable = false, length = 500)
    private String content;

    @Setter
    private String hashtag;

    @CreatedBy
    private String creator;

    @CreatedDate
    private LocalDateTime createTime;

    @LastModifiedBy
    private String modifier;

    @LastModifiedDate
    private LocalDateTime modifiedTime;

    protected Comment() {

    }

    private Comment(Article article, String content, String hashtag) {
        this.article = article;
        this.content = content;
        this.hashtag = hashtag;
    }

    public Comment of(Article article, String content, String hashtag) {
        return new Comment(article, content, hashtag);
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
