package org.kmin.board.api.common.fixture;

import static org.kmin.board.api.common.fixture.UserAccountDtoFixture.createUserAccountDto;

import org.kmin.board.api.apps.article.application.dto.CommentDto;

public class CommentDtoFixture {

    private static final Long DEFAULT_ID = 1L;
    private static final Long ARTICLE_ID = 1L;
    private static final String DEFAULT_CONTENT = "댓글입니다.";

    /**
     * <p>
     * 기본값으로 구성된 {@link CommentDto} 객체를 생성합니다.
     * <ul>
     *   <li>id: {@link CommentDtoFixture#DEFAULT_ID}</li>
     *   <li>articleId: {@link CommentDtoFixture#ARTICLE_ID}</li>
     *   <li>userAccountDto: {@link UserAccountDtoFixture#createUserAccountDto()}</li>
     *   <li>content: {@link CommentDtoFixture#DEFAULT_CONTENT}</li>
     * </ul>
     * </p>
     *
     * @return 기본값으로 구성된 {@link CommentDto} 객체
     */
    public static CommentDto createCommentDto() {
        return new CommentDto(
                DEFAULT_ID,
                ARTICLE_ID,
                createUserAccountDto(),
                DEFAULT_CONTENT,
                null,
                null,
                null,
                null
        );
    }

}
