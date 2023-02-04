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
    @ManyToOne(optional = false)
    @JoinColumn(name = "memberId")
    private Member member;

    @ToString.Exclude
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    @OrderBy("id")
    private final Set<Comment> comments = new LinkedHashSet<>();

    @ToString.Exclude
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "article_hashtag",
            joinColumns = @JoinColumn(name = "article_id"),
            inverseJoinColumns = @JoinColumn(name = "hashtag_id")
    )
    private Set<Hashtag> hashtags = new LinkedHashSet<>();

    protected Article() {

    }

    private Article(Member member, String title, String content) {
        this.member = member;
        this.title = title;
        this.content = content;
    }

    public static Article of(Member member, String title, String content) {
        return new Article(member, title, content);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Article article)) return false;
        return Objects.equals(title, article.title) && Objects.equals(content, article.content) && Objects.equals(member, article.member) && Objects.equals(comments, article.comments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, content, member, comments);
    }

    public void addHashtag(Hashtag hashtag) {
        this.hashtags.add(hashtag);
    }

    public void addHashtags(Set<Hashtag> hashtags) {
        this.hashtags.addAll(hashtags);
    }

    public void removeHashtag(Hashtag hashtag) {
        this.hashtags.remove(hashtag);
    }

    public void clearHashtags() {
        this.hashtags.clear();
    }
}
