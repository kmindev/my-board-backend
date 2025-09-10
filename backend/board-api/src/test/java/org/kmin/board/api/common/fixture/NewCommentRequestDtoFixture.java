package org.kmin.board.api.common.fixture;

import org.kmin.board.api.apps.comment.application.dto.NewCommentRequestDto;

public class NewCommentRequestDtoFixture {

    private static final Long DEFAULT_ARTICLE_ID = 1L;
    private static final String DEFAULT_CONTENT = "댓글입니다.";
    private static final String DEFAULT_USER_ID = "user1";

    /**
     * <p>
     * 기본값으로 구성된 {@link NewCommentRequestDto} 객체를 생성합니다.
     * <ul>
     *   <li>articleId: {@link NewCommentRequestDtoFixture#DEFAULT_ARTICLE_ID}</li>
     *   <li>content: {@link NewCommentRequestDtoFixture#DEFAULT_CONTENT}</li>
     *   <li>userId: {@link NewCommentRequestDtoFixture#DEFAULT_USER_ID}</li>
     * </ul>
     * </p>
     *
     * @return 기본값으로 구성된 {@link NewCommentRequestDto} 객체
     */
    public static NewCommentRequestDto createNewCommentRequestDto() {
        return new NewCommentRequestDto(DEFAULT_ARTICLE_ID, DEFAULT_CONTENT, DEFAULT_USER_ID);
    }

}
