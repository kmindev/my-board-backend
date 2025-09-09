package org.kmin.board.api.common.fixture;

import org.kmin.board.api.article.application.dto.ArticleUpdateDto;

public class ArticleUpdateDtoFixture {


    public static ArticleUpdateDto createArticleUpdateDto(Long articleId, String updatedTitle, String updatedContent, String userAccountId) {
        return new ArticleUpdateDto(articleId, updatedTitle, updatedContent, userAccountId);
    }

}
