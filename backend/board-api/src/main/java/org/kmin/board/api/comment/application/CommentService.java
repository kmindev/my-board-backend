package org.kmin.board.api.comment.application;

import lombok.RequiredArgsConstructor;
import org.kmin.board.api.article.application.ArticleService;
import org.kmin.board.api.article.application.dto.ArticleWithCommentsWithHashtagsDto;
import org.kmin.board.api.comment.application.dto.NewCommentRequestDto;
import org.kmin.board.api.comment.exception.CommentNotFoundException;
import org.kmin.board.api.comment.exception.CommentUserMismatchException;
import org.kmin.board.api.user.application.UserAccountService;
import org.kmin.board.domain.article.Article;
import org.kmin.board.domain.comment.Comment;
import org.kmin.board.domain.comment.repository.CommentRepository;
import org.kmin.board.domain.user.UserAccount;
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
            throw new CommentUserMismatchException();
        }

        commentRepository.deleteById(commentId);
    }

}
