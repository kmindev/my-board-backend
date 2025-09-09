package org.kmin.board.api.common.fixture;

import org.kmin.board.domain.article.Article;
import org.kmin.board.domain.user.UserAccount;
import org.springframework.test.util.ReflectionTestUtils;

import static org.kmin.board.api.common.fixture.UserAccountMockDataFixture.createDBUserAccount;


public class ArticleFixture {

    private static final Long DEFAULT_ID = 1L;
    private static final String DEFAULT_TITLE = "제목1";
    private static final String DEFAULT_CONTENT = "내용입니다.";

    /**
     * <p>
     * 기본값으로 구성된 {@link Article} 객체를 생성합니다.
     * <ul>
     *   <li>id: {@link ArticleFixture#DEFAULT_ID}</li>
     *   <li>title: {@link ArticleFixture#DEFAULT_TITLE}</li>
     *   <li>content: {@link ArticleFixture#DEFAULT_CONTENT}</li>
     *   <li>UserAccount: {@link UserAccountMockDataFixture#createDBUserAccount()}</li>
     * </ul>
     * </p>
     *
     * @return 기본값으로 구성된 {@link Article} 객체
     */
    public static Article createDBArticle() {
        Article article = Article.newArticle(createDBUserAccount(), DEFAULT_TITLE, DEFAULT_CONTENT);
        ReflectionTestUtils.setField(article, "id", DEFAULT_ID);
        return article;
    }

    /**
     * <p>
     * 기본값으로 구성된 {@link Article} 객체를 생성합니다.
     * <ul>
     *   <li>id: {@link ArticleFixture#DEFAULT_ID}</li>
     *   <li>title: {@link ArticleFixture#DEFAULT_TITLE}</li>
     *   <li>content: {@link ArticleFixture#DEFAULT_CONTENT}</li>
     *   <li>UserAccount: {@param userAccount}</li>
     * </ul>
     * </p>
     *
     * @return 기본값으로 구성된 {@link Article} 객체
     */
    public static Article createDBArticleFromUserAccount(UserAccount userAccount) {
        Article article = Article.newArticle(userAccount, DEFAULT_TITLE, DEFAULT_CONTENT);
        ReflectionTestUtils.setField(article, "id", DEFAULT_ID);
        return article;
    }

    /**
     * <p>
     * 기본값으로 구성된 {@link Article} 객체를 생성합니다.
     * <ul>
     *   <li>id: {@param articleId}</li>
     *   <li>title: {@link ArticleFixture#DEFAULT_TITLE}</li>
     *   <li>content: {@link ArticleFixture#DEFAULT_CONTENT}</li>
     *   <li>UserAccount: {@param userAccount}</li>
     * </ul>
     * </p>
     *
     * @return 기본값으로 구성된 {@link Article} 객체
     */
    public static Article createDBArticleFromArticleIdAndUserAccount(Long articleId, UserAccount userAccount) {
        Article article = Article.newArticle(userAccount, DEFAULT_TITLE, DEFAULT_CONTENT);
        ReflectionTestUtils.setField(article, "id", articleId);
        return article;
    }

    /**
     * <p>
     * 기본값으로 구성된 {@link Article} 객체를 생성합니다.
     * <ul>
     *   <li>id: {@param articleId}</li>
     *   <li>title: {@link ArticleFixture#DEFAULT_TITLE}</li>
     *   <li>content: {@link ArticleFixture#DEFAULT_CONTENT}</li>
     *   <li>UserAccount: {@link UserAccountMockDataFixture#createDBUserAccount()}</li>
     * </ul>
     * </p>
     *
     * @return 기본값으로 구성된 {@link Article} 객체
     */
    public static Article createDBArticleFromArticleId(Long articleId) {
        Article article = Article.newArticle(createDBUserAccount(), DEFAULT_TITLE, DEFAULT_CONTENT);
        ReflectionTestUtils.setField(article, "id", articleId);
        return article;
    }

}