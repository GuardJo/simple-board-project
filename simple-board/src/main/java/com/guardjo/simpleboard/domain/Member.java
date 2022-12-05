package com.guardjo.simpleboard.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@ToString
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
    private Set<Article> articles = new HashSet<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @OrderBy("id")
    private Set<Comment> comments = new HashSet<>();

    protected Member(String email, String name, String password) {
        this.email = email;
        this.name = name;
        this.password = password;
    }

    public Member() {

    }

    public static Member of(String email, String name, String password) {
        return new Member(email, name, password);
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
