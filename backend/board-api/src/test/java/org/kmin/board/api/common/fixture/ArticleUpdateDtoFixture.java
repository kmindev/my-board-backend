package org.kmin.board.api.common.fixture;

import org.kmin.board.api.apps.article.application.dto.ArticleUpdateDto;

public class ArticleUpdateDtoFixture {


    public static ArticleUpdateDto createArticleUpdateDto(Long articleId, String updatedTitle, String updatedContent, String userAccountId) {
        return new ArticleUpdateDto(articleId, updatedTitle, updatedContent, userAccountId);
    }

}
