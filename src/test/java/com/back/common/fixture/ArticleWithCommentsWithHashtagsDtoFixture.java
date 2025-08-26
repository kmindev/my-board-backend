package com.back.common.fixture;

import com.back.article.application.dto.ArticleWithCommentsWithHashtagsDto;
import java.util.Set;

import static com.back.common.fixture.CommentDtoFixture.createCommentDto;
import static com.back.common.fixture.HashtagDtoFixture.createHashtagDto;
import static com.back.common.fixture.UserAccountDtoFixture.createUserAccountDto;

public class ArticleWithCommentsWithHashtagsDtoFixture {

    private static final Long DEFAULT_ID = 1L;
    private static final String DEFAULT_TITLE = "제목";
    private static final String DEFAULT_CONTENT = "내용입니다.";

    /**
     * <p>
     * 기본값으로 구성된 {@link ArticleWithCommentsWithHashtagsDto} 객체를 생성합니다.
     * <ul>
     *   <li>id: {@link ArticleWithCommentsWithHashtagsDtoFixture#DEFAULT_ID}</li>
     *   <li>commentDtos: {@link CommentDtoFixture#createCommentDto()}</li>
     *   <li>hashtagDtos: {@link HashtagDtoFixture#createHashtagDto()}</li>
     *   <li>userAccountDto: {@link UserAccountDtoFixture#createUserAccountDto()}</li>
     *   <li>title: {@link ArticleWithCommentsWithHashtagsDtoFixture#DEFAULT_TITLE}</li>
     *   <li>content: {@link ArticleWithCommentsWithHashtagsDtoFixture#DEFAULT_CONTENT}</li>
     * </ul>
     * </p>
     *
     * @return 기본값으로 구성된 {@link ArticleWithCommentsWithHashtagsDto} 객체
     */
    public static ArticleWithCommentsWithHashtagsDto createArticleWithCommentsWithHashtagsDto() {
        return new ArticleWithCommentsWithHashtagsDto(
                DEFAULT_ID,
                Set.of(createCommentDto()),
                Set.of(createHashtagDto()),
                createUserAccountDto(),
                DEFAULT_TITLE,
                DEFAULT_CONTENT,
                null,
                null,
                null,
                null
        );
    }

}
