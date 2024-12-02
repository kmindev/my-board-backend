package com.back.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class UserAccount extends BaseEntity {

    @Id
    @Column(nullable = false, length = 50)
    private String userId; // 유저 id

    @Column(nullable = false) private String userPassword; // 패스워드
    @Column(unique = true, length = 50) private String email; // 이메일
    @Column(unique = true, length = 50) private String nickname; // 닉네임
    @Column private String memo; // 메모
    @Column private String socialProvider; // 소셜 로그인 제공처
    @Column private String socialId; // 유저 소셜 고유 id

}
