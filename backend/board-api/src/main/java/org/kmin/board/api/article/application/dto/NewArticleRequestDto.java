package org.kmin.board.api.article.application.dto;

import org.kmin.board.api.article.domain.Article;
import org.kmin.board.api.user.domain.UserAccount;

public record NewArticleRequestDto(
        String title, // 제목
        String content, // 내용
        String userId // 사용자 id
) {
    public Article newArticle(UserAccount userAccount) {
        return Article.newArticle(userAccount, this.title, this.content);
    }
}
