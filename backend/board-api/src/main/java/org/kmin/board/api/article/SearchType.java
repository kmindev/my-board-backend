package org.kmin.board.api.article;

import lombok.Getter;

@Getter
public enum SearchType {
    TITLE("제목"),
    CONTENT("본문"),
    USER_ID("작성자"),
    NICKNAME("닉네임"),
    HASHTAG("해시태그");

    private final String typeName;

    SearchType(String typeName) {
        this.typeName = typeName;
    }
}
