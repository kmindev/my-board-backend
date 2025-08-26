package com.back.comment.application;

import com.back.article.application.ArticleService;
import com.back.article.domain.Article;
import com.back.comment.domain.Comment;
import com.back.common.fixture.CommentMockDataFactory;
import com.back.user.application.UserAccountService;
import com.back.user.domain.UserAccount;
import com.back.article.domain.exception.ArticleNotFoundException;
import com.back.user.domain.exception.UserMismatchException;
import com.back.comment.infrastructure.repository.CommentRepository;
import com.back.article.application.dto.ArticleWithCommentsWithHashtagsDto;
import com.back.comment.application.dto.NewCommentRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.back.common.fixture.ArticleMockDataFactory.createDBArticle;
import static com.back.common.fixture.CommentMockDataFactory.createDBCommentFromCommentIdAndUserAccount;
import static com.back.common.fixture.UserAccountMockDataFactory.createDBUserAccountFromUserId;
import static com.back.common.fixture.NewCommentRequestDtoFactory.createNewCommentRequestDto;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.*;

@DisplayName("비즈니스 로직 - 댓글")
@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {

    @InjectMocks
    private CommentService sut;

    @Mock
    private CommentRepository commentRepository;
    @Mock
    private ArticleService articleService;
    @Mock
    private UserAccountService userAccountService;

    @DisplayName("댓글 정보를 전달하면, 댓글 정보를 DB에 저장하고, 게시글(댓글, 해시태그) 정보를 반환한다.")
    @Test
    void givenCommentInfo_whenNewComment_thenSavesCommentAndReturnArticleWithHashtagsWithComments() {
        // Given
        NewCommentRequestDto newCommentRequestDto = createNewCommentRequestDto();
        UserAccount userAccount = createDBUserAccountFromUserId(newCommentRequestDto.userId());
        Article findArticle = createDBArticle();
        Comment comment = CommentMockDataFactory.createDBCommentFromArticleAndUserAccount(findArticle, userAccount);

        given(articleService.findArticle(anyLong())).willReturn(findArticle);
        given(userAccountService.getUserAccount(newCommentRequestDto.userId())).willReturn(userAccount);
        given(commentRepository.save(any(Comment.class))).willReturn(comment);

        // When
        ArticleWithCommentsWithHashtagsDto result = sut.newComment(newCommentRequestDto);

        // Then
        assertThat(result.id()).isEqualTo(findArticle.getId());
        then(articleService).should().findArticle(anyLong());
        then(userAccountService).should().getUserAccount(newCommentRequestDto.userId());
        then(commentRepository).should().save(any(Comment.class));
    }

    @DisplayName("댓글 정보를 전달했는데, 찾을 수 없는 게시글이면 예외가 발생한다.")
    @Test
    void givenCommentInfo_whenNewComment_thenThrowsException() {
        // Given
        NewCommentRequestDto newCommentRequestDto = createNewCommentRequestDto();
        given(articleService.findArticle(anyLong())).willThrow(new ArticleNotFoundException());

        // When
        ArticleNotFoundException result = assertThrows(ArticleNotFoundException.class,
                () -> sut.newComment(newCommentRequestDto)
        );

        // Then
        assertThat(result).isInstanceOf(ArticleNotFoundException.class);
        then(articleService).should().findArticle(anyLong());
    }

    @DisplayName("삭제할 댓글 id와 작성자 id를 전달하면, 댓글을 삭제한다.")
    @Test
    void givenCommentIdAndUserId_whenDeleteComment_thenDeletesComment() {
        // Given
        Long commentId = 1L;
        String userId = "user1";
        UserAccount userAccount = createDBUserAccountFromUserId(userId);
        Comment comment = createDBCommentFromCommentIdAndUserAccount(commentId, userAccount);

        given(commentRepository.findById(commentId)).willReturn(Optional.of(comment));
        given(userAccountService.getUserAccount(userId)).willReturn(userAccount);
        willDoNothing().given(commentRepository).deleteById(commentId);

        // When
        sut.deleteComment(commentId, userId);

        // Then
        then(commentRepository).should().findById(commentId);
        then(userAccountService).should().getUserAccount(userId);
        then(commentRepository).should().deleteById(commentId);
    }

    @DisplayName("댓글 작성자가 일치하지 않으면, 예외가 발생한다.")
    @Test
    void givenCommentIdAndUserId_whenDeleteComment_thenThrowsException() {
        // Given
        Long commentId = 1L;
        String userId = "user1";
        UserAccount userAccount = createDBUserAccountFromUserId(userId);
        String otherUserAccountId = "user2";
        UserAccount otherUserAccount = createDBUserAccountFromUserId(otherUserAccountId);
        Comment comment = createDBCommentFromCommentIdAndUserAccount(commentId, userAccount);

        given(commentRepository.findById(commentId)).willReturn(Optional.of(comment));
        given(userAccountService.getUserAccount(userId)).willReturn(otherUserAccount);

        // When
        UserMismatchException result = assertThrows(UserMismatchException.class,
                () -> sut.deleteComment(commentId, userId)
        );

        // Then
        assertThat(result).isInstanceOf(UserMismatchException.class);
        then(commentRepository).should().findById(commentId);
        then(userAccountService).should().getUserAccount(userId);
    }

}
