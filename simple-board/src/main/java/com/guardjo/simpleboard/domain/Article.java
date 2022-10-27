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
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@ToString
@Table(indexes = {
        @Index(name = "title", columnList = "title"),
        @Index(name = "hashtag", columnList = "hashtag"),
        @Index(name = "creator", columnList = "creator"),
        @Index(name = "createTime", columnList = "createTime")
})
@Entity
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(nullable = false)
    private String title;

    @Setter
    @Column(nullable = false, length = 5000)
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

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    @OrderBy("id")
    private final Set<Comment> comments = new LinkedHashSet<>();

    protected Article() {

    }

    private Article(String title, String content, String hashtag) {
        this.title = title;
        this.content = content;
        this.hashtag = hashtag;
    }

    public static Article of(String title, String content, String hashtag) {
        return new Article(title, content, hashtag);
    }


}