package com.guardjo.simpleboard.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@ToString(callSuper = true)
@Entity
@Table(indexes = {
        @Index(name = "email", columnList = "email"),
        @Index(name = "name", columnList = "name")
})
public class Member extends MetaInfoData{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Setter
    @Column(nullable = false)
    private String email;

    @Setter
    @Column(nullable = false, length = 100)
    private String name;

    @Setter
    @Column(nullable = false, length = 100)
    private String password;

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @OrderBy("id")
    @ToString.Exclude
    private Set<Article> articles = new HashSet<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @OrderBy("id")
    @ToString.Exclude
    private Set<Comment> comments = new HashSet<>();

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    public void setArticles(Set<Article> articles) {
        this.articles = articles;
    }

    protected Member(String email, String name, String password, String creator) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.creator = creator;
        this.modifier = creator;
    }

    public Member() {

    }

    public static Member of(String email, String name, String password) {
        return new Member(email, name, password, null);
    }

    /**
     * OAuth2를 통해 외부로부터 회원을 받기 위해 생성자를 별도 파라미터로 추가
     * @param email 회원 메일 및 id
     * @param name 회원명
     * @param password 회원 비밀번호
     * @param creator 생성자
     * @return 회원 Entity
     */
    public static Member of(String email, String name, String password, String creator) {
        return new Member(email, name, password, creator);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Member member)) return false;
        return Objects.equals(email, member.email) && Objects.equals(name, member.name) && Objects.equals(password, member.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, name, password);
    }
}
