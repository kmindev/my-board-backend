package com.back.comment.application;

import com.back.article.application.ArticleService;
import com.back.article.domain.Article;
import com.back.comment.domain.Comment;
import com.back.user.application.UserAccountService;
import com.back.user.domain.UserAccount;
import com.back.comment.domain.exception.CommentNotFoundException;
import com.back.user.domain.exception.UserMismatchException;
import com.back.comment.infrastructure.repository.CommentRepository;
import com.back.article.application.dto.ArticleWithCommentsWithHashtagsDto;
import com.back.comment.application.dto.NewCommentRequestDto;
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
