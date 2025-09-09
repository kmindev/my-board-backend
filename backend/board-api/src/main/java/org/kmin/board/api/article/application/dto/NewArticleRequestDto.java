package org.kmin.board.api.article.application.dto;

import org.kmin.board.domain.article.Article;
import org.kmin.board.domain.user.UserAccount;

public record NewArticleRequestDto(
        String title, // 제목
        String content, // 내용
        String userId // 사용자 id
) {
    public Article newArticle(UserAccount userAccount) {
        return Article.newArticle(userAccount, this.title, this.content);
    }
}
