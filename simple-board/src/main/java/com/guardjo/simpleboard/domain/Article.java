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
        @Index(name = "title", columnList = "title"),
        @Index(name = "hashtag", columnList = "hashtag"),
        @Index(name = "creator", columnList = "creator"),
        @Index(name = "createTime", columnList = "createTime")
})
@Entity
public class Article extends MetaInfoData {
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

    @Setter
    @ManyToOne(optional = false)
    @JoinColumn(name = "memberId")
    private Member member;

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    @OrderBy("id")
    private final Set<Comment> comments = new LinkedHashSet<>();

    protected Article() {

    }

    private Article(Member member, String title, String content, String hashtag) {
        this.member = member;
        this.title = title;
        this.content = content;
        this.hashtag = hashtag;
    }

    public static Article of(Member member, String title, String content, String hashtag) {
        return new Article(member, title, content, hashtag);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Article article)) return false;
        return Objects.equals(title, article.title) && Objects.equals(content, article.content) && Objects.equals(hashtag, article.hashtag) && Objects.equals(member, article.member) && Objects.equals(comments, article.comments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, content, hashtag, member, comments);
    }
}
