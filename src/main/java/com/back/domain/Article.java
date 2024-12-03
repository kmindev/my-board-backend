package com.back.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Article extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // id

    @JoinColumn(name = "userId")
    @ManyToOne(optional = false)
    private UserAccount userAccount; // 작성자

    @Column(nullable = false) private String title; // 제목
    @Column(nullable = false, length = 65535) private String content; // 본문

    @JoinTable(
            name = "article_hashtag",
            joinColumns = @JoinColumn(name = "articleId"),
            inverseJoinColumns = @JoinColumn(name = "hashtagId")
    )
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<Hashtag> hashtags = new LinkedHashSet<>(); // 해시태그 매핑 테이블(N:M)

}
