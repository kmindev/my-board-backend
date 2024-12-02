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
public class Hashtag extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // id

    @Column(length = 50, unique = true) private String hashtagName; // 해시태그 이름

    @ManyToMany(mappedBy = "hashtags")
    private Set<Article> articles = new LinkedHashSet<>(); // 게시글 매핑 테이블(N:M)

}
