package com.back.common.fixture;

import com.back.comment.presentation.dto.request.NewCommentRequest;

public class NewCommentRequestFixture {

    private static final Long DEFAULT_ARTICLE_ID = 1L;
    private static final String DEFAULT_CONTENT = "댓글입니다.";

    /**
     * <p>
     * 기본값으로 구성된 {@link NewCommentRequest} 객체를 생성합니다.
     * <ul>
     *   <li>articleId: {@link NewCommentRequestFixture#DEFAULT_ARTICLE_ID}</li>
     *   <li>content: {@link NewCommentRequestFixture#DEFAULT_CONTENT}</li>
     * </ul>
     * </p>
     *
     * @return 기본값으로 구성된 {@link NewCommentRequest} 객체
     */
    public static NewCommentRequest createNewCommentRequest() {
        return new NewCommentRequest(DEFAULT_ARTICLE_ID, DEFAULT_CONTENT);
    }

}
