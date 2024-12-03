package com.back.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // id

    @JoinColumn(name = "articleId")
    @ManyToOne(optional = false)
    private Article article; // 게시글

    @JoinColumn(name = "userId")
    @ManyToOne(optional = false)
    private UserAccount userAccount; // 작성자

    @Column private Long parentCommentId; // 부모 댓글 id
    @Column(nullable = false, length = 500) private String content; // 본문

}
