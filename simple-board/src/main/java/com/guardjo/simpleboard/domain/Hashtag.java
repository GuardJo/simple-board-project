package com.guardjo.simpleboard.domain;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(indexes = {
        @Index(name = "hashtagName", columnList = "hashtagName"),
        @Index(name = "creator", columnList = "creator"),
        @Index(name = "createTime", columnList = "createTime")
})
@Data
@ToString
public class Hashtag extends MetaInfoData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hashtag_id")
    private Long id;
    @Column(length = 100, nullable = false, unique = true)
    private String hashtagName;

    @ToString.Exclude
    @ManyToMany(mappedBy = "hashtags")
    private final Set<Article> articles = new LinkedHashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Hashtag hashtag)) return false;
        return Objects.equals(hashtagName, hashtag.hashtagName) && Objects.equals(articles, hashtag.articles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hashtagName, articles);
    }

    protected Hashtag() {

    }

    private Hashtag(String name) {
        this.hashtagName = name;
    }

    public static Hashtag of(String name) {
        return new Hashtag(name);
    }
}
