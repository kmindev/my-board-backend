package org.kmin.board.domain.fixture;

import static org.kmin.board.domain.fixture.ArticleFixture.createDBArticle;

import org.kmin.board.domain.article.Article;
import org.kmin.board.domain.comment.Comment;
import org.kmin.board.domain.user.UserAccount;
import org.springframework.test.util.ReflectionTestUtils;

public class CommentFixture {

    private static final Long DEFAULT_ID = 1L;
    private static final String DEFAULT_CONTENT = "댓글입니다.";

    /**
     * <p>
     * 기본값으로 구성된 {@link Comment} 객체를 생성합니다.
     * <ul>
     *   <li>id: {@link CommentFixture#DEFAULT_ID}</li>
     *   <li>content: {@link CommentFixture#DEFAULT_CONTENT}</li>
     *   <li>article: {@param article}</li>
     *   <li>userAccount: {@param userAccount}</li>
     * </ul>
     * </p>
     *
     * @return 기본값으로 구성된 {@link Comment} 객체
     */
    public static Comment createDBCommentFromArticleAndUserAccount(Article article, UserAccount userAccount) {
        Comment comment = Comment.newComment(article, userAccount, DEFAULT_CONTENT);
        ReflectionTestUtils.setField(comment, "id", DEFAULT_ID);
        return comment;
    }

    /**
     * <p>
     * 기본값으로 구성된 {@link Comment} 객체를 생성합니다.
     * <ul>
     *   <li>id: {@param commentId}</li>
     *   <li>content: {@link CommentFixture#DEFAULT_CONTENT}</li>
     *   <li>article: {@link ArticleFixture#createDBArticle()}</li>
     *   <li>userAccount: {@param userAccount}</li>
     * </ul>
     * </p>
     *
     * @return 기본값으로 구성된 {@link Comment} 객체
     */
    public static Comment createDBCommentFromCommentIdAndUserAccount(Long commentId, UserAccount userAccount) {
        Comment comment = Comment.newComment(createDBArticle(), userAccount, DEFAULT_CONTENT);
        ReflectionTestUtils.setField(comment, "id", commentId);
        return comment;
    }

}
