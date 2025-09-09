package org.kmin.board.api.common.fixture;

import org.kmin.board.api.article.application.dto.ArticleWithHashtagsDto;
import java.util.Set;

import static org.kmin.board.api.common.fixture.HashtagDtoFixture.createHashtagDto;
import static org.kmin.board.api.common.fixture.UserAccountDtoFixture.createUserAccountDto;

public class ArticleWithHashtagsDtoFixture {

    private static final Long DEFAULT_ID = 1L;
    private static final String DEFAULT_TITLE = "제목";
    private static final String DEFAULT_CONTENT = "내용입니다.";

    /**
     * <p>
     * 기본값으로 구성된 {@link ArticleWithHashtagsDto} 객체를 생성합니다.
     * <ul>
     *   <li>id: {@link ArticleWithHashtagsDtoFixture#DEFAULT_ID}</li>
     *   <li>title: {@link ArticleWithHashtagsDtoFixture#DEFAULT_TITLE}</li>
     *   <li>content: {@link ArticleWithHashtagsDtoFixture#DEFAULT_CONTENT}</li>
     *   <li>userAccountDto: {@link UserAccountDtoFixture#createUserAccountDto()}</li>
     *   <li>hashtagDtos: {@link HashtagDtoFixture#createHashtagDto()} ()}</li>
     * </ul>
     * </p>
     *
     * @return 기본값으로 구성된 {@link ArticleWithHashtagsDto} 객체
     */
    public static ArticleWithHashtagsDto createArticleWithHashtagsDto() {
        return new ArticleWithHashtagsDto(
                DEFAULT_ID, DEFAULT_TITLE, DEFAULT_CONTENT,
                createUserAccountDto(), Set.of(createHashtagDto()),
                null, null, null, null
        );
    }

}
