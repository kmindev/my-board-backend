package com.back.domain;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum UserRoleType {
    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN");

    private final String name;

    UserRoleType(String name) {
        this.name = name;
    }

    public static UserRoleType getUserRoleType(String dbData) {
        return Arrays.stream(UserRoleType.values())
                .filter(roleType -> roleType.name.equals(dbData))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("잘못된 유저 권한 타입입니다 : " + dbData));
    }

}
