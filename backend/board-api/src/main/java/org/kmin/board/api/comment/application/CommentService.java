package org.kmin.board.api.comment.application;

import org.kmin.board.api.article.application.ArticleService;
import org.kmin.board.api.article.domain.Article;
import org.kmin.board.api.comment.domain.Comment;
import org.kmin.board.api.user.application.UserAccountService;
import org.kmin.board.api.user.domain.UserAccount;
import org.kmin.board.api.comment.domain.exception.CommentNotFoundException;
import org.kmin.board.api.user.domain.exception.UserMismatchException;
import org.kmin.board.api.comment.infrastructure.repository.CommentRepository;
import org.kmin.board.api.article.application.dto.ArticleWithCommentsWithHashtagsDto;
import org.kmin.board.api.comment.application.dto.NewCommentRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class CommentService {

    private final ArticleService articleService;
    private final UserAccountService userAccountService;
    private final CommentRepository commentRepository;

    public ArticleWithCommentsWithHashtagsDto newComment(NewCommentRequestDto dto) {
        Article findArticle = articleService.findArticle(dto.articleId());
        UserAccount findUserAccount = userAccountService.getUserAccount(dto.userId());
        Comment newComment = Comment.newComment(findArticle, findUserAccount, dto.content());
        commentRepository.save(newComment);

        return ArticleWithCommentsWithHashtagsDto.from(findArticle);
    }

    public void deleteComment(Long commentId, String userId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);
        UserAccount userAccount = userAccountService.getUserAccount(userId);
        if (!comment.getUserAccount().equals(userAccount)) {
            throw new UserMismatchException();
        }

        commentRepository.deleteById(commentId);
    }

}
