package org.kmin.board.api.common.fixture;

import org.kmin.board.api.article.application.dto.NewArticleRequestDto;

public class NewArticleRequestDtoFixture {

    private static final String DEFAULT_TITLE = "제목";
    private static final String DEFAULT_CONTENT = "내용입니다.";
    private static final String DEFAULT_USER_ID = "user1";

    /**
     * <p>
     * 기본값으로 구성된 {@link NewArticleRequestDto} 객체를 생성합니다.
     * <ul>
     *   <li>title: {@link NewArticleRequestDtoFixture#DEFAULT_TITLE}</li>
     *   <li>content: {@link NewArticleRequestDtoFixture#DEFAULT_CONTENT}</li>
     *   <li>userId: {@link NewArticleRequestDtoFixture#DEFAULT_USER_ID}</li>
     * </ul>
     * </p>
     *
     * @return 기본값으로 구성된 {@link NewArticleRequestDto} 객체
     */
    public static NewArticleRequestDto createNewArticleRequestDto() {
        return new NewArticleRequestDto(DEFAULT_TITLE, DEFAULT_CONTENT, DEFAULT_USER_ID);
    }

}
